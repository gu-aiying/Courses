package com.example.data.repository

import android.util.Patterns
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun login(email: String, password: String): Boolean {
        // Для демо всегда возвращаем true если email валиден
        return validateEmail(email)
    }

    override fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}