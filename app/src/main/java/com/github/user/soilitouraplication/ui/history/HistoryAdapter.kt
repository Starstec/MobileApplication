package com.github.user.soilitouraplication.ui.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.user.soilitouraplication.api.History
import com.github.user.soilitouraplication.databinding.HistoryItemBinding
import com.github.user.soilitouraplication.utils.DateUtils

class HistoryAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var historyList: MutableList<History> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(historyList: List<History>) {
        this.historyList = historyList.toMutableList()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeAt(position: Int) {
        historyList.removeAt(position)
        notifyDataSetChanged()
    }

    fun getData(): List<History> {
        return historyList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = historyList[position]
        holder.bind(historyItem)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    inner class ViewHolder(private val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val history = historyList[position]
                    listener.onItemClick(history)
                }
            }
        }

        fun bind(historyItem: History) {
            binding.apply {
                tvSoil.text = historyItem.soil_type
                tvDateCreated.text = DateUtils.formatDateTime(historyItem.created_at)
                valueTemperature.text = historyItem.soil_temperature.toString()
                valueMoisture.text = historyItem.soil_moisture.toString()
                valueSoilcondition.text = historyItem.soil_condition
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(history: History)
    }
}
