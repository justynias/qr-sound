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
    val error: MutableLiveData<String> = MutableLiveData(null)
    val isErrorVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun onQrCodeDetected(barCode: String) {
        isLoading.value = true
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
                isLoading.value = false
            }

        } else if (soundResponse.code() == 404) {
            error.value = "This qr code does not exist in our database"
            isErrorVisible.value = true
            isLoading.value = false
        } else if (soundResponse.code() != 200) {
            error.value = "There was some issue, please check your network connection"
            isErrorVisible.value = true
            isLoading.value = false
        }
    }

    private fun onFailure(t: Throwable) {
        Log.d("RESPONSE", "Failed, t: ${t.message}")
        error.value = "There was some issue, please check your network connection"
        isErrorVisible.value = true
        isLoading.value = false
    }

    fun OnTryAgainClicked() {
        isErrorVisible.value = false
    }

}