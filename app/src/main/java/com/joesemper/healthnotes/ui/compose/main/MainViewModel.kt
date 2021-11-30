package com.joesemper.healthnotes.ui.compose.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joesemper.healthnotes.data.model.HealthData
import com.joesemper.healthnotes.data.repository.HealthDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(private val repository: HealthDataRepository): ViewModel() {
    
    val currentContent = mutableStateOf<List<HealthData>>(listOf())

    val topPressure = mutableStateOf("0")
    val bottomPressure = mutableStateOf("0")
    val pulse = mutableStateOf("0")

    init {
        loadAllHealthData()
    }

    fun addNewData() {
        viewModelScope.launch {
            repository.addNewData(
                HealthData(
                    pressureTop = topPressure.value.toInt(),
                    pressureBottom = bottomPressure.value.toInt(),
                    pulse = pulse.value.toInt(),
                    time = Date().time
                )
            )
            cleanData()
        }
    }

    private fun loadAllHealthData() {
        viewModelScope.launch {
            repository.getAllHealthData().collect { contentState ->
                currentContent.value = contentState
            }
        }
    }

    private fun cleanData() {
        topPressure.value = "0"
        bottomPressure.value = "0"
        pulse.value = "0"
    }
}