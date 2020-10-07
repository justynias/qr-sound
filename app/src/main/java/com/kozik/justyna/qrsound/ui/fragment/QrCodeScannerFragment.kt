package com.kozik.justyna.qrsound.ui.fragment

import android.Manifest
import android.content.Context.CAMERA_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.SparseArray
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.util.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.kozik.justyna.qrsound.MainActivity
import com.kozik.justyna.qrsound.databinding.QrCodeScannerFragmentBinding
import com.kozik.justyna.qrsound.ui.viewmodel.QrCodeScannerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.qr_code_scanner_fragment.*

@AndroidEntryPoint
class QrCodeScannerFragment : Fragment() {
    private lateinit var detector: BarcodeDetector
    private lateinit var captureRequestBuilder: CaptureRequest.Builder
    private val qrCodeScannerViewModel: QrCodeScannerViewModel by viewModels()
    private var cameraDevice: CameraDevice? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = QrCodeScannerFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = qrCodeScannerViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initQrDecoder()
        initTextureView()
    }

    private fun initQrDecoder() {
        detector = BarcodeDetector.Builder(activity)
            .setBarcodeFormats(Barcode.DATA_MATRIX or Barcode.QR_CODE).build()

        if (!detector.isOperational) {
            Toast.makeText(
                activity,
                "Could not set up the detector!", Toast.LENGTH_SHORT
            ).show()
            return
        }
    }

    private fun initTextureView() {

        val textureListener: TextureView.SurfaceTextureListener =
            object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    surface: SurfaceTexture?,
                    width: Int,
                    height: Int
                ) {
                    startCameraInstance()
                }

                override fun onSurfaceTextureSizeChanged(
                    surface: SurfaceTexture?,
                    width: Int,
                    height: Int
                ) {
                    //TODO("Not yet implemented")
                }

                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                    //TODO("Not yet implemented")
                    return false
                }

                override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
                    Log.d("CAMERA", "onSurfaceTextureUpdated")
                    val t = cameraPreview.bitmap
                    val m = Matrix()
                    m.postRotate(90.0f)

                    val x = rectanglePreview.x.toInt()
                    val y = rectanglePreview.y.toInt()
                    val w = rectanglePreview.width
                    val h = rectanglePreview.height

                    val test = Bitmap.createBitmap(t, x, y, w, h, m, false)
                    val frame: Frame = Frame.Builder().setBitmap(test).build()

                    // val frame: Frame = Frame.Builder().setBitmap(cameraPreview.bitmap).build()
                    val barcodes: SparseArray<Barcode> = detector.detect(frame)
                    if (barcodes.isNotEmpty()) {
                        val barcode = barcodes.valueAt(0).displayValue

                        //TODO here handle if the qr exist on our backend
                        qrCodeScannerViewModel.hash.value = barcode
                        qrCodeScannerViewModel.onQrCodeDetected(barcode)
                        val mainActivity = activity as? MainActivity
                        mainActivity?.navigateToQrSoundPlayer()
                        cameraDevice?.close()
                    }
                }

            }
        cameraPreview.surfaceTextureListener = textureListener
    }

    //TODO
    //    /** Check if this device has a camera */
//    private fun checkCameraHardware(context: Context): Boolean {
//        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
//            // this device has a camera
//            return true
//        } else {
//            // no camera on this device
//            return false
//        }
//    }
    fun startCameraInstance() {
        val manager = activity?.getSystemService(CAMERA_SERVICE) as CameraManager
        val firstID = manager.cameraIdList.first()
        val characteristics = manager.getCameraCharacteristics(firstID)
        val map: StreamConfigurationMap =
            characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
        val imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]
        if ((ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            this.activity?.let {
                ActivityCompat.requestPermissions(
                    it, arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 1
                )
            }
        }

        val callback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                val texture = cameraPreview.surfaceTexture
                texture.setDefaultBufferSize(imageDimension.width, imageDimension.height)
                val surface = Surface(texture)

                cameraDevice?.let {
                    captureRequestBuilder =
                        cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    captureRequestBuilder.addTarget(surface)

                    cameraDevice?.createCaptureSession(listOf(surface), object :
                        CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            updatePreview(session)
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            TODO("Not yet implemented")
                        }


                    }, null)
                }


            }

            override fun onDisconnected(camera: CameraDevice) {
                cameraDevice?.close()
                //TODO("Not yet implemented")
            }

            override fun onError(camera: CameraDevice, error: Int) {
                cameraDevice?.close()
                cameraDevice = null
                //TODO("Not yet implemented")
            }

        }
        manager.openCamera(firstID, callback, null)
    }

    fun updatePreview(cameraCaptureSession: CameraCaptureSession) {
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

        Log.d("CAMERA", "test")
        val thread = HandlerThread("Camera Thread")
        thread.start()
        val mBackgroundHandler = Handler(thread.looper)
        cameraCaptureSession.setRepeatingRequest(
            captureRequestBuilder.build(),
            null,
            mBackgroundHandler
        )
    }
}