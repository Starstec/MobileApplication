package com.github.user.soilitouraplication.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.github.user.soilitouraplication.api.Campaign
import com.github.user.soilitouraplication.databinding.CampaignItemBinding

@Suppress("DEPRECATION")
class CampaignAdapter : RecyclerView.Adapter<CampaignAdapter.ViewHolder>() {

    private val list = ArrayList<Campaign>()
    private var listener: OnItemClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setList(campaigns: List<Campaign>) {
        list.clear()
        list.addAll(campaigns)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(private val binding: CampaignItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener?.onItemClick(list[position])
                }
            }
        }

        fun bind(campaign: Campaign) {
            binding.apply {
                val width = dpToPx(itemView.context, 116)
                val height = dpToPx(itemView.context, 146)

                Glide.with(itemView)
                    .load(campaign.image)
                    .apply(RequestOptions().override(width, height).centerCrop())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivccampaign)

                tvtitle.text = campaign.name
            }
        }

        private fun dpToPx(context: Context, dp: Int): Int {
            val px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                context.resources.displayMetrics
            )
            return px.toInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            CampaignItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = minOf(list.size, 6)


    interface OnItemClickListener {
        fun onItemClick(campaign: Campaign)
    }
}
