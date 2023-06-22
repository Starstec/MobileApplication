package com.github.user.soilitouraplication.ui.faq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.user.soilitouraplication.R
import com.github.user.soilitouraplication.api.FaqApi
import com.github.user.soilitouraplication.api.FaqResponse
import com.github.user.soilitouraplication.api.RetrofitClient
import com.github.user.soilitouraplication.databinding.ActivityFaqBinding
import com.github.user.soilitouraplication.ui.faq.adapter.FaqAdapter
import com.github.user.soilitouraplication.ui.faq.adapter.SectionItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqActivity : AppCompatActivity() {
    private val adapter = FaqAdapter()
    
    private val faqApi: FaqApi = RetrofitClient.apiInstanceFaq()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        fetchFaq()
        
        binding.recyclerView.adapter = adapter
        
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
    
    private fun fetchFaq() {
        faqApi.getFaq().enqueue(object : Callback<FaqResponse> {
            override fun onResponse(call: Call<FaqResponse>, response: Response<FaqResponse>) {
                if (response.isSuccessful) {
                    val faqResponse = response.body()
                    val faqList = faqResponse?.data ?: emptyList()
                    
                    for (i in faqList.indices) {
                        adapter.addSectionItem(
                            SectionItem(faqList[i].question, R.color.primary, faqList[i].answer)
                        )
                    }
                } else {
                    // Handle request failure
                }
            }
            
            override fun onFailure(call: Call<FaqResponse>, t: Throwable) {
                // Handle network error
            }
        })
    }
}