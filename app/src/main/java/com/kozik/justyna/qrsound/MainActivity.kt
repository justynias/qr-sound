package com.kozik.justyna.qrsound

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kozik.justyna.qrsound.services.ApiClient
import com.kozik.justyna.qrsound.services.data.response.SoundResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testAPI()
    }

    fun testAPI() {
        val service = ApiClient().service
        service.getSound("229e9253-5fe4-49b0-b0b4-29046fca45c8")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSuccess { onResponse(it) }
            .doOnError { onFailure(it) }
            .subscribe()
            .dispose()

    }

    fun onFailure(t: Throwable) {
        Log.d("RESPONSE", "Failed")
    }

    fun onResponse(soundResponse: Response<SoundResponse>) {
        if (soundResponse.isSuccessful) {
            val record = soundResponse.body()
            Log.d("RESPONSE", "Description: ${record?.description}\n Hash: ${record?.hash}")
        } else {
            Log.d("RESPONSE", soundResponse.code().toString())
        }
    }
}