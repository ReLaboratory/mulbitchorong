package com.repro.waterlight.server

import com.repro.waterlight.file.SignupSuccess
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Service {
    @Headers("Content-Type: application/json")
    @POST("/api/account/signup")
    fun postSignup(@Body body: Map<String, String>): Call<SignupSuccess>
}