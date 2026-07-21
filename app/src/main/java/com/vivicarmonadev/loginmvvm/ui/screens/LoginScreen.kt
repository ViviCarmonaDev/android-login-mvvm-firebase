package com.vivicarmonadev.loginmvvm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivicarmonadev.loginmvvm.viewmodel.AuthViewModel
import com.vivicarmonadev.loginmvvm.model.AuthState
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable

fun LoginScreen(modifier: Modifier = Modifier,onNavigateToRegister: () -> Unit = {},viewModel: AuthViewModel = viewModel()){

    // Variables locales que guardan lo que el usuario escribe en cada campo.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()

    // acomoda los elementos de adentro uno debajo del otro (verticalmente).
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)   // <- fondo blanco
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,  // <- centra horizontalmente
        verticalArrangement = Arrangement.Center              // <- centra verticalmente
    ) {
        Text(text = "Iniciar sesión")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField( // campo de texto con borde.
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField( // campo de texto con borde.
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation() //la contraseña se muestre como puntos/asteriscos.
        )

        Spacer(modifier = Modifier.height(16.dp))

        // "if" dentro de Compose: solo dibuja este Text SI la condición se cumple.
        if (authState is AuthState.Error) {
            Text(
                // Necesitamos "castear" authState a Error para poder leer su .message, porque Kotlin solo sabe que es "un AuthState" en general.
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(onClick = { viewModel.signIn(email, password) },
            // Deshabilita el botón mientras está cargando, para evitar que el usuario lo presione varias veces por accidente.
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                // Spinner pequeño en vez de texto, mientras espera la respuesta de Firebase.
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text("Entrar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }

}