package com.github.user.soilitouraplication.database

import androidx.room.*
import com.github.user.soilitouraplication.api.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(history: History)

//    @Query("SELECT * FROM history")
//    fun getAllHistory(): Flow<List<History>>

    @Query("SELECT * FROM history ORDER BY created_at DESC")
    fun getAllHistory(): Flow<List<History>>


    @Query("SELECT * FROM history ORDER BY created_at DESC")
    fun getAllNewHistory(): Flow<List<History>>

    @Query("SELECT * FROM history WHERE created_at = :createdAt LIMIT 1")
    suspend fun getHistoryByCreated(createdAt: String): History?

    @Query("SELECT * FROM history WHERE id = :id")
    suspend fun getHistoryById(id: String): History?


    @Delete
    suspend fun deleteHistory(history: History)

    @Query("DELETE FROM history")
    suspend fun deleteAllHistory()

    @Query("SELECT COUNT(*) FROM history")
    suspend fun getHistoryCount(): Int
}
