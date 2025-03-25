package com.altruist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.altruist.viewmodel.LoginViewModel

//Anotación de Jetpack Compose que indica que genera parte de la UI
@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel()) { //Porque = viewModel()?
    //Se declaran variables de estado para que Compose las observe
    var email by remember { mutableStateOf("") } //Con remember, Compose guarda el valor de la variable en su memoria. Cuando se actualice la pantalla, se recordará el valor.
                                                    // mutableStateOf define una variable observable. Cuando esta cambie, redibujará la UI automáticamente
    var password by remember { mutableStateOf("") } // el BY es como un delegado. Es decir, delego en mutableStateOf() se encargará de leer y escribir su valor.
                                                        //Email y password son como wrappers para la varible que está escichando Compose (MutableState<String>)
    val loginResult by viewModel.loginResult.collectAsState()  //De nuevo, lo setea como campo observable. Cuando cambie, la UI se redibujará

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.login(email, password) }, modifier = Modifier.fillMaxWidth()) {
            Text("Iniciar sesión")
        }

        loginResult?.let { result ->
            Spacer(modifier = Modifier.height(16.dp))
            result.onSuccess {
                Text("Bienvenido/a, ${it.name}!", color = MaterialTheme.colors.primary)
            }.onFailure {
                Text("Error: ${it.message}", color = MaterialTheme.colors.error)
            }
        }
    }
}
//Mostrar loading con un isLoading = MutableStateFlow(false)