package com.example.starstec.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starstec.data.database.StressData

@Dao
interface StressDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStressData(stressData: StressData)


    @Query("SELECT * FROM stress_data ORDER BY id DESC LIMIT 1")
    suspend fun getLatestStressData(): StressData?


}
