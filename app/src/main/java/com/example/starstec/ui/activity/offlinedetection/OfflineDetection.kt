package com.example.starstec.ui.activity.offlinedetection

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.starstec.databinding.ActivityOfflineDetectionBinding
import com.example.starstec.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfflineDetection : AppCompatActivity() {


    private val viewModel: OfflineDetectionViewModel by viewModels()

    private lateinit var binding: ActivityOfflineDetectionBinding
    private lateinit var hrTextView: TextView
    private lateinit var hrvTextView: TextView
    private lateinit var scrTextView: TextView
    private var previousHR: String? = null
    private var previousHRV: String? = null
    private var previousSCR: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfflineDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        hrTextView = binding.hr
        hrvTextView = binding.hrv
        scrTextView = binding.scr

        binding.btndetectstress.setOnClickListener {
            if (!viewModel.isDetecting.value!! && viewModel.isButtonEnabled.value!!) {
                viewModel.startDetection()
            }
        }

        // Observe LiveData dari ViewModel untuk data HR, HRV, dan SCR
        viewModel.hr.observe(this) { hr ->
            if (hr != previousHR) {
                hrTextView.text = hr
                previousHR = hr
            }
        }
        viewModel.hrv.observe(this) { hrv ->
            if (hrv != previousHRV) {
                if (hrv == "0") {
                    hrvTextView.text = previousHRV
                } else {
                    val hrvAbbreviation = hrv.take(2)
                    hrvTextView.text = hrvAbbreviation
                    previousHRV = hrvAbbreviation
                }
            }
        }
        viewModel.scr.observe(this) { scr ->
            if (scr != previousSCR) {
                scrTextView.text = scr
                previousSCR = scr
            }
        }
    }

}
