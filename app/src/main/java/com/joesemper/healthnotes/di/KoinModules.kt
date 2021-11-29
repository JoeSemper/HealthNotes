package com.joesemper.healthnotes.di

import com.joesemper.healthnotes.data.datasource.HealthDataRepositoryImpl
import com.joesemper.healthnotes.data.repository.HealthDataRepository
import com.joesemper.healthnotes.ui.compose.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<HealthDataRepository> { HealthDataRepositoryImpl() }
    viewModel { MainViewModel(get()) }
}