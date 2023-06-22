package com.github.user.soilitouraplication.ui.detection

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.user.soilitouraplication.R
import com.github.user.soilitouraplication.databinding.ActivityClassifierBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ClassifierActivity : AppCompatActivity() {
    private lateinit var classifier: Classifier
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: ActivityClassifierBinding

    companion object {
        private const val MODEL_PATH = "modelsoildetection.tflite"
        private const val LABEL_PATH = "labels.txt"
        private const val INPUT_SIZE = 224
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassifierBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        cameraExecutor = Executors.newSingleThreadExecutor()
        classifier = Classifier.create(assets, MODEL_PATH, LABEL_PATH, INPUT_SIZE)

        val resultDialog = Dialog(this)
        val customProgressView =
            LayoutInflater.from(this).inflate(R.layout.result_dialog_layout, null)
        resultDialog.setCancelable(false)
        resultDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        resultDialog.setContentView(customProgressView)

        val ivImageResult = customProgressView.findViewById<ImageView>(R.id.iViewResult)
        val tvLoadingText = customProgressView.findViewById<TextView>(R.id.tvLoadingRecognition)
        val tvTextResults = customProgressView.findViewById<TextView>(R.id.tvResult)
        val aviLoaderHolder = customProgressView.findViewById<View>(R.id.aviLoaderHolderView)

        val imageCapture = ImageCapture.Builder().build()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

                binding.btnDetectObject.setOnClickListener {
                    resultDialog.show()
                    tvTextResults.visibility = View.GONE
                    ivImageResult.visibility = View.GONE
                    imageCapture.takePicture(cameraExecutor,
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                val bitmap = imageProxyToBitmap(image)
                                image.close()
                                val scaledBitmap =
                                    Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)
                                val results = classifier.recognizeImage(scaledBitmap)
                                runOnUiThread {
                                    ivImageResult.setImageBitmap(scaledBitmap)
                                    tvTextResults.text = results.toString()

                                    tvTextResults.visibility = View.VISIBLE
                                    ivImageResult.visibility = View.VISIBLE

                                    resultDialog.setCancelable(true)

                                    val currentDate = getCurrentDate()
                                    val currentTime = getCurrentTime()
                                    val intent = Intent(
                                        this@ClassifierActivity, DetailDetectionActivity::class.java
                                    )
                                    intent.putExtra("results", results.toString())
                                    intent.putExtra("tvTextResults", tvTextResults.text.toString())
                                    intent.putExtra("currentDate", currentDate)
                                    intent.putExtra("currentTime", currentTime)

                                    val stream = ByteArrayOutputStream()
                                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                                    val byteArray = stream.toByteArray()
                                    intent.putExtra("image", byteArray)

                                    startActivity(intent)
                                }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                // Handle capture error
                                exception.printStackTrace()
                            }
                        })
                }

                resultDialog.setOnDismissListener {
                    tvLoadingText.visibility = View.VISIBLE
                    aviLoaderHolder.visibility = View.VISIBLE
                }

                makeButtonVisible()

                if (!allPermissionsGranted()) {
                    ActivityCompat.requestPermissions(
                        this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                    )
                }
            } catch (exc: Exception) {
                // Handle camera binding exception
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        classifier.close()
    }

    private fun makeButtonVisible() {
        runOnUiThread {
            binding.btnDetectObject.visibility = View.VISIBLE
        }
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Tidak mendapatkan permission.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun getCurrentTime(): String {
        return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    }
}

interface SoilClassifier {
    data class Recognition(
        var id: String = "", // A unique identifier for what has been recognized. Specific to the class, not the instance of the object.
        var title: String = "", // Display name for the recognition.
        var confidence: Float = 0F, // A sortable score for how good the recognition is relative to others. Higher should be better.
    ) {
        override fun toString(): String {
            return title
        }
    }

    fun recognizeImage(bitmap: Bitmap): Recognition

    fun close()
}


