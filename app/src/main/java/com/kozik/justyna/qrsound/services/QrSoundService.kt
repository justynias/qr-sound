package com.kozik.justyna.qrsound.services

import com.kozik.justyna.qrsound.services.data.response.SoundResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QrSoundService {
    @GET("file/{hash}/json")
    fun getSound(@Path("hash") hash: String): Single<Response<SoundResponse>>
}