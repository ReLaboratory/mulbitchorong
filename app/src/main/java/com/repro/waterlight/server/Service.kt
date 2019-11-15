package com.repro.waterlight.server

import com.repro.waterlight.file.GetName
import com.repro.waterlight.file.SignSuccess
import com.repro.waterlight.file.UploadSuccess
import okhttp3.MultipartBody
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
    fun GetUserName(@Path("id") ID: String): Call<GetName>

    @Multipart
    @POST("/api/img/upload")
    fun Upload(@Part file: MultipartBody.Part, @PartMap hash: Map<String, String>): Call<UploadSuccess>

    @GET("/api/img/upload-name")
    fun GetImgNames(): Call<List<String>>

//    @GET("/api/account/profile/:{id}")
//    fun GetProfileImg(@Path("id") ID: String): Call<String>

//    @Headers("Content-Type: application/json")
//    @POST("/api/account/profile")
//    fun RegisterProfile(@Body body: Map<String, String>): Call<SignSuccess>
}