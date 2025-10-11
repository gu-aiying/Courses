package com.example.domain.usecases

import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String): Boolean {
        return authRepository.validateEmail(email)
    }
}