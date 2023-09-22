package com.example.starstec.ui.activity.consultation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starstec.R
import com.example.starstec.data.model.Message
import com.example.starstec.databinding.ActivityConsultationBinding
import com.example.starstec.ui.activity.MainActivity
import com.firebase.ui.database.FirebaseRecyclerOptions

class ConsultationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultationBinding
    private val viewModel: ConsultationViewModel by viewModels()
    private lateinit var adapter: FirebaseMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEditText.text.toString()
            viewModel.sendMessage(messageText)
            binding.messageEditText.setText("")
        }

        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager

        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(viewModel.messagesRef, Message::class.java)
            .build()

        val firebaseUser = viewModel.auth.currentUser
        if (firebaseUser != null) {
            adapter = FirebaseMessageAdapter(options, firebaseUser.displayName)
            binding.messageRecyclerView.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        if (::adapter.isInitialized) {
            adapter.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if (::adapter.isInitialized) {
            adapter.startListening()
        }
    }
}
