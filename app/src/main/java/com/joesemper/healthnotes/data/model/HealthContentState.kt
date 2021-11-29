package com.joesemper.healthnotes.data.model

data class HealthContentState(
    val added: MutableList<HealthData> = mutableListOf(),
    val deleted: MutableList<HealthData> = mutableListOf()
)

