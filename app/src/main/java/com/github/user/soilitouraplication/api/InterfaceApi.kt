package com.github.user.soilitouraplication.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface CampaignApi {
    @GET("/campaign/limit")
    fun getCampaigns(): Call<UserResponse>
}

interface HistoryApi {
    @GET("/history/{id}")
    fun getHistory(@Path("id") userId: String): Call<HistoryResponse>
    
    @DELETE("/history/delete/{id}")
    fun deleteHistory(@Path("id") historyId: String): Call<HistoryResponse>
}

interface FaqApi {
    @GET("/faq")
    fun getFaq(): Call<FaqResponse>
}

interface PostDetectionApi {
    @Multipart
    @POST("/history")
    fun postHistory(
        @Part file: MultipartBody.Part,
        @Part("user_id") user_id: RequestBody,
        @Part("soil_type") soil_type: RequestBody,
        @Part("soil_moisture") soil_moisture: RequestBody,
        @Part("soil_temperature") soil_temperature: RequestBody,
        @Part("soil_condition") soil_condition: RequestBody
    ): Call<HistoryPostResponse>
}




