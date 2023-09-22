package com.example.starstec.ui.activity.meditation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starstec.databinding.ActivityMeditationBinding
import com.example.starstec.ui.activity.MainActivity
import com.example.starstec.utils.MeditationUtils

class MeditationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMeditationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeditationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val adapter = MeditationAdapter(MeditationUtils.dataList)
        binding.rvmeditation.layoutManager = LinearLayoutManager(this)
        binding.rvmeditation.adapter = adapter
    }
}
