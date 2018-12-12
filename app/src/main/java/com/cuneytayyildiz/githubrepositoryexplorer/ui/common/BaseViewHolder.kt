package com.cuneytayyildiz.githubrepositoryexplorer.ui.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<in Model>(itemView : View) : RecyclerView.ViewHolder(itemView) {
    internal abstract fun bind(model: Model)
}