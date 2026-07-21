package com.vivicarmonadev.loginmvvm.repository

import com.vivicarmonadev.loginmvvm.model.User

/**
 * El ViewModel depende de esta interfaz, NUNCA de FirebaseAuth directamente.
 * Esto permite: Sustituir la implementación real por un Fake/Mock en pruebas unitarias.
 * Cambiar de proveedor de backend sin tocar la capa de presentación.
*/

interface AuthRepository {

    val currentUser: User?
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun registerWithEmail(email: String, password: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>

    fun signOut()
}