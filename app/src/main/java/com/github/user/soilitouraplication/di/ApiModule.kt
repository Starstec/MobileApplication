package com.github.user.soilitouraplication.di

import com.github.user.soilitouraplication.api.HistoryApi
import com.github.user.soilitouraplication.api.HistoryApiImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun provideHistoryApi(retrofit: Retrofit): HistoryApi {
        return HistoryApiImplementation(retrofit)
    }

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://soilit-api-iwwdg24ftq-et.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
