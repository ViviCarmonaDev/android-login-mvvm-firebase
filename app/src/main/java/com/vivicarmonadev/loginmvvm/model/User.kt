package com.vivicarmonadev.loginmvvm.model

/** Modelo que representa a un usuario autentificado*/

data class User (
    val idu: String,
    val email: String?,
    val nameuser: String?,
    val photoUrl: String?
)