package com.sun.moviedb.screen.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sun.moviedb.data.model.Item
import com.sun.moviedb.databinding.ItemSearchBinding
import com.sun.moviedb.utils.base.BaseAdapter

class SearchAdapter(
    private val onItemClick: (Item) -> Unit = {}
) : BaseAdapter<Item, SearchAdapter.SearchViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            Glide.with(binding.ivPoster.context)
                .load(item.posterUrl.ifEmpty { item.thumbUrl })
                .centerCrop()
                .into(binding.ivPoster)
            if (item.quality.isNotBlank()) {
                binding.tvQuality.visibility = View.VISIBLE
                binding.tvQuality.text = item.quality
            } else {
                binding.tvQuality.visibility = View.GONE
            }
            if (item.time.isNotBlank()) {
                binding.tvTime.visibility = View.VISIBLE
                binding.tvTime.text = item.time
            } else {
                binding.tvTime.visibility = View.GONE
            }
            val country = item.country.firstOrNull()?.name ?: ""
            if (country.isNotBlank()) {
                binding.tvCountry.visibility = View.VISIBLE
                binding.tvCountry.text = country
            } else {
                binding.tvCountry.visibility = View.GONE
            }
            val year = if (item.year > 0) item.year.toString() else ""
            if (year.isNotBlank()) {
                binding.tvYear.visibility = View.VISIBLE
                binding.tvYear.text = year
            } else {
                binding.tvYear.visibility = View.GONE
            }
            if (item.type.isNotBlank()) {
                binding.tvType.visibility = View.VISIBLE
                binding.tvType.text = item.type
            } else {
                binding.tvType.visibility = View.GONE
            }
            binding.tvTitle.text = item.name.ifBlank { item.originName }
            val category = item.category.firstOrNull()?.name ?: ""
            if (category.isNotBlank()) {
                binding.tvCategory.visibility = View.VISIBLE
                binding.tvCategory.text = category
            } else {
                binding.tvCategory.visibility = View.GONE
            }
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem == newItem
        }
    }
}
