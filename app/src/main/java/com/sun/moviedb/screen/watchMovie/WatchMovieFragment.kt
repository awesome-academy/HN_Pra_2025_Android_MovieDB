package com.sun.moviedb.screen.watchMovie

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.media3.common.Player
import com.sun.moviedb.databinding.FragmentWatchMovieBinding

class WatchMovieFragment : Fragment(), WatchMovieContract.View {

    private var _binding: FragmentWatchMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: WatchMovieContract.Presenter

    private var m3u8Link: String? = null
    private var initialPlaybackPosition = 0L
    private var initialPlayWhenReady = true
    private var fragmentOriginalOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    companion object {
        private const val ARG_M3U8_LINK = "m3u8_link"

        fun newInstance(m3u8Link: String): WatchMovieFragment {
            val fragment = WatchMovieFragment()
            val args = Bundle()
            args.putString(ARG_M3U8_LINK, m3u8Link)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter = WatchMoviePresenterImpl { requireContext() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)

        arguments?.let {
            m3u8Link = it.getString(ARG_M3U8_LINK)
        }

        if (savedInstanceState != null) {
            initialPlaybackPosition = savedInstanceState.getLong("playbackPosition", 0L)
            initialPlayWhenReady = savedInstanceState.getBoolean("playWhenReady", true)
        }
//        presenter.setOriginalOrientation(requireActivity().requestedOrientation)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onFragmentViewCreated(m3u8Link, initialPlaybackPosition, initialPlayWhenReady)
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

    override fun onSaveInstanceState(outState: Bundle) {
        val stateBundle = presenter.onSaveInstanceStateRequested()
        outState.putLong("playbackPosition", stateBundle.getLong("playbackPosition", initialPlaybackPosition))
        outState.putBoolean("playWhenReady", stateBundle.getBoolean("playWhenReady", initialPlayWhenReady))
        super.onSaveInstanceState(outState)
    }

    override fun initializePlayerView(player: Player) {
        binding.playerView.player = player
    }

    override fun releasePlayerView() {
        binding.playerView.player = null
    }

    override fun showPlayerError(message: String) {
        Toast.makeText(context, "Player Error: $message", Toast.LENGTH_LONG).show()
    }

    override fun enterFullscreenMode() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        activity?.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, binding.playerView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    override fun exitFullscreenMode() {
//        requireActivity().requestedOrientation = presenter.getOriginalOrientation()
        activity?.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(window, binding.playerView)
                .show(WindowInsetsCompat.Type.systemBars())
        }
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    override fun getInitialPlaybackPosition(): Long = initialPlaybackPosition
    override fun getInitialPlayWhenReady(): Boolean = initialPlayWhenReady

    override fun setOriginalOrientation(orientation: Int) {
        this.fragmentOriginalOrientation = orientation
    }

    override fun getOriginalOrientation(): Int = this.fragmentOriginalOrientation

    override fun popView() {
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
