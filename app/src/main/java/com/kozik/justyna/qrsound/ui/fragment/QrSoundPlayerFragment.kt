package com.kozik.justyna.qrsound.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kozik.justyna.qrsound.MainActivity
import com.kozik.justyna.qrsound.databinding.QrSoundPlayerFragmentBinding
import com.kozik.justyna.qrsound.ui.viewmodel.QrSoundPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.qr_sound_player_fragment.*

@AndroidEntryPoint
class QrSoundPlayerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val qrSoundPlayerViewModel: QrSoundPlayerViewModel by viewModels()
        val binding = QrSoundPlayerFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = qrSoundPlayerViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanAgainButton.setOnClickListener {
            val mainActivity = activity as? MainActivity
            mainActivity?.navigateToQrCodeScanner()
        }
    }
}