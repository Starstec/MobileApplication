package com.github.user.soilitouraplication.ui.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.user.soilitouraplication.api.Campaign
import com.github.user.soilitouraplication.api.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DEPRECATION")
@HiltViewModel
class HomeViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private val campaigns = MutableLiveData<List<Campaign>>()
    val isLoading = MutableLiveData<Boolean>()
    private var savedState: List<Campaign>? = null

    @RequiresApi(Build.VERSION_CODES.M)
    internal fun fetchCampaigns() {
        isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Cek apakah koneksi internet tersedia
                if (isNetworkAvailable()) {
                    val campaignApi = RetrofitClient.apiInstance()
                    val response = campaignApi.getCampaigns().execute()


                    if (response.isSuccessful) {
                        val userResponse = response.body()
                        // Jika ada data yang tersimpan, gunakan data tersebut daripada data yang diambil
                        val campaignList = savedState ?: userResponse!!.data
                        campaigns.postValue(campaignList)
                    } else {
                        Log.d("Failure", "Request failed with code: ${response.code()}")
                    }
                    isLoading.postValue(false)
                } else {
                    // Jika tidak ada koneksi internet, tampilkan loader
                    isLoading.postValue(true)
                }
            } catch (e: Exception) {
                Log.d("Failure", e.message ?: "")
            }
        }
    }

    // Fungsi untuk mengecek apakah ada koneksi internet
    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkAvailable(): Boolean {
        val context = getApplication<Application>().applicationContext
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.run {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.run {
                    return when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
        }
        return false
    }

    fun getCampaigns(): LiveData<List<Campaign>> {
        return campaigns
    }

    fun saveState(outState: Bundle) {
        // Simpan state saat ini ke ViewModel
        campaigns.value?.let {
            savedState = it
            outState.putParcelableArrayList(KEY_SAVED_STATE, ArrayList(it))
        }
    }

    fun restoreState(state: Bundle?) {
        // Mengembalikan state yang disimpan untuk digunakan nanti
        savedState = state?.getParcelableArrayList<Campaign>(KEY_SAVED_STATE)?.toList()
    }

    companion object {
        private const val KEY_SAVED_STATE = "saved_state"
    }
}
