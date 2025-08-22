package com.sun.moviedb.data.repository.source.local.entity

import androidx.room.Entity

@Entity(tableName = "search_history", primaryKeys = ["keyword"])
data class SearchHistoryEntity(
    val keyword: String,
    val timestamp: Long = System.currentTimeMillis()
)
