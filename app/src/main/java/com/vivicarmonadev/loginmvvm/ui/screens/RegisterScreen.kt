package com.vivicarmonadev.loginmvvm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivicarmonadev.loginmvvm.model.AuthState
import com.vivicarmonadev.loginmvvm.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onNavigateBackToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    val authState by viewModel.authState.collectAsState()

    // Se ejecuta automáticamente cada vez que "authState" cambia de valor.
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onRegisterSuccess()
            viewModel.resetState()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Crear cuenta")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mostramos el error local (contraseñas no coinciden) o el de Firebase, el que exista.
        val errorMessage = localError ?: (authState as? AuthState.Error)?.message
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (password != confirmPassword) {
                    localError = "Las contraseñas no coinciden"
                } else {
                    localError = null
                    viewModel.register(email, password)
                }
            },
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Registrarme")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateBackToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}