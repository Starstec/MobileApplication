package com.example.starstec.utils

import com.example.starstec.R

data class MeditationData(
    val ivmeditation: Int,
    val meditationtype: String,
    val meditationdescription: String
)

object MeditationUtils {
    val dataList = listOf(
        MeditationData(R.drawable.meditationcardbg, "Musical Meditation", "Teknik meditasi ini adalah teknik yang paling populer, Saat melakukan ini, kamu perlu memerhatikan pikiran yang melewati..."),
        MeditationData(R.drawable.cardmantra, "Mindfulness Meditation", "Metode ini dilakukan dengan menggunakan suara yang berulang untuk menjernihkan pikiran. Bisa berupa kata, frasa, hingga suara..."),
        MeditationData(R.drawable.meditationcard, "Natural Meditation", "Jenis meditasi ini sangat beragam layaknya tradisi spiritual itu sendiri. Hal ini berfokus pada pengembangan dan pemahaman yang lebih...")
    )
}
