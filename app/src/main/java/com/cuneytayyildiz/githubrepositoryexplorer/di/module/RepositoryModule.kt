package com.cuneytayyildiz.githubrepositoryexplorer.di.module

import com.cuneytayyildiz.githubrepositoryexplorer.utils.AppExecutors
import com.cuneytayyildiz.githubrepositoryexplorer.data.repository.GithubRepository
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.local.GithubDb
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.remote.GithubService
import com.cuneytayyildiz.githubrepositoryexplorer.ui.details.RepositoryDetailsViewModelFactory
import com.cuneytayyildiz.githubrepositoryexplorer.ui.main.MainViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepoRepository(
        appExecutors: AppExecutors,
        db: GithubDb,
        githubService: GithubService
    ): GithubRepository {
        return GithubRepository(appExecutors, githubService, db)
    }


    @Provides
    @Singleton
    fun provideMainViewModelFactory(repoRepository: GithubRepository) = MainViewModelFactory(repoRepository)

    @Provides
    @Singleton
    fun provideRepositoryDetailsViewModelFactory(repoRepository: GithubRepository) = RepositoryDetailsViewModelFactory(repoRepository)
}