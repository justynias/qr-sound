package com.kozik.justyna.qrsound.repository


import javax.inject.Inject
import javax.inject.Singleton

//
//class QrSoundRepository {
//    val testList = listOf(1, 2, 3, 4)
//}
@Singleton
class QrSoundRepository @Inject constructor() {
    val hash: String?
        get() = _hash
    private var _hash: String? = null
    fun setHash(hash: String) {
        _hash = hash
    }


}