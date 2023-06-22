package com.github.user.soilitouraplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.user.soilitouraplication.R
import com.github.user.soilitouraplication.database.HistoryDao
import com.github.user.soilitouraplication.databinding.ActivityMainBinding
import com.github.user.soilitouraplication.ui.detection.ClassifierActivity
import com.github.user.soilitouraplication.ui.history.HistoryFragment
import com.github.user.soilitouraplication.ui.home.HomeFragment
import com.github.user.soilitouraplication.ui.profile.ProfileFragment
import com.github.user.soilitouraplication.ui.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var historyFragment: HistoryFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var settingsFragment: SettingsFragment
    @Inject
    lateinit var historyDao: HistoryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddPhoto.setOnClickListener {
            val intent = Intent(this@MainActivity, ClassifierActivity::class.java)
            startActivity(intent)
        }

        // Initialize database and DAO

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    showHomeFragment()
                    binding.btnAddPhoto.isEnabled = true
                }
                R.id.history -> {
                    showHistoryFragment()
                    binding.btnAddPhoto.isEnabled = true
                }
                R.id.profile -> {
                    showProfileFragment()
                    binding.btnAddPhoto.isEnabled = true
                }
                R.id.settings -> {
                    showSettingsFragment()
                    binding.btnAddPhoto.isEnabled = true
                }
            }
            true
        }

        // Initialize fragments
        homeFragment = HomeFragment()
        historyFragment = HistoryFragment()
        profileFragment = ProfileFragment()
        settingsFragment = SettingsFragment()

        // Show HomeFragment initially
        if (savedInstanceState == null) {
            showHomeFragment()
        }
    }

    private fun showHomeFragment() {
        Log.d("MainActivity", "Showing HomeFragment")
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, homeFragment)
            .commit()
    }

    private fun showHistoryFragment() {
        Log.d("MainActivity", "Showing HistoryFragment")
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, historyFragment)
            .commit()
    }

    private fun showProfileFragment() {
        Log.d("MainActivity", "Showing ProfileFragment")
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, profileFragment)
            .commit()
    }

    private fun showSettingsFragment() {
        Log.d("MainActivity", "Showing SettingsFragment")
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, settingsFragment)
            .commit()
    }
}
