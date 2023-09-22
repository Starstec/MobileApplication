package com.example.starstec.ui.activity.pss

import androidx.lifecycle.ViewModel
import com.example.starstec.utils.PssQuestionsUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PerceivedStressScaleViewModel @Inject constructor() : ViewModel() {
    val questions = PssQuestionsUtils.questions
}
