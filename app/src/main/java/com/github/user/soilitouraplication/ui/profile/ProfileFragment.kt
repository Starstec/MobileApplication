package com.github.user.soilitouraplication.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.user.soilitouraplication.databinding.FragmentProfileBinding
import com.github.user.soilitouraplication.ui.changepassword.ChangePassword
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Suppress("NAME_SHADOWING")
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUser()
        toggleEdit()
    }

    private fun setUser() {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        val usedId = user?.uid

        db.collection("users").whereEqualTo("uid", usedId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val username = document.data["name"]
                    val email = document.data["email"]

                    binding.username.setText(username as String)
                    binding.email.setText(email as String)
                }
            }
    }

    private fun toggleEdit() {
        binding.btnEditProfile.setOnClickListener {
            binding.isEdit.visibility = View.VISIBLE
            binding.isView.visibility = View.GONE
            binding.usernameLayout.isClickable = true
            binding.usernameLayout.isEnabled = true
        }

        binding.btnChangePassword.setOnClickListener {
            startActivity(Intent(activity, ChangePassword::class.java))
        }

        binding.btnEdit.setOnClickListener {
            binding.btnEdit.isEnabled = false

            val db = Firebase.firestore
            val user = Firebase.auth.currentUser
            val usedId = user?.uid

            val username = binding.username.text.toString()
            val email = binding.email.text.toString()

            db.collection("users").document(usedId.toString())
                .update(
                    mapOf(
                        "name" to username,
                        "email" to email
                    )
                )
                .addOnCompleteListener {
                    binding.btnEdit.isEnabled = true
                    showDialogSuccess()
                    setUser()
                    binding.isEdit.visibility = View.GONE
                    binding.isView.visibility = View.VISIBLE
                    binding.usernameLayout.isClickable = false
                    binding.usernameLayout.isEnabled = false
                }
                .addOnFailureListener {
                    showDialogError()
                }
        }
    }

    private fun showDialogSuccess() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Success")
        dialog.setMessage("Profile updated successfully")
        dialog.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDialogError() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Error")
        dialog.setMessage("Profile failed to update")
        dialog.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
