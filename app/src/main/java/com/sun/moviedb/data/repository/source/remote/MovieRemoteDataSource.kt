package com.sun.moviedb.data.repository.source.remote

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.sun.moviedb.data.repository.source.MovieDataSource
import com.sun.moviedb.data.repository.source.remote.api.Endpoint
import com.sun.moviedb.data.repository.source.remote.dto.DetailMovieResponse
import com.sun.moviedb.data.repository.source.remote.parse.toDetailMovieResponse
import com.sun.moviedb.utils.network.ApiHelper
import java.util.concurrent.Executors
import java.util.concurrent.Future

class MovieRemoteDataSource : MovieDataSource.Remote {
    private val executor = Executors.newSingleThreadExecutor()
    private val mainThread = Handler(Looper.getMainLooper())

    override fun getDetailMovie(
        slug: String,
        callback: (NetworkResult<DetailMovieResponse>) -> Unit
    ): Future<*> {
        val url_string = Endpoint.GET_MOVIE_DETAIL + slug

        return executor.submit {
            val result : NetworkResult<DetailMovieResponse> = try{
                ApiHelper.getObjectFromUrl(
                    url_string,
                    {body -> body.toDetailMovieResponse()}
                )
            } catch (e: Exception){
                NetworkResult.onError(9999, e.message ?: "Unknown error")
            }

            mainThread.post {
                Log.d(("MovieRemoteDataSource"), "Result: $result")
                callback(result)
            }
        }
    }

    companion object{
        private var instance: MovieRemoteDataSource? = null

        fun getInstance() = synchronized(this) {
            instance ?: MovieRemoteDataSource().also { instance = it }
        }
    }
}
