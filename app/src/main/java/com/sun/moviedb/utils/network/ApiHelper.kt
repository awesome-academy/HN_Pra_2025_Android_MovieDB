package com.sun.moviedb.utils.network

import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.data.repository.source.remote.exception.ApiLogicalException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


/* *
* Fetch json data from URL,
* Convert json to object model
* */
object ApiHelper {
    private const val TIME_OUT = 15000
    private const val METHOD_GET = "GET"

    data class RawData(val code: Int, val body: String?)

    @Throws(Exception::class)
    fun getRawDataFromURL(
        url: String
    ): RawData {
        var conn: HttpURLConnection? = null

        return try {
            val url = URL(url)
            conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = METHOD_GET
                connectTimeout = TIME_OUT
                readTimeout = TIME_OUT
                useCaches = false
            }

            val code = conn.responseCode
            val inputStream = if (code in 200..299) {
                conn.inputStream
            } else {
                conn.errorStream
            }
            val reader = BufferedReader(InputStreamReader(inputStream))
            val body = reader.readText()
            reader.close()

            RawData(code, body)
        } finally {
            conn?.disconnect()
        }
    }

    fun <T> getObjectFromUrl(
        urlString: String,
        parser: (String) -> T
    ): NetworkResult<T> {
        return try {
            val raw = getRawDataFromURL(urlString)
            val code = raw.code
            val body = raw.body

            if (code in 200..299 && body != null) {
                val objectModel = parser(body)
                NetworkResult.OnSuccess(objectModel)
            } else {
                NetworkResult.OnError(code, "HTTP $code: $body")
            }
        } catch (e: ApiLogicalException) {
            NetworkResult.OnError(
                200,
                e.message ?: "API logical error"

            )
        } catch (e: Exception) {
            NetworkResult.OnError(null, "Network/Parse error: ${e.message}")
        }
    }
}