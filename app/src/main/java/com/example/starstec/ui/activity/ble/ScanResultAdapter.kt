package com.example.starstec.ui.activity.ble

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.starstec.R
import com.example.starstec.ui.activity.MainActivity
import no.nordicsemi.android.support.v18.scanner.ScanResult

class ScanResultAdapter(
    private val inflater: LayoutInflater,
    private val recyclerView: RecyclerView,
) : RecyclerView.Adapter<ScanResultViewHolder>(), View.OnClickListener {

    private val scanResults = mutableListOf<ScanResult>()

    fun append(scanResult: ScanResult) {
        scanResults
            .indexOfFirst { it.device.address == scanResult.device.address }
            .takeIf { it != -1 }
            ?.let { index ->
                scanResults[index] = scanResult
                notifyItemChanged(index)
                return
            }

        scanResults += scanResult
        notifyItemInserted(scanResults.size - 1)
    }

    override fun getItemCount(): Int = scanResults.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanResultViewHolder =
        ScanResultViewHolder(
            inflater.inflate(
                R.layout.cell_scan_result, parent, false
            )
        )

    override fun onViewAttachedToWindow(holder: ScanResultViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener(this)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ScanResultViewHolder, position: Int) {
        val scanResult = scanResults[position]
        val name = scanResult.device.name ?: "N/A"
        holder.bind(name, scanResult.device.address)
    }


    override fun onViewDetachedFromWindow(holder: ScanResultViewHolder) {
        holder.itemView.setOnClickListener(null)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onClick(view: View) {
        recyclerView.context.startActivity(
            MainActivity.intent(
                recyclerView.context,
                scanResults[recyclerView.getChildAdapterPosition(view)].device
            )
        )
    }
}
