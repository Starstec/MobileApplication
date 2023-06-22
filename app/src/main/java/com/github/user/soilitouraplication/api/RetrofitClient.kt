package com.github.user.soilitouraplication.api

import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object RetrofitClient {
    private const val BASE_URL = "https://soilit-api-iwwdg24ftq-et.a.run.app"
    private const val CACHE_SIZE = 10 * 1024 * 1024 // 10 MB

    private val cache = Cache(File("path_to_cache_directory"), CACHE_SIZE.toLong())

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cache(cache)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun apiInstance(): CampaignApi {
        return retrofit.create(CampaignApi::class.java)
    }

    fun postDetectionApiInstance(): PostDetectionApi {
        return retrofit.create(PostDetectionApi::class.java)
    }


    fun apiInstanceFaq(): FaqApi {
        return retrofit.create(FaqApi::class.java)
    }
}
