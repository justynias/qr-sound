package com.kozik.justyna.qrsound.services

import com.kozik.justyna.qrsound.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

// TODO create in better way and add network observer
class ApiClient {
    val url = BuildConfig.API_KEY
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
    val service = retrofit.create(QrSoundService::class.java)
}