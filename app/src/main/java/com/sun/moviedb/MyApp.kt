package com.sun.moviedb

import android.app.Application
import com.sun.moviedb.data.repository.source.remote.MovieRemoteDataSource
import com.sun.moviedb.data.repository.source.remote.NetworkResult
import com.sun.moviedb.data.repository.source.remote.dto.DetailMovieResponse

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Test getDetailMovie on app start
        val dataSource = MovieRemoteDataSource.getInstance()
        dataSource.getDetailMovie("ngoi-truong-xac-song") { result ->
            when (result) {
                is NetworkResult.onSuccess<DetailMovieResponse> -> {
                    android.util.Log.d("MyApp", "Movie detail: ${result.data}")
                }
                is NetworkResult.onError -> {
                    android.util.Log.e("MyApp", "Error: ${result.message}")
                }
            }
        }
    }

}
