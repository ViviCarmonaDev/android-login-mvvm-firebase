package com.vivicarmonadev.loginmvvm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vivicarmonadev.loginmvvm.ui.screens.LoginScreen
import com.vivicarmonadev.loginmvvm.ui.screens.RegisterScreen

/**
 * Define el mapa de rutas de la app: qué pantalla se muestra según en qué "ruta" (String) esté el usuario en cada momento.
 */

@Composable
fun AppNavigation() {
    // El controlador que recuerda en qué pantalla estamos y permite movernos entre ellas.
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"   // La primera pantalla que se muestra al abrir la app.
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateBackToLogin = { navController.popBackStack() }
            )
        }
    }
}