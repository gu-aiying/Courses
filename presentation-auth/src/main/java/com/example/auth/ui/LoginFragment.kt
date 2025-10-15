package com.example.auth.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.auth.databinding.FragmentLoginBinding
import com.example.design_system.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    // Объявляем callback-интерфейс
    interface LoginNavigationListener {
        fun onLoginSuccess()
    }

    private var navigationListener: LoginNavigationListener? = null

    // Прикрепляем интерфейс к Activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Проверяем, что Activity реализует наш интерфейс
        navigationListener = context as? LoginNavigationListener
            ?: throw RuntimeException("$context must implement LoginNavigationListener")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        // Email field listener
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEmailChanged(s.toString())
            }
        })

        // Password field listener
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.onPasswordChanged(s.toString())
            }
        })

        // Login button
        binding.btLogin.setOnClickListener {
            viewModel.onLoginClicked()
        }

        // Social buttons
        binding.vkButton.setOnClickListener {
            openUrlInBrowser("https://vk.com/")
        }

        binding.okButton.setOnClickListener {
            openUrlInBrowser("https://ok.ru/")
        }

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                updateUI(state)
            }
        }
    }

    private fun updateUI(state: LoginState) {
        // Update login button state
        binding.btLogin.isEnabled = viewModel.isLoginButtonEnabled()

        // Handle email validation error
        if (state.email.isNotBlank() && !state.isEmailValid) {
            binding.emailInputLayout.error = getString(R.string.email_error)
        } else {
            binding.emailInputLayout.error = null
        }

        // Handle password validation error
        if (state.password.isNotBlank() && !state.isPasswordValid) {
            binding.passwordInputLayout.error = getString(R.string.password_error)
        } else {
            binding.passwordInputLayout.error = null
        }

        // Handle login success
        if (state.loginSuccess) {
            // Navigate to main screen
            navigationListener?.onLoginSuccess() // Сообщаем Activity о успешном логине
        }

        // Handle errors
        state.errorMessage?.let { error ->
            // Show error message
            binding.emailInputLayout.error = error
        }
    }

    private fun openUrlInBrowser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, R.string.browser_error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDetach() {
        super.onDetach()
        navigationListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}