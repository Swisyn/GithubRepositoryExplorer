package com.cuneytayyildiz.githubrepositoryexplorer.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cuneytayyildiz.githubrepositoryexplorer.data.RepositoryModel

@Dao
interface RepositoryDao {

    @Query("SELECT * FROM repositories")
    fun getAllRepos(): LiveData<MutableList<RepositoryModel>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repositories: List<RepositoryModel>)

    @Query("DELETE FROM repositories")
    fun deleteRepos()

    @Query("UPDATE repositories SET is_bookmarked = NOT is_bookmarked WHERE name = :repositoryName")
    fun toggleFavouriteRepository(repositoryName: String?)

    @Query("SELECT Count(*) FROM repositories WHERE name LIKE :repositoryName AND is_bookmarked = 1")
    fun isBookmarked(repositoryName: String): Boolean

}