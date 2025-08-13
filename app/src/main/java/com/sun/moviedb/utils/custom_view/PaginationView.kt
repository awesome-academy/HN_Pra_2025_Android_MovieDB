package com.sun.moviedb.utils.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sun.moviedb.R

class PaginationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var currentPage = 1
    private var totalPages = 1
    private var listener: ((Int) -> Unit)? = null

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
    }

    fun setup(totalPages: Int, currentPage: Int = 1, listener: (Int) -> Unit) {
        this.totalPages = totalPages
        this.currentPage = currentPage
        this.listener = listener
        renderPagination()
    }

    private fun renderPagination() {
        removeAllViews()

        val pages = getPaginationList(currentPage, totalPages)

        // Prev button
        addPageButton("<") {
            if (currentPage > 1) {
                currentPage--
                renderPagination()
                listener?.invoke(currentPage)
            }
        }

        // Page numbers + dots
        for (page in pages) {
            if (page == "...") {
                addDots()
            } else {
                val pageNum = page.toInt()
                addPageButton(pageNum.toString(), isSelected = (pageNum == currentPage)) {
                    currentPage = pageNum
                    renderPagination()
                    listener?.invoke(currentPage)
                }
            }
        }

        // Next button
        addPageButton(">") {
            if (currentPage < totalPages) {
                currentPage++
                renderPagination()
                listener?.invoke(currentPage)
            }
        }
    }

    private fun addPageButton(text: String, isSelected: Boolean = false, onClick: () -> Unit) {
        val tv = TextView(context).apply {
            this.text = text
            setPadding(24, 12, 24, 12)
            background = if (isSelected)
                ContextCompat.getDrawable(context, R.drawable.bg_page_selected)
            else
                ContextCompat.getDrawable(context, R.drawable.bg_page_normal)

            setTextColor(
                ContextCompat.getColor(
                    context,
                    if (isSelected) R.color.deep_purple else android.R.color.white
                )
            )

            textSize = 16f
            gravity = Gravity.CENTER

            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.marginStart = 5
            params.marginEnd = 5
            layoutParams = params

            setOnClickListener { onClick() }
        }
        addView(tv)
    }


    private fun addDots() {
        val tv = TextView(context).apply {
            text = "..."
            setPadding(16, 12, 16, 12)
        }
        addView(tv)
    }

    private fun getPaginationList(currentPage: Int, totalPages: Int): List<String> {
        val pages = mutableListOf<String>()

        pages.add("1")

        if (currentPage > 3) pages.add("...")

        val start = maxOf(2, currentPage - 1)
        val end = minOf(totalPages - 1, currentPage + 1)

        for (i in start..end) {
            pages.add(i.toString())
        }

        if (currentPage < totalPages - 2) pages.add("...")

        if (totalPages > 1) pages.add(totalPages.toString())

        return pages
    }
}
