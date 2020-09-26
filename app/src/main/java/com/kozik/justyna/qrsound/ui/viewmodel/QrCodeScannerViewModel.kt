package com.kozik.justyna.qrsound.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kozik.justyna.qrsound.services.ApiClient
import com.kozik.justyna.qrsound.services.data.response.SoundResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

//import io.reactivex.rxkotlin.subscribeBy

class QrCodeScannerViewModel : ViewModel() {
    val hash: MutableLiveData<String> = MutableLiveData("229e9253-5fe4-49b0-b0b4-29046fca45c8")

    val description = MutableLiveData<String>()
    //fun description(): LiveData<String> = description

    //"229e9253-5fe4-49b0-b0b4-29046fca45c8"

    fun updateSoundDescription() {
        val service = ApiClient().service
        service.getSound(hash.value.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ it -> onResponse(it) }, { it -> onFailure(it) })

    }

    private fun onFailure(t: Throwable) {
        Log.d("RESPONSE", "Failed, t: ${t.message}")
    }

    private fun onResponse(soundResponse: Response<SoundResponse>) {
        if (soundResponse.isSuccessful) {
            description.value = soundResponse.body()?.description
        } else {
            description.value = soundResponse.code().toString()
        }
    }
}