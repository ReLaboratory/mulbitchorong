package com.repro.waterlight.server

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object retro {
    var okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    var BASE_URL:String="http://10.156.147.150:3000"
    val getClient: Service
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(Service::class.java)
        }
}