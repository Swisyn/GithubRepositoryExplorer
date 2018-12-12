package com.cuneytayyildiz.githubrepositoryexplorer.data.source.vo

import com.cuneytayyildiz.githubrepositoryexplorer.data.source.vo.Status.*

data class Resource<T>(val status: Status, val data: T?, val message: String?) {
    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T? = null) : Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}