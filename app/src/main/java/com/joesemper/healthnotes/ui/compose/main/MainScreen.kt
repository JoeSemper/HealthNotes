package com.joesemper.healthnotes.ui.compose.main

import androidx.compose.animation.Crossfade
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.joesemper.healthnotes.data.model.HealthData
import org.koin.androidx.compose.getViewModel
import java.util.*

@Composable
fun MainScreen(viewModel: MainViewModel = getViewModel()) {

    Scaffold(backgroundColor = Color.Transparent) {
        val catches by viewModel.currentContent.collectAsState()
        Crossfade(catches) { animatedUiState ->
            if (animatedUiState != null) {
                Text(text = "Hello Android!")
            } else {
                viewModel.addNewData(
                    HealthData(
                        pressureTop = 123,
                        pressureBottom = 321,
                        pulse = 60,
                        time = Date().time
                    )
                )
            }
        }
    }
}