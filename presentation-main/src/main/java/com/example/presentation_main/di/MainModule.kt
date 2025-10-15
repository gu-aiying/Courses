package com.example.presentation_main.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MainModule {
    // Этот модуль может быть использован для предоставления main-специфичных зависимостей
}