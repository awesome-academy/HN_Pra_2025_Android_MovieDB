package com.sun.moviedb.screen.watchMovie

import android.os.Bundle
import androidx.media3.common.Player

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
    }
}
