package com.cuneytayyildiz.githubrepositoryexplorer.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cuneytayyildiz.githubrepositoryexplorer.data.RepositoryModel
import com.cuneytayyildiz.githubrepositoryexplorer.data.StargazerModel
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.local.dao.RepositoryDao
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.local.dao.StargazersDao

@Database(
        entities = [RepositoryModel::class, StargazerModel::class],
        version = 1,
        exportSchema = false
)
abstract class GithubDb : RoomDatabase() {

    companion object {
        const val NAME = "github_repos.db"
    }

    abstract fun repositoryDao(): RepositoryDao

    abstract fun stargazersDao(): StargazersDao

}