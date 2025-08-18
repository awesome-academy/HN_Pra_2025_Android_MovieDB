package com.sun.moviedb.screen.searchUser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sun.moviedb.R
import com.sun.moviedb.data.model.User

class SearchUserRecyclerAdapter(
    private val onItemClicked: (User) -> Unit
) : ListAdapter<User, SearchUserRecyclerAdapter.ViewHolder>(UserDiffCallback()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userProfileImageView: ImageView = itemView.findViewById(R.id.imageViewUserProfile)
        val usernameTextView: TextView = itemView.findViewById(R.id.textViewUsername)

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClicked(getItem(bindingAdapterPosition))
                }
            }
        }

        fun bind(user: User) {
            usernameTextView.text = user.username
            if (user.profileImageUrl != null) {
                Glide.with(itemView.context)
                    .load(user.profileImageUrl)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .circleCrop()
                    .into(userProfileImageView)
            } else {
                userProfileImageView.setImageResource(R.mipmap.ic_launcher_round)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_user_recycler_item, parent, false)
        return ViewHolder(view)
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
