package com.vivicarmonadev.loginmvvm.model

import android.os.Message

/**
 * estado de la pantalla de autenticación en un momento dado
 * reacciona a los cambios de estado sin lógica de negocio
 */

sealed class AuthState{
    object Idle: AuthState()   /** se abrió la pantalla recién */
    object Loading: AuthState() /** mostrar spinner de carga */
    data class Success(val user: User): AuthState() /** login/registro funcionó, y trae adentro el User que logró autenticarse */
    data class Error(val message: String): AuthState() /** algo falló, ejem: contraseña incorrecta */

}