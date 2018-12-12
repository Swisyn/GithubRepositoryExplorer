package com.cuneytayyildiz.githubrepositoryexplorer.di.module

import android.app.Application
import com.cuneytayyildiz.githubrepositoryexplorer.BuildConfig
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.remote.GithubService
import com.cuneytayyildiz.githubrepositoryexplorer.utils.livedata.LiveDataCallAdapterFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class NetModule {

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_END_POINT)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(httpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): GithubService = retrofit.create(GithubService::class.java)

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache = Cache(application.cacheDir, 10 * 1024 * 1024)

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        )
    }

    @Provides
    @Singleton
    fun providesOkHttp(cache: Cache, loggingInterceptor: Interceptor): OkHttpClient {
        val client = OkHttpClient.Builder()

        client.addNetworkInterceptor(  StethoInterceptor())

        client.interceptors().add(loggingInterceptor)
        client.cache(cache)

        client.followRedirects(true)
        client.followSslRedirects(true)
        client.retryOnConnectionFailure(true)

        client.connectTimeout(35, TimeUnit.SECONDS)
        client.readTimeout(35, TimeUnit.SECONDS)
        client.writeTimeout(35, TimeUnit.SECONDS)

        return client.build()
    }

}