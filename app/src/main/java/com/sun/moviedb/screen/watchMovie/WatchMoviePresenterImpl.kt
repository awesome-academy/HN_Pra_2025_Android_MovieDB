package com.sun.moviedb.screen.watchMovie

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import com.sun.moviedb.data.model.Member
import com.sun.moviedb.data.repository.rtdb.MemberRepository
import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.utils.MemberListener
import com.sun.moviedb.utils.session.RoomSession

@OptIn(UnstableApi::class)
class WatchMoviePresenterImpl(
    private val contextProvider: () -> Context,
    private val memberRepository: MemberRepository
) : WatchMovieContract.Presenter {

    private var view: WatchMovieContract.View? = null
    private var exoPlayer: ExoPlayer? = null

    private var currentM3u8Link: String? = null
    private var playbackPosition = 0L
    private var playWhenReady = true

    private var isPlayerPrepared = false
    private var isSurfaceReady = false

    private val cachedMembers = mutableListOf<Member>()
    private var roomId: String? = null

    private var observingRoomId: String? = null
    private var isListening = false

    override fun attachView(view: WatchMovieContract.View) {
        this.view = view
    }

    override fun detachView() {
        releaseActualPlayer()
        this.view = null
    }

    override fun onActivityCreated(m3u8Link: String?, savedPlaybackPosition: Long, savedPlayWhenReady: Boolean) {
        currentM3u8Link = m3u8Link
        playbackPosition = savedPlaybackPosition
        playWhenReady = savedPlayWhenReady

        if (currentM3u8Link == null) {
            view?.showPlayerError("No video link provided.")
            view?.popView()
            return
        }
    }

    override fun onStart() {
        view?.enterFullscreenMode()
        initializeActualPlayer()
    }

    override fun onResume() {
        view?.enterFullscreenMode()
        if (exoPlayer == null) {
            initializeActualPlayer()
        } else {
            exoPlayer?.playWhenReady = playWhenReady
        }
    }

    override fun onPause(currentPosition: Long, currentPlayWhenReady: Boolean) {
        this.playbackPosition = currentPosition
        this.playWhenReady = currentPlayWhenReady
        exoPlayer?.playWhenReady = false
    }

    override fun onStop() {
        releaseActualPlayer()
        view?.exitFullscreenMode()
    }

    private fun initializeActualPlayer() {
        if (exoPlayer != null) {
            if (!isPlayerPrepared) setupPlayerAndMediaSource()
            return
        }
        if (currentM3u8Link == null) {
            Log.e("WatchMoviePresenter", "Attempted to initialize player with null M3U8 link.")
            view?.showPlayerError("Video link is missing.")
            view?.popView()
            return
        }

        try {
            exoPlayer = ExoPlayer.Builder(contextProvider())
                .build()
                .also { player ->
                    view?.initializePlayerView(player)
                    setupPlayerAndMediaSource(player)
                }
        } catch (e: Exception) {
            Log.e("WatchMoviePresenter", "Error initializing ExoPlayer", e)
            onPlayerError("Could not initialize video player.")
        }
    }

    private fun setupPlayerAndMediaSource(player: ExoPlayer? = exoPlayer) {
        player?.let { activePlayer ->
            try {
                val dataSourceFactory = DefaultHttpDataSource.Factory()
                val hlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(currentM3u8Link!!)))

                activePlayer.setMediaSource(hlsMediaSource)
                activePlayer.playWhenReady = playWhenReady
                activePlayer.seekTo(playbackPosition)
                activePlayer.addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        super.onPlayerError(error)
//                        this.onPlayerError(error.message ?: "Unknown player error")
                        isPlayerPrepared = false
                    }

                    fun onPlaybackStateChanged(playbackState: Int, playWhenReady: Boolean) {
                        super.onPlaybackStateChanged(playbackState)
                        if (playbackState == Player.STATE_READY) isPlayerPrepared = true
                        this.onPlaybackStateChanged(playbackState, activePlayer.playWhenReady)
                    }
                })
                activePlayer.prepare()
                isPlayerPrepared = false
            } catch (e: Exception) {
                Log.e("WatchMoviePresenter", "Error setting up media source or preparing player", e)
                onPlayerError("Could not load video: ${e.localizedMessage}")
                isPlayerPrepared = false
            }
        }
    }

    private fun releaseActualPlayer() {
        exoPlayer?.let {

            this.playbackPosition = it.currentPosition
            this.playWhenReady = it.playWhenReady
            it.release()
            exoPlayer = null
            isPlayerPrepared = false
        }
        view?.releasePlayerView()
    }

    override fun onSaveInstanceStateRequested(): Bundle {
        val outState = Bundle()
        outState.putLong("playbackPosition", exoPlayer?.currentPosition ?: playbackPosition)
        outState.putBoolean("playWhenReady", exoPlayer?.playWhenReady ?: playWhenReady)
        return outState
    }

    override fun onPlayerError(errorMessage: String) {
        view?.showPlayerError(errorMessage)
    }

    override fun onPlaybackStateChanged(playbackState: Int, playWhenReady: Boolean) {

        if (playbackState == Player.STATE_ENDED) {
        }
    }

    override fun observeMembers(roomId: String) {
        if (isListening && observingRoomId == roomId) {
            // đã attach rồi, bỏ qua
            return
        }
        observingRoomId = roomId
        isListening = true

        memberRepository.listenMemberChanged(roomId){ result ->
            when(result){
                is MemberListener.OnError -> view?.showPlayerError(result.message)
                is MemberListener.OnJoin<Member> -> view?.showAddedMember(result.data.memberName)
                is MemberListener.OnLeave<Member> -> view?.showLeftMember(result.data.memberName)
                is MemberListener.onListChanged<Member> -> {
                    cachedMembers.clear()
                    cachedMembers.addAll(result.data)
                    view?.updateMemberList(result.data)
                }
            }
        }
    }

    override fun getCachedMembers(): List<Member> = cachedMembers

    override fun removeChoosenMember(
        roomId: String,
        member: Member
    ) {
        memberRepository.removeMember(roomId, member.memberId) { result ->
            when (result) {
                is NetworkResult.OnError -> view?.showPlayerError(result.message)
                is NetworkResult.OnSuccess<Unit> -> {
                    view?.showLeftMember("Removed member: ${member.memberName}")
                }
            }
        }
    }

    override fun updateRoomId(roomId: String) {
        this.roomId = roomId
        RoomSession.updateRoomId(roomId)
    }

    override fun removeMemberListener(roomId: String) {
        memberRepository.removeChildEventListener(roomId)
        memberRepository.removeValueEventListener(roomId)

        isListening = false
        observingRoomId = null
    }
}
