package com.example.starstec.ui.activity.meditation

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.starstec.R
import com.example.starstec.utils.MeditationData

@Suppress("DEPRECATION")
class MeditationAdapter(private val dataList: List<MeditationData>) :
    RecyclerView.Adapter<MeditationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.meditation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bindData(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val meditationTypeTextView: TextView = itemView.findViewById(R.id.meditationtype)
        private val meditationDescriptionTextView: TextView =
            itemView.findViewById(R.id.meditationdescription)
        private val meditationImageView: ImageView = itemView.findViewById(R.id.ivmeditation)

        init {
            itemView.setOnClickListener(this)
        }

        fun bindData(data: MeditationData) {
            meditationImageView.setImageResource(data.ivmeditation)
            meditationTypeTextView.text = data.meditationtype
            meditationDescriptionTextView.text = data.meditationdescription
        }

        override fun onClick(v: View) {
            // Get the position of the clicked item
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Get the corresponding MeditationData object
                dataList[position]

                // Create an Intent to start the desired activity based on the position
                val intent = when (position) {
                    0 -> Intent(v.context, MusicMeditationActivity::class.java)
//                    1 -> Intent(v.context, SecondActivity::class.java)
//                    2 -> Intent(v.context, ThirdActivity::class.java)
                    else -> null // Add more cases if needed for other positions
                }

                // Check if the intent is not null and start the activity
                intent?.let { v.context.startActivity(it) }
            }
        }
    }
}
