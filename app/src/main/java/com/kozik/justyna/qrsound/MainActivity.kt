package com.kozik.justyna.qrsound

import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.os.Bundle
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.kozik.justyna.qrsound.ui.fragment.QrCodeScannerFragment
import com.kozik.justyna.qrsound.ui.fragment.QrSoundPlayerFragment
import com.kozik.justyna.qrsound.ui.viewmodel.QrCodeScannerViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var detector: BarcodeDetector
    private lateinit var textureView: TextureView

    // private lateinit var imageDimension: Size
    private lateinit var captureRequestBuilder: CaptureRequest.Builder

    //private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var viewModel: QrCodeScannerViewModel
    private var cameraDevice: CameraDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateToQrSoundPlayer()

//        val qrCodeScannerViewModel: QrCodeScannerViewModel by viewModels()
//        viewModel = qrCodeScannerViewModel
//        val binding: ActivityMainBinding =
//            DataBindingUtil.setContentView(this, R.layout.activity_main)
//        binding.viewModel = qrCodeScannerViewModel
//        textureView = binding.cameraPreview
//
//        qrCodeScannerViewModel.description.observe(this, Observer {
//            binding.descriptionTextView.text = it
//        })
//
//        qrCodeScannerViewModel.isSoundPlayed.observe(this, Observer { isPlayed ->
//            binding.startSoundButton.isVisible = !isPlayed
//            binding.stopSoundButton.isVisible = isPlayed
//        })
//        qrCodeScannerViewModel.hash.observe(this, Observer {
//            if (it == null) {
//                soundPlayerView.isVisible = false
//                //qrCodeScannerView.isVisible = true
//                qrCodeScannerView.isVisible = false
//            } else {
//                viewModel.updateSoundDescription()
//                //soundPlayerView.isVisible = true
//                soundPlayerView.isVisible = false
//                qrCodeScannerView.isVisible = false
//            }
//        })
//
//        initQrDecoder()
    }


//    private fun initQrDecoder() {
//        detector = BarcodeDetector.Builder(applicationContext)
//            .setBarcodeFormats(Barcode.DATA_MATRIX or Barcode.QR_CODE).build()
//
//        if (!detector.isOperational) {
//            Toast.makeText(
//                applicationContext,
//                "Could not set up the detector!", Toast.LENGTH_SHORT
//            ).show()
//            return
//        }
//        initTextureView()
//    }
//
//    private fun initTextureView() {
//
//        val textureListener: TextureView.SurfaceTextureListener =
//            object : TextureView.SurfaceTextureListener {
//                override fun onSurfaceTextureAvailable(
//                    surface: SurfaceTexture?,
//                    width: Int,
//                    height: Int
//                ) {
//                    startCameraInstance()
//                }
//
//                override fun onSurfaceTextureSizeChanged(
//                    surface: SurfaceTexture?,
//                    width: Int,
//                    height: Int
//                ) {
//                    //TODO("Not yet implemented")
//                }
//
//                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
//                    //TODO("Not yet implemented")
//                    return false
//                }
//
//                override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
//                    Log.d("CAMERA", "onSurfaceTextureUpdated")
//                    val frame: Frame = Frame.Builder().setBitmap(textureView.bitmap).build()
//                    val barcodes: SparseArray<Barcode> = detector.detect(frame)
//                    if (barcodes.isNotEmpty()) {
//                        val barcode = barcodes.valueAt(0).displayValue
//
//                        viewModel.hash.value = barcode
//                        Log.d("UPDATED", barcode)
////                    viewModel.updateSoundDescription()
////                    soundPlayerView.isVisible = true
////                    qrCodeScannerView.isVisible = false
//
//                        //textureView.surfaceTextureListener = null
//                        //textureView.surfaceTexture.release()
//
//                        //cameraDevice?.close()
//
//                    } else {
//                        //textView.text = "nothing"
//                    }
//
//                }
//
//            }
//        textureView.surfaceTextureListener = textureListener
//    }
//
//    //    /** Check if this device has a camera */
////    private fun checkCameraHardware(context: Context): Boolean {
////        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
////            // this device has a camera
////            return true
////        } else {
////            // no camera on this device
////            return false
////        }
////    }
//    fun startCameraInstance() {
//        val manager = applicationContext?.getSystemService(CAMERA_SERVICE) as CameraManager
//        val firstID = manager.cameraIdList.first()
//        val characteristics = manager.getCameraCharacteristics(firstID)
//        val map: StreamConfigurationMap =
//            characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
//        val imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]
//        if ((ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//            ) != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED)
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(
//                this, arrayOf(
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ), 1
//            )
//        }
//
//        val callback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
//            override fun onOpened(camera: CameraDevice) {
//                cameraDevice = camera
//                val texture = textureView.surfaceTexture
//                texture.setDefaultBufferSize(imageDimension.width, imageDimension.height)
//                val surface = Surface(texture)
//
//                cameraDevice?.let {
//                    captureRequestBuilder =
//                        cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//                    captureRequestBuilder.addTarget(surface)
//
//                    cameraDevice?.createCaptureSession(listOf(surface), object :
//                        CameraCaptureSession.StateCallback() {
//                        override fun onConfigured(session: CameraCaptureSession) {
//                            updatePreview(session)
//                        }
//
//                        override fun onConfigureFailed(session: CameraCaptureSession) {
//                            TODO("Not yet implemented")
//                        }
//
//
//                    }, null)
//                }
//
//
//            }
//
//            override fun onDisconnected(camera: CameraDevice) {
//                cameraDevice?.close()
//                //TODO("Not yet implemented")
//            }
//
//            override fun onError(camera: CameraDevice, error: Int) {
//                cameraDevice?.close()
//                cameraDevice = null
//                //TODO("Not yet implemented")
//            }
//
//        }
//        manager.openCamera(firstID, callback, null)
//    }
//
//    fun updatePreview(cameraCaptureSession: CameraCaptureSession) {
//        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
//
//        Log.d("CAMERA", "test")
//        val thread = HandlerThread("Camera Thread")
//        thread.start()
//        val mBackgroundHandler = Handler(thread.looper)
//        cameraCaptureSession.setRepeatingRequest(
//            captureRequestBuilder.build(),
//            null,
//            mBackgroundHandler
//        )
//    }

    //    fun replaceFragment() {
//        val qrSoundPlayerFragment = QrSoundPlayerFragment()
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragment_placeholder, qrSoundPlayerFragment)
//        transaction.commit()
//    }
//
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