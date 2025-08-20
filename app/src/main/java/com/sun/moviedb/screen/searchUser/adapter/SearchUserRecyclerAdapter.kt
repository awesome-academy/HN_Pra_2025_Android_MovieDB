package com.sun.moviedb.screen.searchUser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sun.moviedb.R // Keep for placeholder/error drawables
import com.sun.moviedb.data.model.User
import com.sun.moviedb.databinding.CustomUserRecyclerItemBinding // Import generated binding class

class SearchUserRecyclerAdapter(
    private val onItemClicked: (User) -> Unit
) : ListAdapter<User, SearchUserRecyclerAdapter.ViewHolder>(UserDiffCallback()) {
    inner class ViewHolder(
        private val binding: CustomUserRecyclerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {

                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked(getItem(position))
                }
            }
        }

        fun bind(user: User) {
            binding.textViewUsername.text = user.username
            if (user.profileImageUrl != null) {
                Glide.with(binding.root.context)
                    .load(user.profileImageUrl)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .circleCrop()
                    .into(binding.imageViewUserProfile)
            } else {

                binding.imageViewUserProfile.setImageResource(R.mipmap.ic_launcher_round)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = CustomUserRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}
