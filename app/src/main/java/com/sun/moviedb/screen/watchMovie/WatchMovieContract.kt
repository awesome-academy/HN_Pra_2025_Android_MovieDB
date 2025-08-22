package com.sun.moviedb.screen.watchMovie

import android.os.Bundle
import androidx.media3.common.Player
import com.sun.moviedb.data.model.Member

interface WatchMovieContract {

    interface View {
        fun initializePlayerView(player: Player)
        fun releasePlayerView()
        fun showPlayerError(message: String)
        fun enterFullscreenMode()
        fun exitFullscreenMode()
        fun getInitialPlaybackPosition(): Long
        fun getInitialPlayWhenReady(): Boolean
        fun setOriginalOrientation(orientation: Int)
        fun getOriginalOrientation(): Int
        fun popView()

        fun showAddedMember(memberName: String)
        fun showLeftMember(memberName: String)
        fun updateMemberList(members: List<Member>)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun onActivityCreated(m3u8Link: String?, savedPlaybackPosition: Long, savedPlayWhenReady: Boolean)
        fun onStart()
        fun onResume()
        fun onPause(currentPosition: Long, currentPlayWhenReady: Boolean)
        fun onStop()
        fun onSaveInstanceStateRequested(): Bundle
        fun onPlayerError(errorMessage: String)
        fun onPlaybackStateChanged(playbackState: Int, playWhenReady: Boolean)
        fun observeMembers(roomId: String)
        fun getCachedMembers(): List<Member>
        fun removeChoosenMember(roomId: String, member: Member)
        fun updateRoomId(roomId: String)

        fun removeMemberListener(roomId: String)
    }
}
