package com.kozik.justyna.qrsound

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.kozik.justyna.qrsound.databinding.ActivityMainBinding
import com.kozik.justyna.qrsound.ui.viewmodel.QrCodeScannerViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val qrCodeScannerViewModel: QrCodeScannerViewModel by viewModels()
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = qrCodeScannerViewModel

        qrCodeScannerViewModel.description.observe(this, Observer {
            binding.descriptionTextView.text = it
        })
    }


}