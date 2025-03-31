package com.altruist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.model.User
import com.altruist.data.network.dto.auth.LoginRequest
import com.altruist.data.network.dto.auth.LoginResponse
import com.altruist.data.network.RetrofitInstance
import com.altruist.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginResult = MutableStateFlow<Result<LoginResponse>?>(null)
    val loginResult: StateFlow<Result<LoginResponse>?> = _loginResult

    val currentUser: Flow<User?> = repository.getLoggedInUser()

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onLoginClick() {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _errorMessage.value = "Por favor, completa todos los campos"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = repository.login(_email.value, _password.value)
            _loginResult.value = result
            _isLoading.value = false
        }
    }

    fun showError(message: String) {
        _errorMessage.value = message
    }

}
