package com.cuneytayyildiz.githubrepositoryexplorer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "stargazers"
)
data class StargazerModel(
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") var id: Int = 0,
    @ColumnInfo(name = "login") @SerializedName("login") var login: String? = null,
    @ColumnInfo(name = "avatar_url") @SerializedName("avatar_url") var avatarUrl: String? = null,
    @ColumnInfo(name = "repository_name") var repositoryName: String? = null
)