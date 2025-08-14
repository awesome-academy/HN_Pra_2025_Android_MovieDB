package com.sun.moviedb.screen.filter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sun.moviedb.data.model.Item
import com.sun.moviedb.databinding.ItemFilterMovieBinding
import com.sun.moviedb.utils.base.BaseAdapter

class FilterMovieAdapter(
    private val onMovieClick: (Item) -> Unit
) : BaseAdapter<Item, FilterMovieAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemFilterMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ItemFilterMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Item) {
            binding.apply {
                tvMovieTitle.text = movie.name
                tvMovieYear.text = movie.year.toString()
                tvMovieType.text = getMovieTypeDisplayName(movie.type)

                if (movie.episodeCurrent.isNotEmpty()) {
                    tvEpisodeCurrent.text = movie.episodeCurrent
                    tvEpisodeCurrent.visibility = android.view.View.VISIBLE
                } else {
                    tvEpisodeCurrent.visibility = android.view.View.GONE
                }
                tvQuality.text = movie.quality.ifEmpty { "HD" }
                tvTime.text = movie.time.ifEmpty { "N/A" }

                Glide.with(itemView.context)
                    .load(movie.thumbUrl)
                    .into(ivMoviePoster)

                root.setOnClickListener {
                    onMovieClick(movie)
                }
            }
        }

        private fun getMovieTypeDisplayName(type: String): String {
            return when (type.lowercase()) {
                "single" -> "Phim lẻ"
                "series" -> "Phim bộ"
                "hoathinh" -> "Hoạt hình"
                else -> type.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem
        }
    }
}
