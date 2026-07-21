package com.vivicarmonadev.loginmvvm.repository

import com.vivicarmonadev.loginmvvm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

/**
 * Implementación concreta de AuthRepository respaldada por Firebase Authentication.
 */

class FirebaseAuthRepository (private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()): AuthRepository {

    /**
     * Propiedad calculada: cada vez que se consulta "currentUser", se ejecuta este
     * bloque get(). Pregunta a Firebase quién está logueado ahora mismo.
    */

    override val currentUser: User?
        get() = firebaseAuth.currentUser?.toDomainUser()

    /**
     * Inicia sesión con correo y contraseña.
     * "suspend" permite usar corrutinas: la función "pausa" mientras espera
     * la respuesta de Firebase, sin congelar el resto de la app.
     */

    override suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
                ?: return Result.failure(IllegalStateException("No se pudo obtener el usuario"))
            Result.success(firebaseUser.toDomainUser())
        } catch (e: Exception) {
            Result.failure(Exception(traducirError(e)))
        }
    }

    /**
     * Registra un usuario nuevo con correo y contraseña.
     * Misma lógica que signInWithEmail, pero llamando al metodo de crear Firebase
     */

    override suspend fun registerWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
                ?: return Result.failure(IllegalStateException("No se pudo crear el usuario"))
            Result.success(firebaseUser.toDomainUser())
        } catch (e: Exception) {
            Result.failure(Exception(traducirError(e)))
        }
    }

    /**
     * Inicia sesión usando una cuenta de Google.
     * @param idToken el token que entrega Google Sign-In tras el login exitoso con Google
     * (esto se obtiene desde la pantalla de Login.
     */

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = result.user
                ?: return Result.failure(IllegalStateException("No se pudo autenticar con Google"))
            Result.success(firebaseUser.toDomainUser())
        } catch (e: Exception) {
            Result.failure(Exception(traducirError(e)))
        }
    }

    /**
     * Cierra la sesión actual.
     * No es "suspend" porque esta operación es instantánea (no espera al servidor).
     */

    override fun signOut() {
        firebaseAuth.signOut()
    }

    /**
     * Función de extensión privada, "Traduce" un FirebaseUser (con los nombres fijos que define Firebase, hacia nuestro propio modelo User.
     * Ventaja: si Firebase cambiara sus nombres, o si cambiá de proveedor de autenticación, solo esta función necesita ajustarse.
     */

    private fun FirebaseUser.toDomainUser() = User(
        idu = uid,
        email = email,
        nameuser = displayName,
        photoUrl = photoUrl?.toString()
    )

    /**
     * Traduce las excepciones específicas de Firebase Auth a mensajes
     * en español, para el usuario final (en vez de mostrar el texto técnico en inglés que da Firebase por defecto).
     */

    private fun traducirError(e: Exception): String {
        return when (e) {
            // El correo no está registrado como usuario en Firebase.
            is FirebaseAuthInvalidUserException -> "No existe una cuenta con este correo"

            // La contraseña es incorrecta, o el formato del correo es inválido.
            is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos"

            // Se intentó registrar un correo que ya tiene una cuenta creada.
            is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con este correo"

            // Cualquier otro error no contemplado (sin internet, error del servidor, etc.)
            else -> "Ocurrió un error, intenta de nuevo"
        }
    }
}