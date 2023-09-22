package com.example.starstec.ui.activity.offlinedetection

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.starstec.data.repository.StressDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
@Suppress("DEPRECATION")
class OfflineDetectionViewModel @Inject constructor(private val stressDataRepository: StressDataRepository) : ViewModel() {

    private val _hr = MutableLiveData<String>()
    val hr: LiveData<String> = _hr

    private val _hrv = MutableLiveData<String>()
    val hrv: LiveData<String> = _hrv

    private val _scr = MutableLiveData<String>()
    val scr: LiveData<String> = _scr

    private val _isDetecting = MutableLiveData<Boolean>()
    val isDetecting: LiveData<Boolean> = _isDetecting

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private var client: OkHttpClient
    private var handler: Handler
    private lateinit var stopDetectionRunnable: Runnable
    private var currentCall: Call? = null

    init {
        _isDetecting.value = false
        _isButtonEnabled.value = true
        client = OkHttpClient()
        handler = Handler()
    }

    fun startDetection() {
        _isDetecting.value = true
        _isButtonEnabled.value = false

        val request = Request.Builder()
            .url("http://192.168.28.32") // Ganti dengan URL API yang sesuai
            .build()

        stopDetectionRunnable = Runnable {
            stopDetection()
        }

        handler.postDelayed(stopDetectionRunnable, 30000) // Durasi deteksi (30 detik)

        var requestCount = 0 // Menyimpan jumlah permintaan yang telah dilakukan

        val runnable = object : Runnable {
            override fun run() {
                if (requestCount >= 10) { // Stop jika gelang tidak terhubung atau permintaan sudah dilakukan sebanyak 10 kali
                    stopDetection() // Revert button state
                    return
                }

                currentCall = client.newCall(request)
                currentCall?.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        _isDetecting.postValue(false)
                        _isButtonEnabled.postValue(true)
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseData = response.body()?.string()

                        // Tangani data yang diterima dari API
                        handler.post {
                            handleResponseData(responseData)
                        }
                    }
                })

                // Set delay waktu antara request berikutnya
                handler.postDelayed(this, 3000) // Ganti dengan delay yang sesuai (dalam milidetik)

                requestCount++
            }
        }

        handler.post(runnable)
    }

    private fun saveDataToDatabase(hr: String, hrv: String, scr: String) {
        // Save data to the database using the StressDataRepository
        CoroutineScope(Dispatchers.IO).launch {
            stressDataRepository.saveStressData(hr, hrv, scr)
        }
    }

    fun stopDetection() {
        _isDetecting.value = false
        _isButtonEnabled.value = true
        handler.removeCallbacks(stopDetectionRunnable)
        currentCall?.cancel()
        currentCall = null
    }

    private fun handleResponseData(responseData: String?) {
        responseData?.let {
            // Parsing data dari responseData
            val data = it.split("\n")
            var hr = ""
            var hrv = ""
            var scr = ""

            for (item in data) {
                val keyValue = item.split(": ")
                if (keyValue.size == 2) {
                    val key = keyValue[0]
                    val value = keyValue[1]

                    when (key) {
                        "Heart Rate" -> hr = value
                        "Heart Rate Variability (HRV)" -> hrv = value
                        "EDA" -> scr = value
                    }
                }
            }

            // Update LiveData
            _hr.value = hr
            _hrv.value = hrv
            _scr.value = scr

            saveDataToDatabase(hr, hrv, scr)
        }
    }
}
