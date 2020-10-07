package com.kozik.justyna.qrsound

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kozik.justyna.qrsound.repository.QrSoundRepository
import com.kozik.justyna.qrsound.ui.fragment.QrCodeScannerFragment
import com.kozik.justyna.qrsound.ui.fragment.QrSoundPlayerFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repo: QrSoundRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateToQrCodeScanner()
    }

    fun navigateToQrSoundPlayer() {
        navigateToFragment(QrSoundPlayerFragment())
    }

    fun navigateToQrCodeScanner() {
        navigateToFragment(QrCodeScannerFragment())
    }

    private fun navigateToFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_placeholder, fragment)
        transaction.commit()
    }
}