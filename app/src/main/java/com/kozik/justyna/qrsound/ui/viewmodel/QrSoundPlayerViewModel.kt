package com.kozik.justyna.qrsound.ui.viewmodel

import android.media.MediaPlayer
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kozik.justyna.qrsound.BuildConfig
import com.kozik.justyna.qrsound.repository.QrSoundRepository
import com.kozik.justyna.qrsound.services.ApiClient
import com.kozik.justyna.qrsound.services.data.response.SoundResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.*


class QrSoundPlayerViewModel @ViewModelInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val repository: QrSoundRepository
) : ViewModel(), LifecycleObserver {
    private val service = ApiClient().service
    private val mediaPlayer = MediaPlayer()
    private var hash: String? = null
    val description = MutableLiveData<String>()
    val isSoundPlayed = MutableLiveData<Boolean>(false)

    init {
        hash = repository.hash
        updateSoundDescription()
    }

    private fun updateSoundDescription() {
        if (!hash.isNullOrEmpty()) {
            service.getSound(hash.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it -> onResponse(it) }, { it -> onFailure(it) })
        }
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
//
//    fun onStartStopSoundClicked() {
//        if (mediaPlayer.isPlaying) {
//            stopPlayingSound()
//        }  else {
//
//            startPlayingSound()
//        }
//    }

    fun onStopSoundClicked() {
        mediaPlayer.reset()
    }

    fun onStartSoundClicked() {
        val url = "${BuildConfig.API_KEY}public/${hash}"
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()

        val duration = mediaPlayer.duration.toLong()
        val amoungToupdate = duration / 100.toLong()


        mediaPlayer.start()
        isSoundPlayed.value = true
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                // runOnUiThread {
                Log.d("PLAYER", "Current position: ${mediaPlayer.currentPosition}")
                Log.d("PLAYER", "amoungToupdate: ${amoungToupdate}")
                //}
            }
        }, 0, amoungToupdate)

        mediaPlayer.setOnCompletionListener {
            timer.cancel()
            mediaPlayer.reset()
            isSoundPlayed.value = false
        }


    }

}