package com.sun.moviedb.screen.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sun.moviedb.R
import com.sun.moviedb.data.model.NotificationModel
import com.sun.moviedb.data.model.NotificationType
import com.sun.moviedb.databinding.ViewholderNotificationItemBinding
import com.sun.moviedb.utils.TimeUtils

class NotificationAdapter(
    private val items: MutableList<NotificationModel>,
    private val onClicked: (String, String) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)

        when (viewType) {
            VIEW_TYPE_INVITE -> {
                val binding = ViewholderNotificationItemBinding.inflate(inflater, parent, false)
                return InviteViewHolder(binding)
            }

            VIEW_TYPE_SYSTEM -> {
                val binding = ViewholderNotificationItemBinding.inflate(inflater, parent, false)
                return SystemViewHolder(binding)
            }

            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is InviteViewHolder -> if (item is NotificationModel.Invite) holder.bind(item)
            is SystemViewHolder -> if (item is NotificationModel.System) holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            NotificationType.INVITE -> VIEW_TYPE_INVITE
            else -> VIEW_TYPE_SYSTEM
        }
    }

    override fun getItemCount(): Int = items.size


    inner class InviteViewHolder(private val binding: ViewholderNotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationModel.Invite) {
            binding.tvTitleItem.text = item.title
            binding.tvBody.text = item.body
            binding.tvDuration.text = TimeUtils.getTimeAgo(item.createAt)
            Glide.with(context)
                .load(R.drawable.icons8_movie_48)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(binding.imgIconItem)
            if (item.senderAvatar.isNotEmpty())
                Glide.with(context)
                    .load(item.senderAvatar)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imgNotiItem)


            // Show/hide red dot based on isRead
            binding.imgRedDot.visibility = if (item.isRead) View.GONE else View.VISIBLE

            binding.root.setOnClickListener {
                onClicked(item.roomId, item.movieSlug)
                item.isRead = true
                val pos = bindingAdapterPosition
                notifyItemChanged(pos)
            }
        }
    }

    inner class SystemViewHolder(private val binding: ViewholderNotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationModel.System) {
            binding.tvTitleItem.text = item.title
            binding.tvBody.text = item.body
            binding.tvDuration.text = TimeUtils.getTimeAgo(item.createAt)
            Glide.with(context)
                .load(R.drawable.icons8_speaker_48)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(binding.imgIconItem)
            Glide.with(context)
                .load(R.drawable.food)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(binding.imgNotiItem)

            // Show/hide red dot based on isRead
            binding.imgRedDot.visibility = if (item.isRead) View.GONE else View.VISIBLE

            binding.root.setOnClickListener {
                onClicked("", "")
                item.isRead = true
                val pos = bindingAdapterPosition
                notifyItemChanged(pos)
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_INVITE = 0
        private const val VIEW_TYPE_SYSTEM = 1
    }
}

