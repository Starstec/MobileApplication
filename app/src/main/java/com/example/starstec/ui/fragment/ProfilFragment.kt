package com.example.starstec.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.starstec.databinding.FragmentProfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfilFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private val auth: FirebaseAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengambil data pengguna dari Firebase Authentication
        val user = auth.currentUser
        user?.let { firebaseUser ->
            // Mendapatkan nama pengguna
            val username = firebaseUser.displayName
            binding.tvusername.text = username

            // Mengambil URL gambar profil dari objek currentUser
            val photoUrl = firebaseUser.photoUrl
            photoUrl?.let { url ->
                // Gunakan Glide untuk menampilkan gambar profil di ImageView
                Glide.with(requireContext())
                    .load(url)
                    .circleCrop()
                    .into(binding.profileimage)
            }
        }
    }
}
