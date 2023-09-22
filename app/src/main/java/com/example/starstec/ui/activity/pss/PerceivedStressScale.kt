package com.example.starstec.ui.activity.pss

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starstec.R
import com.example.starstec.databinding.ActivityPerceivedStressScaleBinding
import com.example.starstec.ui.activity.MainActivity
import com.example.starstec.utils.PssQuestionsUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PerceivedStressScale : AppCompatActivity() {
    private lateinit var binding: ActivityPerceivedStressScaleBinding
    private lateinit var adapter: PSSAdapter
    private val viewModel: PssResultViewModel by viewModels()
//    private val viewModel: PerceivedStressScaleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerceivedStressScaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PSSAdapter(PssQuestionsUtils.questions)
        binding.rvpss.layoutManager = LinearLayoutManager(this)
        binding.rvpss.adapter = adapter

        binding.btnCalculateTotal.setOnClickListener {
            val totalStressScore = adapter.getTotalPoints()

            if (adapter.isAllQuestionsAnswered()) {
                // Store the data in the ViewModel
                viewModel.totalStressScore = totalStressScore
                viewModel.adapter = adapter

                // Show the result fragment
                Log.d("PerceivedStressScale", "Show Result Fragment")
                showResultFragment()
            } else {
                // Show Toast if any question is not answered
                Log.d("PerceivedStressScale", "Not all questions are answered")
                Toast.makeText(this, "You must answer all questions", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    private fun showResultFragment() {
        // Pass the ViewModel to the PssResultFragment when creating a new instance
        val resultFragment = PssResultFragment()
        val fragmentContainer = findViewById<FrameLayout>(R.id.fragmentContainer)

        // Set the visibility of the fragment container to VISIBLE to make the fragment visible
        fragmentContainer.visibility = View.VISIBLE

        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, resultFragment)
        }

        // Close the fragment after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            supportFragmentManager.commit {
                remove(resultFragment)
            }
            // Set the visibility of the fragment container back to GONE after 3 seconds
            fragmentContainer.visibility = View.GONE

            recreate()
        }, 3000)
    }

}