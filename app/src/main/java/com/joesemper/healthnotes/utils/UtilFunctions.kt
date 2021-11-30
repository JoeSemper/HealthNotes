package com.joesemper.healthnotes.utils

import com.joesemper.healthnotes.data.model.HealthData
import java.text.SimpleDateFormat
import java.util.*

fun generateNewDataId() = getRandomString(10)

fun getRandomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun getDateByMillisecondsTextMonth(ms: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
    val date = Date(ms)
    return sdf.format(date)
}

fun getTimeByMilliseconds(ms: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.US)
    val date = Date(ms)
    return sdf.format(date)
}

fun getDatesList(catches: List<HealthData>): List<String> {
    val dates = mutableListOf<String>()
    catches.sortedByDescending { it.time }.forEach { userData ->
        val date = getDateByMillisecondsTextMonth(userData.time)
        if (!dates.contains(date)) {
            dates.add(date)
        }
    }
    return dates
}