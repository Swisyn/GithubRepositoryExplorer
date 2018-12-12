package com.cuneytayyildiz.githubrepositoryexplorer.ui.details

import androidx.recyclerview.widget.DiffUtil
import com.cuneytayyildiz.githubrepositoryexplorer.data.StargazerModel

    class StargazersDiffCallback internal constructor(
    private val mOldList: List<StargazerModel>?,
    private val mNewList: List<StargazerModel>?
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList!![oldItemPosition].id == mNewList!![newItemPosition].id &&
                mOldList[oldItemPosition].avatarUrl == mNewList[newItemPosition].avatarUrl &&
                mOldList[oldItemPosition].repositoryName == mNewList[newItemPosition].repositoryName   &&
                mOldList[oldItemPosition].login == mNewList[newItemPosition].login
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