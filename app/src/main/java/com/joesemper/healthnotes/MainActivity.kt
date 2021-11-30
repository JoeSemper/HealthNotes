package com.joesemper.healthnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.joesemper.healthnotes.ui.compose.main.MainScreen
import com.joesemper.healthnotes.ui.theme.HealthNotesTheme

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthNotesTheme {
                MainScreen()
            }
        }
    }
}