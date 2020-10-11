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


class QrSoundPlayerViewModel @ViewModelInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val repository: QrSoundRepository
) : ViewModel(), LifecycleObserver {
    private val mediaPlayer = MediaPlayer()
    private var sound: SoundResponse? = null
    val description = MutableLiveData<String>()
    val isSoundPlayed = MutableLiveData<Boolean>(false)
    val error: MutableLiveData<String> = MutableLiveData(null)
    val isErrorVisible: MutableLiveData<Boolean> = MutableLiveData(false)

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
            val url = "${BuildConfig.API_KEY}public/${it.hash}"

            try {
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepare()//Async()
            } catch (e: Exception) {
                error.value = "There was some issue, please check your network connection"
                isErrorVisible.value = true
            }

            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                isSoundPlayed.value = true
            }

            mediaPlayer.setOnCompletionListener {

                mediaPlayer.reset()
                isSoundPlayed.value = false
            }
        }

    }

    fun OnTryAgainClicked() {
        isErrorVisible.value = false
    }

}

