package com.example.starstec.ui.activity.ppgactivity

import android.graphics.Typeface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.example.starstec.R

class DailyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi Line Chart
        val lineChart: LineChart = view.findViewById(R.id.lineChart)

        // Simulasi data (gantilah dengan data ECG yang sebenarnya)
        val entries = ArrayList<Entry>()
        for (i in 0 until 7) {
            entries.add(Entry(i.toFloat(), (Math.random() * 100).toFloat())) // Contoh data acak
        }

        // Atur data ke dalam LineDataSet
        val dataSet = LineDataSet(entries, "Heart Rate")

        // Konfigurasi LineDataSet
        dataSet.color = Color.RED // Warna garis
        dataSet.setCircleColor(Color.BLUE) // Warna titik-titik
        dataSet.lineWidth = 3f // Lebar garis
        dataSet.circleRadius = 6f // Ukuran titik-titik
        dataSet.setDrawValues(false) // Tidak menampilkan label pada titik-titik
        dataSet.disableDashedLine()
        dataSet.disableDashedHighlightLine()

        // Atur data ke dalam LineData
        val lineData = LineData(dataSet)

        // Atur LineData ke dalam LineChart
        lineChart.data = lineData

        // Konfigurasi sumbu X
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.typeface = Typeface.create("poppins", Typeface.BOLD)
        xAxis.textSize = 12f
        xAxis.textColor = Color.BLACK // Warna teks sumbu X

        // Konfigurasi sumbu Y
        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.setDrawGridLines(false) // Menghilangkan gridlines pada sumbu Y
        yAxisLeft.typeface = Typeface.create("poppins", Typeface.BOLD)
        yAxisLeft.textSize = 12f
        yAxisLeft.textColor = Color.BLACK // Warna teks sumbu Y

        // Konfigurasi latar belakang chart
        lineChart.setBackgroundColor(Color.WHITE)

        // Sembunyikan placeholder jika ada data
        if (entries.isNotEmpty()) {
            view.findViewById<View>(R.id.noDataTextView).visibility = View.GONE
        }
    }
}
