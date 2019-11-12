package com.repro.waterlight.server

import com.repro.waterlight.file.SignSuccess
import retrofit2.Call
import retrofit2.http.*

interface Service {
    @Headers("Content-Type: application/json")
    @POST("/api/account/signup")
    fun postSignup(@Body body: Map<String, String>): Call<SignSuccess>

    @Headers("Content-Type: application/json")
    @POST("/api/account/login")
    fun postLogin(@Body body: Map<String, String>): Call<SignSuccess>

    @GET("/api/account/uname/{id}")
    fun GetUserName(@Path("Id") ID: String): Call<String>
}