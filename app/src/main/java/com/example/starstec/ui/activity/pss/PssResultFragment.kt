package com.example.starstec.ui.activity.pss

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.starstec.databinding.FragmentPssResultBinding

class PssResultFragment : Fragment() {
    private lateinit var binding: FragmentPssResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPssResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: PssResultViewModel by activityViewModels()
        val totalStressScore = viewModel.totalStressScore

        // Calculate the percentage
        val MAX_SCORE = 20// Change this to the maximum possible score
        val percentage = (totalStressScore.toFloat() / MAX_SCORE) * 100

        binding.resultTextView.text = "${percentage.toInt()}%"
    }
}
