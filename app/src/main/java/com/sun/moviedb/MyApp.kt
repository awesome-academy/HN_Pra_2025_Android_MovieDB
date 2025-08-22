package com.sun.moviedb

import android.app.Application
import com.sun.moviedb.data.repository.source.MovieRepository
import com.sun.moviedb.data.repository.source.firebase.MovieFirebaseDataSource
import com.sun.moviedb.data.repository.source.local.AppDatabase
import com.sun.moviedb.data.repository.source.local.MovieLocalDataSource
import com.sun.moviedb.data.repository.source.remote.MovieRemoteDataSource
import com.sun.moviedb.service.SyncLocalFirebaseService

class MyApp : Application() {
    private lateinit var appDatabase: AppDatabase

    lateinit var movieRepository: MovieRepository
        private set

    override fun onCreate() {
        super.onCreate()
        appDatabase = AppDatabase.getInstance(this)
        movieRepository = MovieRepository(
            remote = MovieRemoteDataSource.getInstance(),
            local = MovieLocalDataSource.getInstance(appDatabase),
            firebase = MovieFirebaseDataSource.getInstance()
        )
        SyncLocalFirebaseService.startSyncFavorites(this)
    }
}
