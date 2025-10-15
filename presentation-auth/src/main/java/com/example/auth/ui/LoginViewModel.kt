package com.example.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.LoginUseCase
import com.example.domain.usecases.ValidateEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val rawEmailInput = MutableStateFlow("")

    init {
        @OptIn(FlowPreview::class)
        rawEmailInput
            .debounce(500L)
            .distinctUntilChanged()
            .onEach { email ->
                val isEmailValid = validateEmailUseCase(email)

                _state.update { currentState ->
                    currentState.copy(
                        email = email,
                        isEmailValid = isEmailValid,
                        errorMessage = null
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEmailChanged(email: String) {
        rawEmailInput.value = email

        _state.update { it.copy(email = email) }
    }


    fun onPasswordChanged(password: String) {
        val isPasswordValid = password.isNotBlank()
        _state.update { currentState ->
            currentState.copy(
                password = password,
                isPasswordValid = isPasswordValid,
                errorMessage = null
            )
        }
    }

    fun onLoginClicked() {
        val currentState = _state.value
        if (currentState.isEmailValid && currentState.isPasswordValid && !currentState.isLoading) {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            viewModelScope.launch {
                try {
                    val success = loginUseCase(currentState.email, currentState.password)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = success,
                            errorMessage = if (!success) "Ошибка входа" else null
                        )
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Ошибка сети: ${e.message}"
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun isLoginButtonEnabled(): Boolean {
        val currentState = _state.value
        return currentState.isEmailValid && currentState.isPasswordValid && !currentState.isLoading
    }
}