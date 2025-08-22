package com.sun.moviedb.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.sun.moviedb.MyApp
import java.util.concurrent.Executors

class ClearLocalDataService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Executors.newSingleThreadExecutor().execute {
            val app = applicationContext as MyApp
            app.movieRepository.clearLocalData()
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

