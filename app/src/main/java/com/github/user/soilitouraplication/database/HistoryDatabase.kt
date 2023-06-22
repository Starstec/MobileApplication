package com.github.user.soilitouraplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.user.soilitouraplication.api.History

@Database(entities = [History::class], version = 6)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        private const val DATABASE_NAME = "HistoryDatabase"

        fun buildDatabase(context: Context): HistoryDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                HistoryDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
