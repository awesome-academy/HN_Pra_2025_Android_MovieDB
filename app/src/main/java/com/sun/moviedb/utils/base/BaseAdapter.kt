package com.sun.moviedb.utils.base

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    private val diffCallback: DiffUtil.ItemCallback<T>
) : RecyclerView.Adapter<VH>() {

    private val differ = AsyncListDiffer(this, diffCallback)

    protected val items: List<T> get() = differ.currentList

    override fun getItemCount(): Int = items.size

    fun submitList(newList: List<T>) {
        differ.submitList(newList)
    }

    fun getItem(position: Int): T = items[position]
}
