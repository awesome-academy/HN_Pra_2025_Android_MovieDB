package com.sun.moviedb.screen.room.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sun.moviedb.data.model.Member
import com.sun.moviedb.databinding.ViewholderMemberItemBinding

class RoomAdapter(
    private val items: MutableList<Member>,
    private val onRemoveMember: (String) -> Unit
) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RoomAdapter.ViewHolder {
        context = parent.context
        val binding =
            ViewholderMemberItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            tvMemberName.text = item.userName
        }
        if (item.linkAvatar.isNotEmpty()) {
            Glide.with(context)
                .load(item.linkAvatar)
                .into(holder.binding.imgAvtMember)
        }
        holder.binding.btnRemoveMember.setOnClickListener {
            onRemoveMember(item.userId)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: ViewholderMemberItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}