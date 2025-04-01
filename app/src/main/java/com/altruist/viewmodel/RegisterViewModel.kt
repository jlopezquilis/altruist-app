package com.altruist.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.network.dto.user.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import com.altruist.data.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _register1Success = MutableStateFlow(false)
    val register1Success: StateFlow<Boolean> = _register1Success

    private val _register2Success = MutableStateFlow(false)
    val register2Success: StateFlow<Boolean> = _register2Success

    private val _register3Success = MutableStateFlow(false)
    val register3Success: StateFlow<Boolean> = _register3Success

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre

    private val _apellidos = MutableStateFlow("")
    val apellidos: StateFlow<String> = _apellidos

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _genero = MutableStateFlow("")
    val genero: StateFlow<String> = _genero

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _repeatPassword = MutableStateFlow("")
    val repeatPassword: StateFlow<String> = _repeatPassword

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val usernameRegex = Regex("^[a-zA-Z][a-zA-Z0-9_.]{3,}$")

    private val _verificationCode = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCode

    private val _inputCode = MutableStateFlow("")
    val inputCode: StateFlow<String> = _inputCode

    fun onNombreChange(value: String) {
        _nombre.value = value
    }

    fun onApellidosChange(value: String) {
        _apellidos.value = value
    }

    fun onUsernameChange(value: String) {
        _username.value = value
    }

    fun onGeneroChange(value: String) {
        _genero.value = value
    }

    fun showError(message: String) {
        _errorMessage.value = message
    }

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onRepeatPasswordChange(value: String) {
        _repeatPassword.value = value
    }

    fun onInputCodeChange(value: String) {
        _inputCode.value = value
    }

    fun isUsernameValid(): Boolean {
        return usernameRegex.matches(_username.value)
    }

    fun isDataValid(): Boolean {
        return _nombre.value.isNotBlank() &&
                _apellidos.value.isNotBlank() &&
                isUsernameValid() &&
                _genero.value.isNotBlank()
    }

    fun isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
    }

    fun onContinueFromRegister1Click() {
        when {
            _nombre.value.isBlank() ||
                    _apellidos.value.isBlank() ||
                    _username.value.isBlank() ||
                    _genero.value.isBlank() -> {
                showError("Por favor, completa todos los campos.")
            }

            !isUsernameValid() -> {
                showError("El nombre de usuario debe:\n- Tener al menos 4 caracteres\n- Empezar por letra\n- Solo usar letras, números, puntos o guiones bajos")
            }
            else -> _register1Success.value = true
        }
    }

    fun onContinueFromRegister2Click() {
        when {
            _email.value.isBlank() || _password.value.isBlank() || _repeatPassword.value.isBlank() -> {
                showError("Por favor, completa todos los campos.")
            }

            !isEmailValid() -> {
                showError("El e-mail introducido no es válido.")
            }

            _password.value.length < 6 -> {
                showError("La contraseña debe tener al menos 6 caracteres.")
            }

            _password.value != _repeatPassword.value -> {
                showError("Las contraseñas no coinciden.")
            }

            else -> {
                generarYEnviarCodigo(_email.value)
            }
        }
    }

    fun onContinueFromRegister3Click() {
        if (_inputCode.value == _verificationCode.value) {
            _register3Success.value = true
        } else {
            showError("El código no es correcto.")
        }
    }

    fun generarYEnviarCodigo(email: String) {
        val code = (100000..999999).random().toString()
        _verificationCode.value = code

        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.sendVerificationCode(email, code)
            _isLoading.value = false
            result.onSuccess {
                _register2Success.value = true
            }.onFailure {
                showError("Error al enviar el correo: ${it.message}")
            }
        }
    }

    //Necesario para proteger si el usuario vuelve a la Screen anterior
    fun resetCodigoEnviado() {
        _register2Success.value = false
    }


}
