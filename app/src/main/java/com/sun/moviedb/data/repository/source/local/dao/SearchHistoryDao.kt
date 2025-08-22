package com.sun.moviedb.data.repository.source.local.dao

import androidx.room.*
import com.sun.moviedb.data.repository.source.local.entity.SearchHistoryEntity

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchHistory(history: SearchHistoryEntity)

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC")
    fun getSearchHistory(): List<SearchHistoryEntity>

    @Query("DELETE FROM search_history")
    fun clearSearchHistory()

    @Query("DELETE FROM search_history WHERE keyword = :keyword")
    fun deleteKeyword(keyword: String)
}
