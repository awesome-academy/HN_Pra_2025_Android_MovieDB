package com.sun.moviedb.screen.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sun.moviedb.data.model.Item
import com.sun.moviedb.databinding.ItemHomeMovieBinding
import com.sun.moviedb.utils.base.BaseAdapter

class SeriesMovieAdapter(
    private val onMovieClick: ((Item) -> Unit)? = null,
    private val onFavouriteClick: ((Item) -> Unit)? = null
) : BaseAdapter<Item, SeriesMovieAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemHomeMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ItemHomeMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            Glide.with(binding.imgThumbnail).load(item.thumbUrl).into(binding.imgThumbnail)
            binding.tvEpisode.text = item.episodeCurrent
            binding.tvTimeRemaining.text = item.time
            binding.tvName.text = item.name
            binding.tvQuality.text = item.quality.ifEmpty { "HD" }
            binding.ivFavourite.setOnClickListener { onFavouriteClick?.invoke(item) }
            binding.root.setOnClickListener { onMovieClick?.invoke(item) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem == newItem
        }
    }
}
