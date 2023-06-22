package com.github.user.soilitouraplication.utils

object Validate {
    fun validateEmail(email: CharSequence?): String? {
        val emailPattern = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*"

        return if (email != null) {
            if (email.isEmpty()) {
                "Email tidak boleh kosong"
            } else if (!email.toString().trim().matches(emailPattern.toRegex())) {
                "Format email tidak valid"
            } else {
                null
            }
        } else {
            null
        }
    }

    fun validatePassword(password: CharSequence?): String? {
        return if (password != null) {
            if (password.isEmpty()) {
                "Password tidak boleh kosong"
            } else if (password.toString().length < 8) {
                "Password minimal 8 karakter"
            } else {
                null
            }
        } else {
            null
        }
    }
    
    fun validateConfirmPassword(password: CharSequence?, confirmPassword: CharSequence?): String? {
        return if (password != null && confirmPassword != null) {
            if (confirmPassword.isEmpty()) {
                "Konfirmasi password tidak boleh kosong"
            } else if (password.toString() != confirmPassword.toString()) {
                "Konfirmasi password tidak sama"
            } else {
                null
            }
        } else {
            null
        }
    }
}