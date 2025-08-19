package com.sun.moviedb.screen.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.sun.moviedb.data.model.Episode
import com.sun.moviedb.data.model.ServerData
import com.sun.moviedb.databinding.ItemSeriesTabBinding
import com.sun.moviedb.screen.home.adapter.SeriesTabAdapter

class EpsListAdapter(
    private val item: List<Episode>,
    private val onClick: (List<ServerData>) -> Unit,
) : RecyclerView.Adapter<EpsListAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var selectedPos = RecyclerView.NO_POSITION

    inner class ViewHolder(val binding: ItemSeriesTabBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val currentPos = bindingAdapterPosition
                val previousPos = selectedPos
                selectedPos = currentPos

                if (currentPos != RecyclerView.NO_POSITION) {
                    /* *
                    * Notify to update the button state
                    * */
                    notifyItemChanged(previousPos)
                    notifyItemChanged(selectedPos)

                    onClick(item[currentPos].serverData)
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpsListAdapter.ViewHolder {
        this.context = parent.context
        val binding = ItemSeriesTabBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpsListAdapter.ViewHolder, position: Int) {
        val item = item[position]
        holder.binding.tvTabTitle.text = item.serverName
        holder.binding.tvTabTitle.isSelected = position == selectedPos
    }

    override fun getItemCount(): Int = item.size

    /* *
    * trigger to select the first item in the list
    * */
    fun selectFirstItem() {
        if (item.isNotEmpty()) {
            selectedPos = 0
            notifyItemChanged(selectedPos)
            onClick(item[selectedPos].serverData)
        }
    }

}

