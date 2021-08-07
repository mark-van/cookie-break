package com.example.cookiebreak.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface  HistoryDao {
    @Query("SELECT * FROM history ORDER BY cookie_time ASC")
    fun getAll(): Flow<List<History>>

    //insert is a suspend function so that it can be ryn from coroutine
    //insert can take awhile
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: History)

    @Delete
    suspend fun delete(history: History)
}