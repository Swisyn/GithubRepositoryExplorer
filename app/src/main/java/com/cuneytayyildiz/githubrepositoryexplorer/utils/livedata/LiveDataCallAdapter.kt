package com.cuneytayyildiz.githubrepositoryexplorer.utils.livedata

import androidx.lifecycle.LiveData
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.remote.ApiResponse
import retrofit2.*
import retrofit2.CallAdapter.Factory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean


class LiveDataCallAdapterFactory : Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (LiveData::class.java != getRawType(returnType)) {
            return null
        }
        val responseType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawType = getRawType(responseType)
        if (rawType != ApiResponse::class.java) {
            throw IllegalArgumentException("LiveData return type must be parameterized as LiveData<Foo> or LiveData<out Foo>")
        }
        if (responseType !is ParameterizedType) {
            throw IllegalArgumentException("ApiResponse must be parameterized as ApiResponse<Foo> or ApiResponse<out Foo>")
        }
        val bodyType = getParameterUpperBound(0, responseType)
        return LiveDataCallAdapter<Any>(bodyType)
    }

    inner class LiveDataCallAdapter<R>(private val responseType: Type) : CallAdapter<R, LiveData<ApiResponse<R>>> {

        override fun responseType(): Type {
            return responseType
        }

        override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {
            return object : LiveData<ApiResponse<R>>() {
                var started = AtomicBoolean(false)

                override fun onActive() {
                    super.onActive()

                    if (started.compareAndSet(false, true)) {
                        call.enqueue(object : Callback<R> {
                            override fun onResponse(call: Call<R>, response: Response<R>) {
                                postValue(ApiResponse(response))
                            }

                            override fun onFailure(call: Call<R>, t: Throwable) {
                                postValue(ApiResponse(t))
                            }
                        })
                    }
                }
            }
        }
    }
}