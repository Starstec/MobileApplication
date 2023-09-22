package com.example.starstec.ui.activity.edaactivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.starstec.databinding.ActivityEdaSensorBinding
import com.example.starstec.ui.activity.MainActivity

class EdaSensorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEdaSensorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEdaSensorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.edacard.setOnClickListener {
            val youtubeUrl =
                "https://www.youtube.com/watch?v=lxkNYFehwrU&ab_channel=UniversitasAirlangga"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.google.android.youtube")
            startActivity(intent)
        }
    }
}
