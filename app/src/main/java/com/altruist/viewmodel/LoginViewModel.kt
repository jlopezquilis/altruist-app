package com.altruist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.model.LoginRequest
import com.altruist.data.model.LoginResponse
import com.altruist.data.network.RetrofitInstance
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel : ViewModel() {

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
            try {
                val response = RetrofitInstance.api.login(LoginRequest(_email.value, _password.value))
                if (response.isSuccessful && response.body() != null) {
                    _loginResult.value = Result.success(response.body()!!)
                } else {
                    _loginResult.value = Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: IOException) {
                _loginResult.value = Result.failure(Exception("Error de red: ${e.message}"))
            } catch (e: HttpException) {
                _loginResult.value = Result.failure(Exception("Error del servidor: ${e.message}"))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun showError(message: String) {
        _errorMessage.value = message
    }

}
