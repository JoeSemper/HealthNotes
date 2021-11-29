package com.joesemper.healthnotes.ui.compose.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joesemper.healthnotes.data.model.HealthData
import com.joesemper.healthnotes.data.repository.HealthDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val repository: HealthDataRepository): ViewModel() {

    val currentContent = MutableStateFlow<MutableList<HealthData>?>(null)

    init {
        loadAllHealthData()
    }

    fun addNewData(data: HealthData) {
        viewModelScope.launch {
            repository.addNewData(data)
        }
    }

    private fun loadAllHealthData() {
        viewModelScope.launch {
            repository.getAllHealthData().collect { contentState ->
                if (currentContent.value == null) {
                    currentContent.value = mutableListOf()
                }
                currentContent.value?.apply {
                    addAll(contentState.added)
                    removeAll(contentState.deleted)
                }
            }
        }
    }
}