package com.cuneytayyildiz.githubrepositoryexplorer.di.module

import android.app.Application
import androidx.room.Room
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.local.GithubDb
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.local.dao.RepositoryDao
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.local.dao.StargazersDao
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton


@Module
class AppModule(val app: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideDatabase(app: Application): GithubDb =
        Room.databaseBuilder(app, GithubDb::class.java, GithubDb.NAME).build()

    @Provides
    @Singleton
    fun provideRepositoryDao(database: GithubDb): RepositoryDao = database.repositoryDao()

    @Provides
    @Singleton
    fun provideStargazersDao(database: GithubDb): StargazersDao = database.stargazersDao()


    @Provides
    @Singleton
    fun provideAppExecutors(): Executor {
        return Executors.newSingleThreadExecutor()
    }


}