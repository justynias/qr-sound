package com.kozik.justyna.qrsound.repository


import com.kozik.justyna.qrsound.services.ApiClient
import com.kozik.justyna.qrsound.services.data.response.SoundResponse
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QrSoundRepository @Inject constructor() {
    //TODO inject it
    private val apiClientService = ApiClient().service

    //TODO observe it
    val sound: SoundResponse?
        get() = _sound
    private var _sound: SoundResponse? = null
    fun setSound(sound: SoundResponse) {
        _sound = sound
    }

    fun getSound(hash: String): Single<Response<SoundResponse>> {
        return apiClientService.getSound("hash")
    }

}