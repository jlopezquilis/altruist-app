package com.altruist.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.R
import com.altruist.data.model.User
import com.altruist.data.network.dto.user.CreateUserRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import com.altruist.data.repository.AuthRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
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

    private val _register4Success = MutableStateFlow(false)
    val register4Success: StateFlow<Boolean> = _register4Success

    private val _register5Success = MutableStateFlow(false)
    val register5Success: StateFlow<Boolean> = _register5Success


    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _surname = MutableStateFlow("")
    val surname: StateFlow<String> = _surname

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _gender = MutableStateFlow("")
    val gender: StateFlow<String> = _gender

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _repeatPassword = MutableStateFlow("")
    val repeatPassword: StateFlow<String> = _repeatPassword

    private val _situation = MutableStateFlow("")
    val situation: StateFlow<String> = _situation

    private val _anonymous = MutableStateFlow(false)
    val anonymous: StateFlow<Boolean> = _anonymous

    private val _profilePictureUri = MutableStateFlow<Uri?>(null)
    val profilePictureUri: StateFlow<Uri?> = _profilePictureUri

    private val _profilePictureUrlString = MutableStateFlow("")
    val profilePictureUrlString: StateFlow<String> = _profilePictureUrlString

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val usernameRegex = Regex("^[a-zA-Z][a-zA-Z0-9_.]{3,}$")

    private val _verificationCode = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCode

    private val _inputCode = MutableStateFlow("")
    val inputCode: StateFlow<String> = _inputCode

    fun onNameChange(value: String) {
        _name.value = value
        _errorMessage.value = null
    }

    fun onSurnameChange(value: String) {
        _surname.value = value
        _errorMessage.value = null
    }

    fun onUsernameChange(value: String) {
        _username.value = value
        _errorMessage.value = null
    }

    fun onGeneroChange(value: String) {
        _gender.value = value
        _errorMessage.value = null
    }

    fun showError(message: String) {
        _errorMessage.value = message
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun onEmailChange(value: String) {
        _email.value = value
        _errorMessage.value = null
    }

    fun onPasswordChange(value: String) {
        _password.value = value
        _errorMessage.value = null
    }

    fun onRepeatPasswordChange(value: String) {
        _repeatPassword.value = value
        _errorMessage.value = null
    }

    fun onInputCodeChange(value: String) {
        _inputCode.value = value
        _errorMessage.value = null
    }

    fun onSituationChange(value: String) {
        _situation.value = value
        _errorMessage.value = null
    }

    fun onAnonymousChange(value: Boolean) {
        _anonymous.value = value
        _errorMessage.value = null
    }

    fun onProfilePictureUriChange(uri: Uri?) {
        _profilePictureUri.value = uri
    }


    fun isUsernameValid(): Boolean {
        return usernameRegex.matches(_username.value)
    }

    suspend fun isEmailAvailable(email: String): Boolean {
        val exists = repository.checkEmailExists(email)
        return !exists
    }

    suspend fun isUsernameAvailable(username: String): Boolean {
        val exists = repository.checkUsernameExists(username)
        return !exists
    }


    fun isDataValid(): Boolean {
        return _name.value.isNotBlank() &&
                _surname.value.isNotBlank() &&
                isUsernameValid() &&
                _gender.value.isNotBlank()
    }

    fun isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
    }

    suspend fun onContinueFromRegister1Click() {
        when {
            _name.value.isBlank() ||
                    _surname.value.isBlank() ||
                    _username.value.isBlank() ||
                    _gender.value.isBlank() -> {
                showError("Por favor, completa todos los campos.")
            }

            !isUsernameValid() -> {
                showError(
                    "El nombre de usuario debe:\n" +
                            "- Tener al menos 4 caracteres\n" +
                            "- Empezar por letra\n" +
                            "- Solo usar letras, números, puntos o guiones bajos"
                )
            }

            !isUsernameAvailable(_username.value) -> {
                showError("El nombre de usuario ya está en uso.")
            }

            else -> {
                _register1Success.value = true
            }
        }
    }


    suspend fun onContinueFromRegister2Click() {
        when {
            _email.value.isBlank() || _password.value.isBlank() || _repeatPassword.value.isBlank() -> {
                showError("Por favor, completa todos los campos.")
            }

            !isEmailValid() -> {
                showError("El e-mail introducido no es válido.")
            }

            !isEmailAvailable(_email.value) -> {
                showError("El e-mail ya está en uso.")
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

    fun onContinueFromRegister3Click() {
        _isLoading.value = true
        if (_inputCode.value == _verificationCode.value) {
            _register3Success.value = true
        } else {
            showError("El código no es correcto.")
        }
        _isLoading.value = false
    }

    fun onContinueFromRegister4Click() {
        _register4Success.value = true
    }

    fun uploadProfileImage(
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uri = _profilePictureUri.value ?: getDefaultProfileUri(context) // 👈 Aquí el cambio clave

        val storageRef = FirebaseStorage.getInstance().reference
        val fileRef = storageRef.child("profile_pictures/${UUID.randomUUID()}.jpg")

        fileRef.putFile(uri)
            .addOnSuccessListener {
                fileRef.downloadUrl
                    .addOnSuccessListener { downloadUri ->
                        val imageUrl = downloadUri
                        _profilePictureUri.value = imageUrl
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onError("No se pudo obtener la URL")
                    }
            }
            .addOnFailureListener {
                onError("Error al subir imagen: ${it.message}")
            }
    }

    fun getDefaultProfileUri(context: Context): Uri {
        return Uri.parse("android.resource://${context.packageName}/${R.drawable.profile_pic}")
    }

    fun onContinueFromRegister5Click(context: Context) {
        _isLoading.value = true
        uploadProfileImage(
            context = context,
            onSuccess = {
                _profilePictureUrlString.value = _profilePictureUri.value.toString()
            },
            onError = { error ->
                showError(error)
            }
        )
        registerUser(
            onSuccess = {
            },
            onError = { error ->
                showError(error)
            }
        )
        _isLoading.value = false
        _register5Success.value = true
    }

    fun registerUser(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = CreateUserRequest(
            name = _name.value,
            surname = _surname.value,
            username = _username.value,
            gender = _gender.value,
            email = _email.value,
            password_hash = _password.value,
            situation = _situation.value,
            profile_picture_url = _profilePictureUrlString.value,
            anonymous = _anonymous.value
        )

        viewModelScope.launch {
            val result = repository.createUser(user)
            result.onSuccess {
                onSuccess()
            }.onFailure {
                onError("Error al registrar usuario: ${it.message}")
            }
        }
    }


    //Necesario para proteger si el usuario vuelve a la Screen anterior

    fun resetRegister1Success() {
        _register1Success.value = false
    }
    fun resetRegister2Success() {
        _register2Success.value = false
    }
    fun resetRegister3Success() {
        _register3Success.value = false
    }
    fun resetRegister4Success() {
        _register4Success.value = false
    }
    fun resetRegister5Success() {
        _register5Success.value = false
    }
}
