package com.example.starstec.ui.activity.ppgactivity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.starstec.R

class WeeklyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Add a log statement to indicate when this fragment is created
        Log.d("WeeklyFragment", "WeeklyFragment created")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weekly, container, false)
    }
}
