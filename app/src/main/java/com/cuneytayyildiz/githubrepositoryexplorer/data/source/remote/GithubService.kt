package com.cuneytayyildiz.githubrepositoryexplorer.data.source.remote

import androidx.lifecycle.LiveData
import com.cuneytayyildiz.githubrepositoryexplorer.data.RepositoryModel
import com.cuneytayyildiz.githubrepositoryexplorer.data.StargazerModel
import com.cuneytayyildiz.githubrepositoryexplorer.utils.ITEMS_PER_PAGE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("orgs/{owner}/repos")
    fun listRepos(@Path("owner") owner: String, @Query("page") page: Int, @Query("per_page") perPage: Int = ITEMS_PER_PAGE): LiveData<ApiResponse<MutableList<RepositoryModel>>>

    @GET("repos/{owner}/{repositoryName}/stargazers")
    fun listStargazers(
        @Path("owner") owner: String,
        @Path("repositoryName") repositoryName: String,
        @Query("page") page: Int, @Query("per_page") perPage: Int = ITEMS_PER_PAGE
    ): LiveData<ApiResponse<MutableList<StargazerModel>>>

}