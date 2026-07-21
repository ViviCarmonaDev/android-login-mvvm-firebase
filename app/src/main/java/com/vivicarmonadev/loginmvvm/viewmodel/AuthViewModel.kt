package com.vivicarmonadev.loginmvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivicarmonadev.loginmvvm.model.AuthState
import com.vivicarmonadev.loginmvvm.repository.AuthRepository
import com.vivicarmonadev.loginmvvm.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para las pantallas de Login y Registro.
 *  - Expone el estado actual de la UI (AuthState) para que las pantallas lo observen.
 *  - Valida datos simples antes de llamar al repositorio (ej. campos vacíos).
 *  - Delegar TODA la lógica de autenticación al AuthRepository (nunca habla con Firebase directo).
 *
 * Por eso extiende de ViewModel(): esto le da acceso a "viewModelScope",
 * un espacio seguro para lanzar corrutinas que se cancelan automáticamente, si la pantalla se destruye (evita fugas de memoria).
 */

class AuthViewModel ( private val authRepository: AuthRepository = FirebaseAuthRepository()) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    /**
     * Se llama cuando el usuario presiona "Entrar" en LoginScreen.
     */

    fun signIn(email: String, password: String) {
        if (!isInputValid(
                email,
                password
            )
        ) return //Valida antes de llamar a Firebase, para evitar peticiones innecesarias.

        _authState.value = AuthState.Loading // La UI mostrará un spinner de carga

        // viewModelScope.launch abre una corrutina: aquí sí podemos llamar funciones "suspend" como signInWithEmail().
        viewModelScope.launch {
            val result = authRepository.signInWithEmail(email, password)

            // result.fold() es una forma elegante de manejar Result<User>: si fue éxito, ejecuta la primera lambda; si fue fallo, la segunda.
            _authState.value = result.fold(
                onSuccess = { AuthState.Success(it) },
                onFailure = { AuthState.Error(it.message ?: "Error al iniciar sesión") }
            )
        }
    }

    /**
     * Se llama cuando el usuario presiona "Registrarme" en RegisterScreen.
     */

    fun register(email: String, password: String) {
        if (!isInputValid(email, password)) return

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.registerWithEmail(email, password)
            _authState.value = result.fold(
                onSuccess = { AuthState.Success(it) },
                onFailure = { AuthState.Error(it.message ?: "Error al registrar usuario") }
            )
        }
    }

    /**
     * Se llama tras obtener el idToken de Google Sign-In
     */

    fun signInWithGoogle(idToken: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(idToken)
            _authState.value = result.fold(
                onSuccess = { AuthState.Success(it) },
                onFailure = { AuthState.Error(it.message ?: "Error al iniciar sesión con Google") }
            )
        }
    }

    fun signOut() {
        authRepository.signOut()
        _authState.value = AuthState.Idle
    }

    /**
     * Regresa el estado a Idle. Útil después de mostrar un error o tras navegar a otra pantalla, para que la próxima vez que se abra el login
     * no arrastre un estado viejo.
     */

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    /**
     * Validaciones simples del lado de la app, antes de gastar una llamada a Firebase.
     * Devuelve true si está bien; si no, actualiza el estado a Error y devuelve false.
     */
    private fun isInputValid(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Correo y contraseña son obligatorios")
            return false
        }
        if (password.length < 6) {
            _authState.value = AuthState.Error("La contraseña debe tener al menos 6 caracteres")
            return false
        }
        return true
    }
}
