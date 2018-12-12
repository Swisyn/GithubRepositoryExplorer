package com.cuneytayyildiz.githubrepositoryexplorer.data.source.vo

import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.remote.ApiResponse
import com.cuneytayyildiz.githubrepositoryexplorer.utils.AppExecutors


@Suppress("LeakingThis")
abstract class NetworkBoundResource<ResultType, RequestType> @MainThread
constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)

        val dbSource = loadFromDb()
        result.addSource(dbSource) {
            result.removeSource(dbSource)
            if (this@NetworkBoundResource.shouldFetch(it)) {
                this@NetworkBoundResource.fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    this@NetworkBoundResource.setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (!areEqual(result.value, newValue)) {
            result.value = newValue
        }
    }

    private fun areEqual(a: Any?, b: Any): Boolean {
        return a == b || a != null && a == b
    }

    fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()

        result.addSource(dbSource) { newData ->
            this@NetworkBoundResource.setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            if (response.isSuccessful) {
                appExecutors.diskIO().execute {
                    this@NetworkBoundResource.processResponse(response)?.let {
                        this@NetworkBoundResource.saveCallResult(it)
                    }

                    appExecutors.mainThread().execute {
                        result.addSource(this@NetworkBoundResource.loadFromDb()) { newData ->
                            this@NetworkBoundResource.setValue(Resource.success(newData))
                        }
                    }
                }
            } else {
                this@NetworkBoundResource.onFetchFailed()
                result.addSource(dbSource) { newData ->
                    this@NetworkBoundResource.setValue(Resource.error(response.errorMessage ?: "", newData))
                }
            }
        }
    }


    protected open fun onFetchFailed() {}

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @WorkerThread
    protected open fun processResponse(response: ApiResponse<RequestType>): RequestType? {
        return response.body
    }

    @WorkerThread
    protected abstract fun saveCallResult(items: RequestType)

    @MainThread
    protected abstract fun shouldFetch(@Nullable data: ResultType): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}