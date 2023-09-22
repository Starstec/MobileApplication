package com.example.starstec.ui.activity.laststressdetect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starstec.data.database.StressData
import com.example.starstec.data.repository.StressDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StressDetectViewModel @Inject constructor(private val stressDataRepository: StressDataRepository) : ViewModel() {

    private val _latestStressData = MutableLiveData<StressData?>()
    val latestStressData: LiveData<StressData?> = _latestStressData

    fun loadLatestStressData() {
        viewModelScope.launch(Dispatchers.IO) {
            _latestStressData.postValue(stressDataRepository.getLatestStressData())
        }
    }
}
