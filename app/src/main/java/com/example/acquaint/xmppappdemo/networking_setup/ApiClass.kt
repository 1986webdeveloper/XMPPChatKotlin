package com.example.acquaint.xmppappdemo.networking_setup

import com.example.acquaint.xmppappdemo.app.Endpoints

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClass {

    // OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    val retrofitObject: ApiInterface
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.MINUTES)
                    .readTimeout(300, TimeUnit.SECONDS)
                    .writeTimeout(150, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(Endpoints.ENDPOINT_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(ApiInterface::class.java)
        }
}
