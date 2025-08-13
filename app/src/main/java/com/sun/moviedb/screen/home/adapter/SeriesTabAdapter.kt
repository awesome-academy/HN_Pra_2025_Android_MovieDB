package com.sun.moviedb.screen.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sun.moviedb.R

class SeriesTabAdapter(
    private val onTabSelected: ((String) -> Unit)? = null
) : ListAdapter<String, SeriesTabAdapter.SeriesTabViewHolder>(DIFF_CALLBACK) {

    private var selectedIndex: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesTabViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_series_tab, parent, false) as ViewGroup
        return SeriesTabViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeriesTabViewHolder, position: Int) {
        val title = getItem(position)
        holder.bind(title, position == selectedIndex)
        holder.itemView.setOnClickListener {
            updateSelection(position)
            onTabSelected?.invoke(title)
        }
    }

    fun setSelectedTab(tab: String) {
        val newIndex = currentList.indexOf(tab)
        if (newIndex != -1) {
            updateSelection(newIndex)
        }
    }

    private fun updateSelection(newIndex: Int) {
        if (selectedIndex == newIndex) return

        val oldIndex = selectedIndex
        selectedIndex = newIndex

        if (oldIndex != RecyclerView.NO_POSITION) notifyItemChanged(oldIndex)
        notifyItemChanged(newIndex)
    }

    inner class SeriesTabViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        private val tvTabTitle: TextView = itemView.findViewById(R.id.tvTabTitle)
        fun bind(title: String, selected: Boolean) {
            tvTabTitle.text = title
            tvTabTitle.isSelected = selected
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        }
    }
}
