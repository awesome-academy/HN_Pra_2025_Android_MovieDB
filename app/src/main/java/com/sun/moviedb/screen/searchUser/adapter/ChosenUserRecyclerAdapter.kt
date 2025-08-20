package com.sun.moviedb.screen.searchUser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sun.moviedb.R
import com.sun.moviedb.data.model.User
import com.sun.moviedb.databinding.CustomChosenUserRecyclerItemBinding

class ChosenUserRecyclerAdapter(
    private val onItemClicked: (User) -> Unit
) : ListAdapter<User, ChosenUserRecyclerAdapter.ViewHolder>(UserDiffCallback()) {

    inner class ViewHolder(
        private val binding: CustomChosenUserRecyclerItemBinding
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
            if (user.profileImageUrl != null) {
                Glide.with(binding.root.context)
                    .load(user.profileImageUrl)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .circleCrop()
                    .into(binding.imageViewChosenUserProfile)
            } else {
                binding.imageViewChosenUserProfile.setImageResource(R.mipmap.ic_launcher_round)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = CustomChosenUserRecyclerItemBinding.inflate(
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