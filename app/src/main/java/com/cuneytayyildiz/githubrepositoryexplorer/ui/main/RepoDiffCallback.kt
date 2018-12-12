package com.cuneytayyildiz.githubrepositoryexplorer.ui.main

import androidx.recyclerview.widget.DiffUtil
import com.cuneytayyildiz.githubrepositoryexplorer.data.RepositoryModel

  class RepoDiffCallback internal constructor(
    private val mOldList: List<RepositoryModel>?,
    private val mNewList: List<RepositoryModel>?
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList!![oldItemPosition].name == mNewList!![newItemPosition].name && mOldList[oldItemPosition].isBookmarked == mNewList[newItemPosition].isBookmarked
    }

    override fun getOldListSize(): Int {
        return mOldList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return mNewList?.size ?: 0
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldRepo = mOldList!![oldItemPosition]
        val newRepo = mNewList!![newItemPosition]
        return oldRepo == newRepo
    }
}