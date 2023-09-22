package com.example.starstec.data.repository

import com.example.starstec.data.database.StressData
import com.example.starstec.data.database.dao.StressDataDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StressDataRepository @Inject constructor(private val stressDataDao: StressDataDao) {
    // Your repository methods that use the stressDataDao go here.
    suspend fun saveStressData(hr: String, hrv: String, scr: String) {
        val stressData = StressData(
            hr = hr,
            hrv = hrv,
            scr = scr,
            timestamp = System.currentTimeMillis()
        )

        // Use the stressDataDao to save data to the database
        stressDataDao.insertStressData(stressData)
    }

    suspend fun getLatestStressData(): StressData? {
        return withContext(Dispatchers.IO) {
            stressDataDao.getLatestStressData()
        }

        // Other repository methods and business logic go here.
    }
}
