package com.example.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
    fun validateEmail(email: String): Boolean
}