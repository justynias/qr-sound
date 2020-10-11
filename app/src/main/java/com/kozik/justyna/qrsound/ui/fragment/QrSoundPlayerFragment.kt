package com.kozik.justyna.qrsound.ui.fragment

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
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
    private val qrSoundPlayerViewModel: QrSoundPlayerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            qrSoundPlayerViewModel.onStopSoundClicked()
            //TODO wait for the media player when really stops
            val mainActivity = activity as? MainActivity
            mainActivity?.navigateToQrCodeScanner()
        }
        startSoundButton.setOnClickListener {
            //request for the audio focus
            val audioManager = activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
                setAudioAttributes(AudioAttributes.Builder().run {
                    setUsage(AudioAttributes.USAGE_GAME)
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    build()
                })
                setAcceptsDelayedFocusGain(true)

                setOnAudioFocusChangeListener(afChangeListener)
                build()
            }
            val focusLock = Any()
            val request = audioManager.requestAudioFocus(focusRequest)

            synchronized(focusLock) {
                //TODO throw error message?
                when (request) {
                    AudioManager.AUDIOFOCUS_REQUEST_FAILED -> Log.d("FOCUS", "LREQUEST_FAILED")
                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> qrSoundPlayerViewModel.onStartSoundClicked()
                    AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> Log.d("FOCUS", "LREQUEST_DELAYED")
                    else -> Log.d("FOCUS", "else")
                }
            }
        }

    }


    private val afChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                qrSoundPlayerViewModel.onStopSoundClicked()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                qrSoundPlayerViewModel.onStopSoundClicked()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                qrSoundPlayerViewModel.onStopSoundClicked()
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                qrSoundPlayerViewModel.onStartSoundClicked()
            }
        }
    }
}