package com.sun.moviedb.utils.network

import android.os.Handler
import android.os.Looper
import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.data.repository.source.remote.exception.ApiLogicalException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future


/* *
* Fetch json data from URL,
* Convert json to object model
* */
object ApiHelper {
    private const val TIME_OUT = 15000
    private const val METHOD_GET = "GET"
    // asynchronous executor 4 threads
    private val executor = Executors.newFixedThreadPool(4)
    private val mainThread = Handler(Looper.getMainLooper())

    data class RawData(val code: Int, val body: String?)

    @Throws(Exception::class)
    private fun getRawDataFromURL(url: String): RawData {
        (URL(url).openConnection() as HttpURLConnection).run {
            requestMethod = METHOD_GET
            connectTimeout = TIME_OUT
            readTimeout = TIME_OUT
            useCaches = false
            val code = responseCode
            val inputStream = if (code in 200..299) inputStream else errorStream
            val body = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
            disconnect()
            return RawData(code, body)
        }
    }

    private fun <R> getResultFromUrl(urlString: String, parser: (String) -> R): NetworkResult<R> =
        try {
            getRawDataFromURL(urlString).let { raw ->
                if (raw.code in 200..299 && raw.body != null) NetworkResult.OnSuccess(parser(raw.body))
                else NetworkResult.OnError(raw.code, "HTTP ${raw.code}: ${raw.body}")
            }
        } catch (e: ApiLogicalException) {
            NetworkResult.OnError(9999, e.message ?: "API logical error")
        } catch (e: Exception) {
            NetworkResult.OnError(null, "Network/Parse error: ${e.message}")
        }

    fun <R> getResultFromUrlAsync(
        urlString: String,
        parser: (String) -> R,
        callback: (NetworkResult<R>) -> Unit
    ): Future<*> = executor.submit {
        val result = getResultFromUrl(urlString, parser)
        mainThread.post { callback(result) }
    }
}