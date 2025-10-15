package com.example.auth.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {
    // Этот модуль может быть использован для предоставления auth-специфичных зависимостей
}