package com.cuneytayyildiz.githubrepositoryexplorer.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cuneytayyildiz.githubrepositoryexplorer.data.repository.GithubRepository
import javax.inject.Inject

class RepositoryDetailsViewModel @Inject constructor(private val repository: GithubRepository) : ViewModel() {

    private val currentPage: MutableLiveData<Int> = MutableLiveData()
    private val repositoryName: MutableLiveData<String> = MutableLiveData()

    val stargazers = Transformations.switchMap(currentPage) {
        repositoryName.value?.let { repositoryName -> repository.listStargazers("square", repositoryName, it) }
    }

    fun setCurrentPage(page: Int) {
        this.currentPage.value = page
    }

    fun setRepositoryName(repoName: String) {
        repositoryName.value = repoName
    }

    fun toggleBookmark() {
        repositoryName.value?.let {
            repository.toggleFavouriteRepository(it)
        }
    }

    fun isRepositoryBookmarked(): LiveData<Boolean> {
        repositoryName.value?.let {
            return repository.isRepositoryBookmarked(it)
        }
    }
}