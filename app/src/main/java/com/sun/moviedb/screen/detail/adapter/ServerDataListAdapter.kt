package com.sun.moviedb.screen.detail.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.logger.Logger
import com.sun.moviedb.data.model.ServerData
import com.sun.moviedb.databinding.ViewholderEpsItemBinding

class ServerDataListAdapter(
    private val items: List<ServerData>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ServerDataListAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServerDataListAdapter.ViewHolder {
        context = parent.context
        val binding = ViewholderEpsItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServerDataListAdapter.ViewHolder, position: Int) {
        val item = items[position]
        val number = extractString(item.name)
        if (number != null){
            holder.binding.tvEpsItem.text = number.toString()
        }
        else {
            holder.binding.tvEpsItem.text = item.name
        }
        holder.binding.tvEpsItem.setOnClickListener {
            onClick(item.linkM3u8)
        }
    }

    override fun getItemCount(): Int = items.size

    private fun extractString (input: String) : Int?{
        val numberString = input.filter { it.isDigit() }
        return numberString.toIntOrNull()
    }

    inner class ViewHolder(val binding: ViewholderEpsItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

