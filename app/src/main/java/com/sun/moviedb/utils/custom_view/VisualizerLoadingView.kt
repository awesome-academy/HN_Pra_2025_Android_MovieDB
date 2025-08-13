package com.sun.moviedb.utils.custom_view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.setMargins

class VisualizerLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val animators = mutableListOf<ValueAnimator>()
    private val bars = mutableListOf<View>()

    private val colors: List<Int>
        get() = listOf(
            0xFF9C27B0.toInt(),
            0xFF03A9F4.toInt(),
            0xFF009688.toInt(),
            0xFFFFEB3B.toInt(),
            0xFFF44336.toInt()
        )

    init {
        orientation = HORIZONTAL
        createBars()
    }

    private fun createBars() {
        val barWidth = dpToPx(4)
        val barHeight = dpToPx(24)
        val barMargin = dpToPx(4)
        val cornerRadius = dpToPx(3).toFloat()

        colors.forEach { color ->
            val bar = View(context).apply {
                val params = LayoutParams(barWidth, barHeight)
                params.setMargins(barMargin)
                layoutParams = params
                background = GradientDrawable().apply {
                    setColor(color)
                    this.cornerRadius = cornerRadius
                }
                pivotY = barHeight / 2f
            }
            addView(bar)
            bars.add(bar)
        }
    }

    private fun startAnimation() {
        if (animators.isNotEmpty()) return
        bars.forEachIndexed { index, bar ->
            val animator = ValueAnimator.ofFloat(1f, 0.4f, 1f).apply {
                duration = 600
                repeatCount = ValueAnimator.INFINITE
                startDelay = index * 100L
                addUpdateListener {
                    bar.scaleY = it.animatedValue as Float
                }
            }
            animator.start()
            animators.add(animator)
        }
    }

    private fun stopAnimation() {
        animators.forEach { it.cancel() }
        animators.clear()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE) startAnimation() else stopAnimation()
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}
