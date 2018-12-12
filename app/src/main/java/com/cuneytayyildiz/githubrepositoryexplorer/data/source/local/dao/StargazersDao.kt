package com.cuneytayyildiz.githubrepositoryexplorer.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cuneytayyildiz.githubrepositoryexplorer.data.StargazerModel


@Dao
interface StargazersDao {
    @Query("SELECT * FROM stargazers WHERE repository_name LIKE :repositoryName")
    fun getAllStargazers(repositoryName: String): LiveData<MutableList<StargazerModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStargazers(stargazers: List<StargazerModel>)

    @Query("DELETE FROM repositories")
    fun deleteAllStargazers()


}