package com.altruist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.model.LoginRequest
import com.altruist.data.model.LoginResponse
import com.altruist.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

//Hereda de ViewModel(), en Kotlin puedes directamente indicar que quieres llamar a su constructor vacío poniendo las (). De hecho es necesario para que no de error.
class LoginViewModel : ViewModel() {

    //privado y mutable: Solo viewmodel puede modificarlo. Por nomenclatura, la privada la escribimos con _
    private val _loginResult = MutableStateFlow<Result<LoginResponse>?>(null)
    //publico y solo lectura: así la view puede observarlo pero no modificarlo
    val loginResult = _loginResult.asStateFlow()

    fun login(email: String, password: String) {
        //Se lanza coroutine que se ejecuta en segundo plano, evitando bloquear la UI
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _loginResult.value = Result.success(response.body()!!)  //La doble exclamación indica al compilador que response.body() no devolverá null, para evitar error de compilación.
                                                                            //Si devolviera null, se lanzaría un NullPointerException. Pero en principio el servidor siempre devolverá un body.
                } else {
                    _loginResult.value = Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: IOException) {
                _loginResult.value = Result.failure(e)
            } catch (e: HttpException) {
                _loginResult.value = Result.failure(e)
            }
        }
    }
}
