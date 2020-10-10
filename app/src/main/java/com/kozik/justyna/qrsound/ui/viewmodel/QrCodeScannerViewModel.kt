package com.kozik.justyna.qrsound.ui.viewmodel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kozik.justyna.qrsound.repository.QrSoundRepository
import com.kozik.justyna.qrsound.services.data.response.SoundResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class QrCodeScannerViewModel @ViewModelInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val repository: QrSoundRepository
) : ViewModel(), LifecycleObserver {
    val detectedSound: MutableLiveData<SoundResponse> = MutableLiveData()

    fun onQrCodeDetected(barCode: String) {
        repository.getSound(barCode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ it -> onResponse(it) }, { it -> onFailure(it) })
    }

    private fun onResponse(soundResponse: Response<SoundResponse>) {
        if (soundResponse.isSuccessful) {
            soundResponse.body()?.let {
                repository.setSound(it)
                //TODO observe only repo values
                detectedSound.value = it
            }

        } else if (soundResponse.code() == 404) {
            //TODO error handling, Qr code was recognized, but its not in our db
        } else {
            //TODO general error handling
        }
    }

    private fun onFailure(t: Throwable) {
        //TODO general error handling (check the internet connection etc)
        Log.d("RESPONSE", "Failed, t: ${t.message}")
    }

}