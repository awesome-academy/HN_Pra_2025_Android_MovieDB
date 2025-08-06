package com.sun.moviedb.utils.network

import android.os.Handler
import android.os.Looper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import org.json.JSONArray
import org.json.JSONObject

object ApiHelper {

    interface Callback<T> {
        fun onLoading(isLoading: Boolean) {}
        fun onSuccess(data: T)
        fun onError(message: String, code: Int? = null)
    }

    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    private fun getJsonFromUrl(urlString: String): Pair<Boolean, String?> {
        var connection: HttpURLConnection? = null
        return try {
            val url = URL(urlString)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            val responseCode = connection.responseCode
            val inputStream = if (responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }

            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = reader.readText()
            reader.close()

            if (responseCode in 200..299) {
                Pair(true, response)
            } else {
                Pair(false, "HTTP $responseCode: $response")
            }
        } catch (e: Exception) {
            Pair(false, "Exception: ${e.localizedMessage}")
        } finally {
            connection?.disconnect()
        }
    }

    fun <T> fetchObjectAsync(
        urlString: String,
        parse: (JSONObject) -> T,
        callback: Callback<T>
    ) {
        callback.onLoading(true)
        executor.execute {
            val (success, result) = getJsonFromUrl(urlString)
            mainHandler.post {
                callback.onLoading(false)
                if (success && result != null) {
                    try {
                        val json = JSONObject(result)
                        val obj = parse(json)
                        callback.onSuccess(obj)
                    } catch (e: Exception) {
                        callback.onError("Parse error: ${e.message}")
                    }
                } else {
                    callback.onError(result ?: "Unknown error")
                }
            }
        }
    }

    fun <T> fetchListAsync(
        urlString: String,
        parse: (JSONObject) -> T,
        callback: Callback<List<T>>
    ) {
        callback.onLoading(true)
        executor.execute {
            val (success, result) = getJsonFromUrl(urlString)
            mainHandler.post {
                callback.onLoading(false)
                if (success && result != null) {
                    try {
                        val jsonArray = JSONArray(result)
                        val list = mutableListOf<T>()
                        for (i in 0 until jsonArray.length()) {
                            val item = jsonArray.getJSONObject(i)
                            list.add(parse(item))
                        }
                        callback.onSuccess(list)
                    } catch (e: Exception) {
                        callback.onError("Parse error: ${e.message}")
                    }
                } else {
                    callback.onError(result ?: "Unknown error")
                }
            }
        }
    }
}
