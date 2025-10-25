package com.bienestar.app.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8081/"

    fun getRetrofit(context: Context): Retrofit {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val authInterceptor = Interceptor { chain ->
            val token = context.getSharedPreferences("auth", Context.MODE_PRIVATE).getString("token", null)
            val req = if (token != null) {
                chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
            } else chain.request()
            chain.proceed(req)
        }
        val client = OkHttpClient.Builder().addInterceptor(logging).addInterceptor(authInterceptor).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
