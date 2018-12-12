package com.cuneytayyildiz.githubrepositoryexplorer.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuneytayyildiz.githubrepositoryexplorer.R
import com.cuneytayyildiz.githubrepositoryexplorer.data.StargazerModel
import com.cuneytayyildiz.githubrepositoryexplorer.utils.load

class StargazersListAdapter(
      var items: MutableList<StargazerModel>
) :
    RecyclerView.Adapter<StargazersListAdapter.ListItemViewHolder>() {

    fun addItems(newItems: MutableList<StargazerModel>?) {
        if (newItems == null) {
            val oldSize = this.items.size
            this.items.clear()
            notifyItemRangeRemoved(0, oldSize)
        } else {
            val result = DiffUtil.calculateDiff(StargazersDiffCallback(this.items, newItems))
            this.items.clear()
            this.items.addAll(newItems)
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_stargazer, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val item = items[position]

        with(holder) {
            imageStargazerAvatar.load(item.avatarUrl)
            textStargazerName.text = item.login
        }
    }

    class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageStargazerAvatar: ImageView = itemView.findViewById(R.id.image_stargazer_avatar)
        var textStargazerName: TextView = itemView.findViewById(R.id.text_stargazer_name)
    }
}