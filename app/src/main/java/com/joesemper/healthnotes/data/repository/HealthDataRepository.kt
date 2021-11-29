package com.joesemper.healthnotes.data.repository

import com.joesemper.healthnotes.data.model.HealthContentState
import com.joesemper.healthnotes.data.model.HealthData
import kotlinx.coroutines.flow.Flow

interface HealthDataRepository {
    fun getAllHealthData(): Flow<HealthContentState>
    suspend fun addNewData(data: HealthData)
}