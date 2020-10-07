package com.kozik.justyna.qrsound.repository


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QrSoundRepository @Inject constructor() {
    val hash: String?
        get() = _hash
    private var _hash: String? = null
    fun setHash(hash: String) {
        _hash = hash
    }


}