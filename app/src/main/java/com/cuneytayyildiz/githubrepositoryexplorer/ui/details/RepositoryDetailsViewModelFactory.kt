package com.cuneytayyildiz.githubrepositoryexplorer.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cuneytayyildiz.githubrepositoryexplorer.data.repository.GithubRepository



class RepositoryDetailsViewModelFactory(private val repository: GithubRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepositoryDetailsViewModel::class.java)) {
            return RepositoryDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
