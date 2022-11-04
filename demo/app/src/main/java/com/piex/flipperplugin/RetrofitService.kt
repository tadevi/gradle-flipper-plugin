package com.piex.flipperplugin

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface RetrofitService {
    @GET("/users")
    fun getUsers(): Call<List<JsonObject>>

    @GET("/users")
    suspend fun getUserAsync(): List<JsonObject>

    companion object {
        const val BASE_URL = "https://5fc85cf82af77700165ad3da.mockapi.io/api/v1/"

        val userService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService::class.java)
        }
    }
}
