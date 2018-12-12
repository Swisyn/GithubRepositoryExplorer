package com.cuneytayyildiz.githubrepositoryexplorer

import android.app.Application
import com.cuneytayyildiz.githubrepositoryexplorer.di.component.AppComponent
import com.facebook.stetho.Stetho

class GithubExplorerApp : Application() {

    companion object {
        //JvmStatic allow access it from java code
        @JvmStatic
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        appComponent = AppComponent.create(this)
    }

}