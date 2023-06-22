package com.github.user.soilitouraplication.ui.fullcampaign

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.user.soilitouraplication.databinding.ActivityDetailCampaignBinding
import com.github.user.soilitouraplication.utils.DateUtils

@Suppress("DEPRECATION")
class DetailCampaign : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCampaignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val campaignId = intent.getStringExtra("campaignId")
        val campaignTitle = intent.getStringExtra("campaignTitle")
        val campaignDescription = intent.getStringExtra("campaignDescription")
        val campaignDate = intent.getStringExtra("campaignDate")
        val campaignImage = intent.getStringExtra("campaignImage")
        // Mendapatkan data lainnya jika diperlukan

        Glide.with(this) // Ganti 'this' dengan 'requireContext()' jika kode ini berada di dalam Fragment
            .load(campaignImage)
            .centerCrop()
            .into(binding.ivcampaign)


        // Gunakan data yang diterima sesuai kebutuhan Anda
        binding.tvcampaigndate.text = campaignId
        binding.tvtitle.text = campaignTitle
        binding.tvcampaigndescription.text = campaignDescription
        binding.tvcampaigndate.text = DateUtils.formatDateTimeCampaign(campaignDate.toString())
        
        binding.textView.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        fun startActivity(context: Context, campaignId: String, campaignTitle: String) {
            val intent = Intent(context, DetailCampaign::class.java).apply {
                putExtra("campaignId", campaignId)
                putExtra("campaignTitle", campaignTitle)
            }
            context.startActivity(intent)
        }
    }
}
