package com.example.starstec.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.starstec.R
import com.example.starstec.databinding.FragmentDashboardBinding
import com.example.starstec.ui.activity.consultation.ConsultationActivity
import com.example.starstec.ui.activity.edaactivity.EdaSensorActivity
import com.example.starstec.ui.activity.laststressdetect.StressDetectActivity
import com.example.starstec.ui.activity.login.LoginActivity
import com.example.starstec.ui.activity.meditation.MeditationActivity
import com.example.starstec.ui.activity.offlinedetection.OfflineDetection
import com.example.starstec.ui.activity.ppgactivity.PpgSensorActivity
import com.example.starstec.ui.activity.pss.PerceivedStressScale
import com.example.starstec.ui.activity.realtimedetection.RealtimeStressDetectionActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        currentUser = FirebaseAuth.getInstance().currentUser!!
        binding.tvuser.text = currentUser.displayName?.split(" ")?.get(0)
        Glide.with(this).load(currentUser.photoUrl).circleCrop().into(binding.ivuser)

        binding.stressdetectsensorcard.setOnClickListener {
            val intent = Intent(requireContext(), StressDetectActivity::class.java)
            startActivity(intent)
        }

        binding.ppgsensorcard.setOnClickListener {
            val intent = Intent(requireContext(), PpgSensorActivity::class.java)
            startActivity(intent)
        }

        binding.edasensorcard.setOnClickListener {
            val intent = Intent(requireContext(), EdaSensorActivity::class.java)
            startActivity(intent)
        }

        binding.realtimedetection.setOnClickListener {
            val intent = Intent(requireContext(), RealtimeStressDetectionActivity::class.java)
            startActivity(intent)
        }

        binding.meditation.setOnClickListener {
            val intent = Intent(requireContext(), MeditationActivity::class.java)
            startActivity(intent)
        }

        binding.consultation.setOnClickListener {
            val intent = Intent(requireContext(), ConsultationActivity::class.java)
            startActivity(intent)
        }

        binding.offline.setOnClickListener {
            val intent = Intent(requireContext(), OfflineDetection::class.java)
            startActivity(intent)
        }

        binding.flaslight.setOnClickListener {
            showToast("Fitur dalam pengembangan")
        }

        binding.perceived.setOnClickListener {
            val intent = Intent(requireContext(), PerceivedStressScale::class.java)
            startActivity(intent)
        }


        binding.logout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Konfirmasi")
            builder.setMessage("Apakah Anda yakin ingin keluar?")
            builder.setPositiveButton("Ya") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                googleSignInClient.signOut().addOnCompleteListener {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            builder.setNegativeButton("Tidak", null)
            val dialog = builder.create()
            dialog.show()
        }
        return view

    }
    private fun showToast(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    companion object {
        // ...
    }
}


