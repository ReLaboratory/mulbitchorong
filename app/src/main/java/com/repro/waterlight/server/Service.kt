package com.repro.waterlight.server

import com.repro.waterlight.file.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
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
    fun GetImgNames(): Call<ArrayList<GetimgNames>>

    @GET("/api/img/upload-file/{filename}")
    fun GetImage(@Path("filename") fileName: String?): Call<ResponseBody>

    //
    @GET("/api/account/profile/:{id}")
    fun GetProfileImg(@Path("id") ID: String): Call<Getimgs>

    @Headers("Content-Type: application/json")
    @POST("/api/account/profile")
    fun RegisterProfile(@Body body: Map<String, String>): Call<UploadSuccess>
}