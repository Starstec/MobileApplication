package com.github.user.soilitouraplication.ui.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.user.soilitouraplication.api.History
import com.github.user.soilitouraplication.api.HistoryApi
import com.github.user.soilitouraplication.api.HistoryResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val historyApi: HistoryApi) : ViewModel() {
    val historyList: MutableLiveData<List<History>> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()
    
    val isSuccessDelete: MutableLiveData<Boolean> = MutableLiveData()
    
    fun fetchHistory() {
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        
        val call = historyApi.getHistory(userId = userId ?: "")
        call.enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>,
            ) {
                if (response.isSuccessful) {
                    val historyResponse = response.body()
                    
                    if (historyResponse?.data == null) {
                        historyList.value = emptyList()
                        errorLiveData.value = "History is empty"
                        return
                    } else {
                        historyResponse?.data?.let { value ->
                            historyList.value = value
                        }
                    }
                    
                } else {
                    Log.d("TAG", "onResponse: ${response.message()}")
                }
            }
            
            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                // Handle failure
                Log.e("HistoryViewModel", "Failed to fetch history: ${t.message}")
                // Tambahan: Mengirim pesan kesalahan ke LiveData atau melakukan penanganan lainnya
                errorLiveData.value = "Failed to fetch history. Please try again later."
                Log.d("TAG", "onFailure: ${t.message}")
            }
        })
    }
    
    fun deleteHistory(id: String) {
        val call = historyApi.deleteHistory(historyId = id)
        call.enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>,
            ) {
                Log.d(
                    "TAG",
                    "onResponse: ${response.body()} ${id} ${response.headers()} ${response.raw()}"
                )
                isSuccessDelete.value = response.isSuccessful
            }
            
            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                isSuccessDelete.value = false
            }
        })
    }
}
