package com.example.starstec.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.starstec.data.database.dao.StressDataDao
import javax.inject.Singleton

@Database(entities = [StressData::class], version = 2)
@Singleton // This annotation is used when the database is a singleton (usually the case)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stressDataDao(): StressDataDao
}
