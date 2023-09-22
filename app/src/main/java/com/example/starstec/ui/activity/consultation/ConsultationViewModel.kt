package com.example.starstec.ui.activity.consultation

import androidx.lifecycle.ViewModel
import com.example.starstec.data.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Date

class ConsultationViewModel : ViewModel() {

    val auth: FirebaseAuth by lazy { Firebase.auth }
    private val db: FirebaseDatabase by lazy { Firebase.database }

    val messagesRef = db.reference.child(MESSAGES_CHILD)
    fun sendMessage(messageText: String) {
        val firebaseUser = auth.currentUser
        val messagesRef = db.reference.child(MESSAGES_CHILD)

        val friendlyMessage = Message(
            messageText,
            firebaseUser?.displayName.toString(),
            firebaseUser?.photoUrl.toString(),
            Date().time
        )

        messagesRef.push().setValue(friendlyMessage) { _, _ ->
            // Tangani callback setelah data dikirim
        }
    }

    companion object {
        const val MESSAGES_CHILD = "messages"
    }
}

