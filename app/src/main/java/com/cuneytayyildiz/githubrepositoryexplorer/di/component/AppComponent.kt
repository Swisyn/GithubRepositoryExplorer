package com.cuneytayyildiz.githubrepositoryexplorer.di.component

import android.app.Application
import com.cuneytayyildiz.githubrepositoryexplorer.di.module.AppModule
import com.cuneytayyildiz.githubrepositoryexplorer.di.module.NetModule
import com.cuneytayyildiz.githubrepositoryexplorer.di.module.RepositoryModule
import com.cuneytayyildiz.githubrepositoryexplorer.ui.details.RepositoryDetailsActivity
import com.cuneytayyildiz.githubrepositoryexplorer.ui.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(app: Application)
    fun inject(mainActivity: MainActivity)
    fun inject(detailsActivity: RepositoryDetailsActivity)


    companion object Factory {
        fun create(app: Application): AppComponent {
            return DaggerAppComponent.builder().appModule(AppModule(app)).netModule(NetModule()).build()
        }
    }
}

