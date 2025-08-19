package com.sun.moviedb.screen.watchMovie

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.Player
import com.sun.moviedb.databinding.ActivityWatchMovieBinding // Changed from Fragment binding
import com.sun.moviedb.utils.base.BaseActivity

class WatchMovieActivity : BaseActivity<ActivityWatchMovieBinding>(), WatchMovieContract.View {

    private lateinit var presenter: WatchMovieContract.Presenter

    private var m3u8Link: String? = null
    private var initialPlaybackPosition = 0L
    private var initialPlayWhenReady = true
    private var activityOriginalOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    companion object {
        private const val ARG_M3U8_LINK = "m3u8_link"
        private const val SAVED_PLAYBACK_POSITION = "playbackPosition"
        private const val SAVED_PLAY_WHEN_READY = "playWhenReady"

        fun newIntent(context: Context, m3u8Link: String): Intent {
            val intent = Intent(context, WatchMovieActivity::class.java)
            intent.putExtra(ARG_M3U8_LINK, m3u8Link)
            return intent
        }
    }

    override fun getViewBinding(): ActivityWatchMovieBinding {
        return ActivityWatchMovieBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        presenter = WatchMoviePresenterImpl { this }
        presenter.attachView(this)

        m3u8Link = intent.getStringExtra(ARG_M3U8_LINK)
//        m3u8Link = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8"

        if (intent.extras != null && intent.extras!!.containsKey(SAVED_PLAYBACK_POSITION)) {
            initialPlaybackPosition = intent.extras!!.getLong(SAVED_PLAYBACK_POSITION, 0L)
            initialPlayWhenReady = intent.extras!!.getBoolean(SAVED_PLAY_WHEN_READY, true)
        }
        activityOriginalOrientation = requestedOrientation

        presenter.onActivityCreated(m3u8Link, initialPlaybackPosition, initialPlayWhenReady)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val stateBundle = presenter.onSaveInstanceStateRequested()
        outState.putLong(SAVED_PLAYBACK_POSITION, stateBundle.getLong(SAVED_PLAYBACK_POSITION, initialPlaybackPosition))
        outState.putBoolean(SAVED_PLAY_WHEN_READY, stateBundle.getBoolean(SAVED_PLAY_WHEN_READY, initialPlayWhenReady))
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        initialPlaybackPosition = savedInstanceState.getLong(SAVED_PLAYBACK_POSITION, 0L)
        initialPlayWhenReady = savedInstanceState.getBoolean(SAVED_PLAY_WHEN_READY, true)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        val currentPosition = binding.playerView.player?.currentPosition ?: initialPlaybackPosition
        val currentPlayWhenReady = binding.playerView.player?.playWhenReady ?: initialPlayWhenReady
        presenter.onPause(currentPosition, currentPlayWhenReady)
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun initializePlayerView(player: Player) {
        binding.playerView.player = player
    }

    override fun releasePlayerView() {
        binding.playerView.player?.release()
        binding.playerView.player = null
    }

    override fun showPlayerError(message: String) {
        Toast.makeText(this, "Player Error: $message", Toast.LENGTH_LONG).show()
    }

    override fun enterFullscreenMode() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.playerView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        supportActionBar?.hide()
    }

    override fun exitFullscreenMode() {
        requestedOrientation = activityOriginalOrientation
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, binding.playerView)
            .show(WindowInsetsCompat.Type.systemBars())
        supportActionBar?.show()
    }

    override fun getInitialPlaybackPosition(): Long = initialPlaybackPosition
    override fun getInitialPlayWhenReady(): Boolean = initialPlayWhenReady

    override fun setOriginalOrientation(orientation: Int) {
        this.activityOriginalOrientation = orientation
    }

    override fun getOriginalOrientation(): Int = this.activityOriginalOrientation

    override fun popView() {
        finish()
    }
}

