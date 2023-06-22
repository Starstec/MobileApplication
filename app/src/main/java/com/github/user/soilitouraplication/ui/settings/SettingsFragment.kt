package com.github.user.soilitouraplication.ui.settings

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.github.user.soilitouraplication.R
import com.github.user.soilitouraplication.database.HistoryDatabase
import com.github.user.soilitouraplication.databinding.FragmentSettingBinding
import com.github.user.soilitouraplication.ui.changepassword.ChangePassword
import com.github.user.soilitouraplication.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        initializeGoogleSignIn()
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.btnLogout.setOnClickListener {
            showDialog()
        }

        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.btnToChangePasswordScreen.setOnClickListener {
            startActivity(Intent(activity, ChangePassword::class.java))
        }
    }

    private fun showDialog() {
        val alertDialogBuilder = AlertDialog.Builder(
            activity
        )

        alertDialogBuilder
            .setMessage(R.string.confirm_logout)
            .setCancelable(true)
            .setPositiveButton(
                R.string.logout
            ) { _, _ ->
                FirebaseAuth.getInstance().signOut()
                googleSignInClient.signOut().addOnCompleteListener {
                    val database = HistoryDatabase.buildDatabase(requireContext())
                    CoroutineScope(Dispatchers.IO).launch {
                        database.historyDao().deleteAllHistory()
                        database.close()
                    }
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
            .setNegativeButton(
                R.string.cancel
            ) { dialog, _ ->
                dialog.cancel()
            }

        val alertDialog = alertDialogBuilder.create()

        alertDialog.show()
    }

    private fun initializeGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }
}


