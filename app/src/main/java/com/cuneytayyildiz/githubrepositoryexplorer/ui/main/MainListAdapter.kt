package com.cuneytayyildiz.githubrepositoryexplorer.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuneytayyildiz.githubrepositoryexplorer.R
import com.cuneytayyildiz.githubrepositoryexplorer.data.RepositoryModel
import com.cuneytayyildiz.githubrepositoryexplorer.ui.common.BaseViewHolder
import com.cuneytayyildiz.githubrepositoryexplorer.utils.formatNumber


class MainListAdapter(
    var items: MutableList<RepositoryModel> = mutableListOf(),
    private val clickListener: ((RepositoryModel, Int) -> Unit?)
) : RecyclerView.Adapter<MainListAdapter.ListItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(
            clickListener,
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_repository,
                parent,
                false
            )
        )
    }

    fun addItems(newItems: MutableList<RepositoryModel>?) {
        if (newItems == null) {
            val oldSize = this.items.size
            this.items.clear()
            notifyItemRangeRemoved(0, oldSize)
        } else {
            val result = DiffUtil.calculateDiff(RepoDiffCallback(this.items, newItems))
            this.items.clear()
            this.items.addAll(newItems)

            result.dispatchUpdatesTo(this)
        }
      notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ListItemViewHolder(private val clickListener: ((RepositoryModel, Int) -> Unit?), itemView: View) :
        BaseViewHolder<RepositoryModel>(itemView) {
        override fun bind(model: RepositoryModel) {
            textRepositoryName.text = model.name
            textStargazersCount.text = model.stargazersCount.formatNumber()
            bookmarkContainer.visibility = if (model.isBookmarked) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    clickListener.invoke(model, adapterPosition)
                }
            }
        }

        private var textRepositoryName: TextView =
            itemView.findViewById(com.cuneytayyildiz.githubrepositoryexplorer.R.id.text_repository_name)
        private var textStargazersCount: TextView =
            itemView.findViewById(com.cuneytayyildiz.githubrepositoryexplorer.R.id.text_stargazers_count)
        private var bookmarkContainer: FrameLayout =
            itemView.findViewById(com.cuneytayyildiz.githubrepositoryexplorer.R.id.bookmark_container)

    }
}