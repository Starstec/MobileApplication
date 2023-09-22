package com.example.starstec.di

import android.content.Context
import androidx.room.Room
import com.example.starstec.data.database.AppDatabase
import com.example.starstec.data.database.dao.StressDataDao
import com.example.starstec.data.repository.StressDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "stress_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideStressDataDao(appDatabase: AppDatabase): StressDataDao {
        return appDatabase.stressDataDao()
    }

    @Provides
    fun provideStressDataRepository(stressDataDao: StressDataDao): StressDataRepository {
        return StressDataRepository(stressDataDao)
    }

}
