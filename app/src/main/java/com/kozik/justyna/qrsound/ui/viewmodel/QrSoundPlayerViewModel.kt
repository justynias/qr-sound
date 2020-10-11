package com.kozik.justyna.qrsound.ui.viewmodel

import android.media.MediaPlayer
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kozik.justyna.qrsound.BuildConfig
import com.kozik.justyna.qrsound.repository.QrSoundRepository
import com.kozik.justyna.qrsound.services.data.response.SoundResponse
import java.util.*


class QrSoundPlayerViewModel @ViewModelInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val repository: QrSoundRepository
) : ViewModel(), LifecycleObserver {
    private val mediaPlayer = MediaPlayer()
    private var sound: SoundResponse? = null
    val description = MutableLiveData<String>()
    val isSoundPlayed = MutableLiveData<Boolean>(false)

    init {
        updateSoundDescription()
    }

    private fun updateSoundDescription() {
        sound = repository.sound
        description.value = sound?.description
    }

    fun onStopSoundClicked() {
        isSoundPlayed.value = false
        mediaPlayer.reset()
    }

    fun onStartSoundClicked() {
        sound?.let {
            val url = "${BuildConfig.API_KEY}public/${"it.hash"}"
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            // if the url does not exist -> app is lagging and error

            val duration = mediaPlayer.duration.toLong()
            val amoungToupdate = duration / 100.toLong()


            mediaPlayer.start()
            isSoundPlayed.value = true
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    // runOnUiThread {
//                Log.d("PLAYER", "Current position: ${mediaPlayer.currentPosition}")
//                Log.d("PLAYER", "amoungToupdate: ${amoungToupdate}")
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

}

