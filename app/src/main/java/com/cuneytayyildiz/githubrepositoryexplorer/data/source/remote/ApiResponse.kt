package com.cuneytayyildiz.githubrepositoryexplorer.data.source.remote


import androidx.annotation.Nullable
import androidx.collection.ArrayMap
import retrofit2.Response
import java.io.IOException
import java.util.*
import java.util.regex.Pattern


/**
 * Common class used by API responses.
 *
 * @param <T>
</T> */
class ApiResponse<T> {
    val code: Int
    @Nullable  val body: T?
    @Nullable val errorMessage: String?
    val links: MutableMap<String, String>

    val isSuccessful: Boolean
        get() = code in 200..299

    val nextPage: Int?
        get() {
            val next = links[NEXT_LINK] ?: return null
            val matcher = PAGE_PATTERN.matcher(next)
            if (!matcher.find() || matcher.groupCount() !== 1) {
                return null
            }
            try {
                return Integer.parseInt(matcher.group(1))
            } catch (ex: NumberFormatException) {
                return null
            }

        }

    constructor(error: Throwable) {
        code = 500
        body = null
        errorMessage = error.message
        links = Collections.emptyMap()
    }

    constructor(response: Response<T>) {
        code = response.code()
        if (response.isSuccessful) {
            body = response.body()
            errorMessage = null
        } else {
            var message: String? = null
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody()!!.string()
                } catch (ignored: IOException) {
                    //                    Timber.e(ignored, "error while parsing response");
                }

            }
            if (message == null || message.trim { it <= ' ' }.isEmpty()) {
                message = response.message()
            }
            errorMessage = message
            body = null
        }
        val linkHeader = response.headers().get("link")
        if (linkHeader == null) {
            links = Collections.emptyMap()
        } else {
            links = ArrayMap()
            val matcher = LINK_PATTERN.matcher(linkHeader)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)] = matcher.group(1)
                }
            }
        }
    }

    companion object {
        private val LINK_PATTERN = Pattern
            .compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private val NEXT_LINK = "next"
    }
}