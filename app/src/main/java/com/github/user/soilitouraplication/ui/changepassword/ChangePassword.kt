package com.github.user.soilitouraplication.ui.changepassword

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.github.user.soilitouraplication.ui.MainActivity
import com.github.user.soilitouraplication.R
import com.github.user.soilitouraplication.databinding.ActivityChangePasswordBinding
import com.github.user.soilitouraplication.utils.Validate
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class ChangePassword : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onShowBackDialog()
        }

        setupListeners()

        setupFieldValidations()
    }

    private fun setupListeners() {
        binding.btnChangePassword.setOnClickListener {
            doChangePassword()
        }
    }

    private fun setupFieldValidations() {
        binding.currentPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateCurrentPassword()
                enableButton()
            }
        }

        binding.newPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateNewPassword()
                enableButton()
            }
        }

        binding.confirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateConfirmPassword()
                enableButton()
            }
        }

        binding.currentPassword.addTextChangedListener {
            validateNewPassword()
            enableButton()
        }

        binding.newPassword.addTextChangedListener {
            validateCurrentPassword()
            enableButton()
        }

        binding.confirmPassword.addTextChangedListener {
            validateConfirmPassword()
            enableButton()
        }
    }

    private fun validateForm(): Boolean {
        return validateNewPassword() && validateCurrentPassword() && validateConfirmPassword()
    }

    private fun enableButton() {
        binding.btnChangePassword.isEnabled = validateForm()
    }

    private fun validateNewPassword(): Boolean {
        val password = binding.newPassword.text

        binding.newPasswordLayout.error = Validate.validatePassword(password)

        return binding.newPasswordLayout.error == null
    }
    private fun validateCurrentPassword(): Boolean {
        val password = binding.currentPassword.text

        binding.currentPasswordLayout.error = Validate.validatePassword(password)

        return binding.currentPasswordLayout.error == null
    }

    private fun validateConfirmPassword(): Boolean {
        val password = binding.newPassword.text
        val confirmPassword = binding.confirmPassword.text

        binding.confirmPasswordLayout.error =
            Validate.validateConfirmPassword(password, confirmPassword)

        return binding.confirmPasswordLayout.error == null
    }

    private fun onLoading(active:Boolean) {
        binding.btnChangePassword.isEnabled = !active
    }

    private fun doChangePassword() {
        onLoading(true)

        val user = FirebaseAuth.getInstance().currentUser

        user?.updatePassword(binding.newPassword.text.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onLoading(false)
                    onShowSuccessDialog()
                } else {
                    onLoading(false)
                    onShowErrorDialog()
                }

                onLoading(false)
            }
    }

    private fun onShowSuccessDialog() {
        val alertDialogBuilder = AlertDialog.Builder(
            this
        )

        alertDialogBuilder
            .setMessage(R.string.change_password_success)
            .setCancelable(false)
            .setPositiveButton(
                R.string.ok
            ) { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        val alertDialog = alertDialogBuilder.create()

        alertDialog.show()
    }

    private fun onShowErrorDialog() {
        val alertDialogBuilder = AlertDialog.Builder(
            this
        )

        alertDialogBuilder
            .setMessage(R.string.change_password_error)
            .setCancelable(false)
            .setPositiveButton(
                R.string.ok
            ) { dialog, _ ->
                dialog.cancel()
            }

        val alertDialog = alertDialogBuilder.create()

        alertDialog.show()
    }

    private fun onShowBackDialog() {
        val alertDialogBuilder = AlertDialog.Builder(
            this
        )

        alertDialogBuilder
            .setMessage(R.string.cancel_change_password_desc)
            .setCancelable(true)
            .setPositiveButton(
                R.string.yes
            ) { _, _ ->
                onBackPressed()
            }
            .setNegativeButton(
                R.string.no
            ) { dialog, _ ->
                dialog.cancel()
            }

        val alertDialog = alertDialogBuilder.create()

        alertDialog.show()
    }
}