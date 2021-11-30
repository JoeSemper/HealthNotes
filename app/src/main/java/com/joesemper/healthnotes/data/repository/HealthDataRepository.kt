package com.joesemper.healthnotes.data.repository

import com.joesemper.healthnotes.data.model.HealthData
import kotlinx.coroutines.flow.Flow

interface HealthDataRepository {
    fun getAllHealthData(): Flow<List<HealthData>>
    suspend fun addNewData(data: HealthData)
}