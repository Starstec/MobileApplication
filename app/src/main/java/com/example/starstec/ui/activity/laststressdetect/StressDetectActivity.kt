package com.example.starstec.ui.activity.laststressdetect

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.starstec.databinding.ActivityStressDetectBinding
import com.example.starstec.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StressDetectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStressDetectBinding
    private val viewModel: StressDetectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStressDetectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

//        binding.detectcard.setOnClickListener {
//            val youtubeUrl = "https://www.youtube.com/watch?v=0_nlxJvylko&ab_channel=ThePip"
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.setPackage("com.google.android.youtube")
//            startActivity(intent)
//        }

        lifecycleScope.launch {
            viewModel.loadLatestStressData()
        }

        viewModel.latestStressData.observe(this) { stressData ->
            stressData?.let {
                binding.hrv.text = it.hrv.take(2)
                binding.scr.text = it.scr.take(3)
                val hrvValue = it.hrv.take(2).toInt()
                val stressPercentage = calculateStressPercentage(hrvValue)
                binding.stresslevel.text = stressPercentage.toString()

                val hrvFocusValue = it.hrv.take(2).toInt()
                val hrvFocusPercentage = calculateFocusPercentage(hrvFocusValue)
                binding.energylevel.text = hrvFocusPercentage.toString()
            }
        }
    }

    private fun calculateStressPercentage(hrvValue: Int): Int {
        return when (hrvValue) {
            in 0..2 -> 10
            in 3..5 -> 20
            in 6..8 -> 30
            in 9..11 -> 40
            in 12..14 -> 50
            in 15..17 -> 60
            in 18..20 -> 70
            in 21..23 -> 80
            in 24..60 -> 90
            else -> 0
        }
    }

    private fun calculateFocusPercentage(hrvValue: Int): Int {
        return when {
            hrvValue in 21..40 -> 10
            hrvValue >= 19 -> 20
            hrvValue >= 17 -> 30
            hrvValue >= 15 -> 40
            hrvValue >= 13 -> 50
            hrvValue >= 11 -> 60
            hrvValue >= 9 -> 70
            hrvValue >= 7 -> 80
            hrvValue >= 5 -> 90
            else -> 100
        }
    }
}
