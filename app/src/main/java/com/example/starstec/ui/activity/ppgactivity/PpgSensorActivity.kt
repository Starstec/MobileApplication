package com.example.starstec.ui.activity.ppgactivity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.starstec.R
import com.example.starstec.databinding.ActivityBloodPressureBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PpgSensorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBloodPressureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBloodPressureBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        // Create a list of fragments
        val fragmentList = listOf(DailyFragment(), WeeklyFragment(), MonthlyFragment())

        // Create the PpgPageAdapter
        val ppgPageAdapter = PpgPageAdapter(this, fragmentList)

        // Set up the ViewPager2 with the adapter
        viewPager.adapter = ppgPageAdapter

        // Link the TabLayout with the ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Daily"
                1 -> tab.text = "Weekly"
                2 -> tab.text = "Monthly"
            }
        }.attach()

        // Terapkan gaya teks kustom pada TabLayout
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            tab?.view?.findViewById<TextView>(com.google.android.material.R.id.title)
        }
    }
}
