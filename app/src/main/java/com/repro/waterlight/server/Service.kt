package com.repro.waterlight.server

import com.repro.waterlight.file.GetName
import com.repro.waterlight.file.GetimgNames
import com.repro.waterlight.file.SignSuccess
import com.repro.waterlight.file.UploadSuccess
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

    //프로필 가져옴
    @GET("/api/account/profile/{id}")
    fun GetProfileImg(@Path("id") ID: String): Call<ResponseBody>

    @Multipart
    @POST("/api/account/profile")   //프로필 등록
    fun PostRegisterProfile(@Part file: MultipartBody.Part, @PartMap hash: Map<String, String>): Call<UploadSuccess>

    @Multipart
    @PUT("api/account/profile")     //프로필 수정
    fun PutRegisterProfile(@Part file: MultipartBody.Part, @PartMap hash: Map<String, String>): Call<UploadSuccess>

    @GET("api/account/profile-registered/{id}") //프로필 등록 여부
    fun GetProfileRegistered(@Path("id") ID: String): Call<UploadSuccess>
}