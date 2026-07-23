package com.vivicarmonadev.loginmvvm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivicarmonadev.loginmvvm.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()){

    // Preguntamos al ViewModel/Repository quién es el usuario actual,para poder saludarlo por su correo o nombre.
    val currentUser = viewModel.currentUser

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "¡Bienvenido!", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = currentUser?.email ?: "Usuario")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.signOut()
                onSignOut()
            }
        ) {
            Text("Cerrar sesión")
        }
    }
}
