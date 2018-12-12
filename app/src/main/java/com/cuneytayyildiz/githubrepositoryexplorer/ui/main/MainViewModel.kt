package com.cuneytayyildiz.githubrepositoryexplorer.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cuneytayyildiz.githubrepositoryexplorer.data.RepositoryModel
import com.cuneytayyildiz.githubrepositoryexplorer.data.repository.GithubRepository
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.vo.Resource
import javax.inject.Inject

class MainViewModel @Inject constructor(repository: GithubRepository) : ViewModel() {

    private val currentPage: MutableLiveData<Int> = MutableLiveData()

    val repos: LiveData<Resource<MutableList<RepositoryModel>>> = Transformations.switchMap(currentPage) {
        repository.listRepos("square", it)
    }

    fun setCurrentPage(page: Int) {
        this.currentPage.value = page
    }
}