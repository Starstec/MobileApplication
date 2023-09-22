package com.example.starstec.ui.activity.pss

import androidx.lifecycle.ViewModel

class PssResultViewModel : ViewModel() {
    var totalStressScore: Int = 0
    lateinit var adapter: PSSAdapter
}

