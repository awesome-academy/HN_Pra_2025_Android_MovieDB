package com.sun.moviedb.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.google.firebase.auth.FirebaseAuth
import com.sun.moviedb.MyApp
import java.util.concurrent.Executors

class SyncLocalFirebaseService : Service() {
    companion object {
        const val ACTION_SYNC_FAVORITES = "ACTION_SYNC_FAVORITES"
        const val ACTION_BACKUP_SEARCH_HISTORY = "ACTION_BACKUP_SEARCH_HISTORY"
        fun startSyncFavorites(context: Context) {
            val intent = Intent(context, SyncLocalFirebaseService::class.java)
            intent.action = ACTION_SYNC_FAVORITES
            context.startService(intent)
        }

        fun startBackupSearchHistory(context: Context) {
            val intent = Intent(context, SyncLocalFirebaseService::class.java)
            intent.action = ACTION_BACKUP_SEARCH_HISTORY
            context.startService(intent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        Executors.newSingleThreadExecutor().execute {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (!uid.isNullOrEmpty()) {
                val app = applicationContext as MyApp
                when (action) {
                    ACTION_SYNC_FAVORITES, null -> {
                        app.movieRepository.syncFavoritesFromFirebase(uid) {}
                    }

                    ACTION_BACKUP_SEARCH_HISTORY -> {
                        app.movieRepository.backupSearchHistoryToFirebase(uid) {}
                    }
                }
            }
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
