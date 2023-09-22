package com.example.starstec.ui.activity.realtimedetection

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.starstec.databinding.ActivityRealtimeStressDetectionBinding
import com.example.starstec.ui.activity.MainActivity
import com.example.starstec.ui.activity.ble.BleServiceHolder
import com.masselis.rxbluetoothkotlin.findCharacteristic
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import java.util.UUID

@Suppress("DEPRECATION")
class RealtimeStressDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRealtimeStressDetectionBinding

    private lateinit var handler: Handler
    private lateinit var hrvTextView: TextView
    private lateinit var previousHRV: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealtimeStressDetectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        hrvTextView = binding.hrv
        handler = Handler()

        previousHRV = ""

        binding.btndetectstress.setOnClickListener {
            startReadingHRVData()
        }
    }

    @SuppressLint("CheckResult")
    private fun startReadingHRVData() {
        val gatt = BleServiceHolder.bleService // Retrieve the BluetoothGatt instance from BleServiceHolder

        // Check if the gatt instance is valid
        if (gatt != null) {
            Maybe.defer {
                if (gatt.source.services.isEmpty()) gatt.discoverServices()
                else Maybe.just(gatt.source.services)
            }
                // HRV characteristic
                .flatMap { gatt.read(gatt.source.findCharacteristic(UUID.fromString("00002A19-0000-1000-8000-00805F9B34FB"))!!) }
                .map { it[0].toInt() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { hrvValue ->
                        if (hrvValue.toString() != previousHRV) {
                            hrvTextView.text = hrvValue.toString()
                            previousHRV = hrvValue.toString()
                        }
                    },
                    { error ->
                        // Handle error
                        showToast("Error reading HRV: ${error.message}")
                    }
                )
        } else {
            showToast("Starstec not connected")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}
