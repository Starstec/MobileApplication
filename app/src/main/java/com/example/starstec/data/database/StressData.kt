package com.example.starstec.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stress_data")
data class StressData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hr: String,
    val hrv: String,
    val scr: String,
    val timestamp: Long = System.currentTimeMillis()
)

