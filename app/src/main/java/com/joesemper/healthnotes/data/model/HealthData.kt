package com.joesemper.healthnotes.data.model

data class HealthData(
    val pressureTop: Int = 0,
    val pressureBottom: Int = 0,
    val pulse: Int = 0,
    val time: Long = 0
)