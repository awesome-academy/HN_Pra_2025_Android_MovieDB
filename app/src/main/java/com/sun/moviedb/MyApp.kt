package com.sun.moviedb

import android.app.Application
import android.util.Log
import com.sun.moviedb.data.repository.source.remote.datasource.MovieRemoteDataSource
import com.sun.moviedb.data.repository.source.remote.model.MovieDetailResponse

import com.sun.moviedb.utils.network.ApiHelper

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val movieRemoteDataSource = MovieRemoteDataSource()
        movieRemoteDataSource.getMovieDetail("xuan-muon", object : ApiHelper.Callback<MovieDetailResponse> {
            override fun onSuccess(data: MovieDetailResponse) {
                for (dataItem in data.episodes ?: emptyList()) {
                    for (dataItem1 in dataItem.server_data ?: emptyList()) {
                        Log.d("hadsjgdja", "onSuccess: ${dataItem1.name}, ${dataItem1.link_embed}")
                    }
                }
            }

            override fun onError(message: String, code: Int?) {
                Log.e("hadsjgdja", "onError: $message, code: $code")
            }

        })


    }

}
