package com.kozik.justyna.qrsound.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kozik.justyna.qrsound.repository.QrSoundRepository

class QrCodeScannerViewModel @ViewModelInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val repository: QrSoundRepository
) : ViewModel(), LifecycleObserver {
    val hash: MutableLiveData<String> = MutableLiveData()

    fun onQrCodeDetected(barCode: String) {
        repository.setHash(barCode)
    }
}