package com.cuneytayyildiz.githubrepositoryexplorer.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "repositories"
)
data class RepositoryModel(
    @PrimaryKey @ColumnInfo(name = "name") @SerializedName("name") var name: String = "",
    @ColumnInfo(name = "stargazers_count") @SerializedName("stargazers_count") var stargazersCount: Int = 0,
    @ColumnInfo(name = "is_bookmarked") var isBookmarked: Boolean
) : Parcelable