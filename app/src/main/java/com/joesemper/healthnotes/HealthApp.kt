package com.joesemper.healthnotes

import android.app.Application
import com.joesemper.healthnotes.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HealthApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@HealthApp)
            modules(
                listOf(
                    appModule
                )
            )
        }
    }
}