package com.cuneytayyildiz.githubrepositoryexplorer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cuneytayyildiz.githubrepositoryexplorer.data.RepositoryModel
import com.cuneytayyildiz.githubrepositoryexplorer.data.StargazerModel
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.local.GithubDb
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.remote.ApiResponse
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.remote.GithubService
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.vo.NetworkBoundResource
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.vo.RateLimiter
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.vo.Resource
import com.cuneytayyildiz.githubrepositoryexplorer.utils.AppExecutors
import com.cuneytayyildiz.githubrepositoryexplorer.utils.doTransaction
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: GithubService,
    private val database: GithubDb

) {
    private val bookmarkLiveData = MutableLiveData<Boolean>()

    private val repoListRateLimit =
        RateLimiter<String>(10, TimeUnit.MINUTES)

    fun listRepos(
        owner: String,
        page: Int
    ): LiveData<Resource<MutableList<RepositoryModel>>> {
        val cacheKey = "$owner/$page"
        return object : NetworkBoundResource<MutableList<RepositoryModel>, MutableList<RepositoryModel>>(appExecutors) {

            override fun shouldFetch(data: MutableList<RepositoryModel>): Boolean {
                return data.isEmpty() || repoListRateLimit.shouldFetch(cacheKey)
            }

            override fun saveCallResult(items: MutableList<RepositoryModel>) {
                if (items.isNotEmpty()) {
                    database.doTransaction {
                        it.repositoryDao().insertRepos(items)
                    }
                }
            }

            override fun loadFromDb(): LiveData<MutableList<RepositoryModel>> {
                return database.repositoryDao().getAllRepos()

            }

            override fun createCall(): LiveData<ApiResponse<MutableList<RepositoryModel>>> {
                return apiService.listRepos(owner, page)
            }

            override fun onFetchFailed() {
                repoListRateLimit.reset(cacheKey)
            }
        }.asLiveData()
    }

    fun listStargazers(
        owner: String,
        repositoryName: String,
        page: Int
    ): LiveData<Resource<MutableList<StargazerModel>>> {
        val cacheKey = "$owner/$repositoryName/$page"
        return object : NetworkBoundResource<MutableList<StargazerModel>, MutableList<StargazerModel>>(appExecutors) {
            override fun saveCallResult(items: MutableList<StargazerModel>) {
                if (items.isNotEmpty()) {
                    database.doTransaction {
                        it.stargazersDao().insertStargazers(items.map { item ->
                            item.repositoryName = repositoryName
                            item
                        })
                    }
                }
            }

            override fun shouldFetch(data: MutableList<StargazerModel>): Boolean {
                return data.isEmpty() || repoListRateLimit.shouldFetch(cacheKey)
            }

            override fun loadFromDb(): LiveData<MutableList<StargazerModel>> {
                return database.stargazersDao().getAllStargazers(repositoryName)
            }

            override fun createCall(): LiveData<ApiResponse<MutableList<StargazerModel>>> {
                return apiService.listStargazers(owner, repositoryName, page)
            }

            override fun onFetchFailed() {
                repoListRateLimit.reset(cacheKey)
            }
        }.asLiveData()
    }

    fun isRepositoryBookmarked(repositoryName: String): LiveData<Boolean> {
        appExecutors.diskIO().execute {
            bookmarkLiveData.postValue(database.repositoryDao().isBookmarked(repositoryName))
        }
        return bookmarkLiveData
    }

    fun toggleFavouriteRepository(repositoryName: String) {
        appExecutors.diskIO().execute {
            database.repositoryDao().toggleFavouriteRepository(repositoryName)
            isRepositoryBookmarked(repositoryName)
        }
    }
}