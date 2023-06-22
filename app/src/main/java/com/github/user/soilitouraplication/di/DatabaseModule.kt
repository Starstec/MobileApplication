package com.github.user.soilitouraplication.di

import android.content.Context
import androidx.room.Room
import com.github.user.soilitouraplication.database.HistoryDao
import com.github.user.soilitouraplication.database.HistoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideHistoryDao(database: HistoryDatabase): HistoryDao {
        return database.historyDao()
    }

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): HistoryDatabase {
        return HistoryDatabase.buildDatabase(appContext)
    }


}
