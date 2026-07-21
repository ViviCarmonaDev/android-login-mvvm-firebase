## FLUJO DE AUTENTICACIÓN

Inicio de sesión con correo y contraseña

1. El usuario escribe su correo y contraseña en `LoginScreen`.
2. Al presionar "Entrar", se llama a `viewModel.signIn(email, password)`.
3. `AuthViewModel` valida que los campos no estén vacíos y que la contraseña tenga
   al menos 6 caracteres. Si falla, actualiza el estado a `AuthState.Error`
   inmediatamente, sin llamar a Firebase.
4. Si la validación pasa, se actualiza el estado a `AuthState.Loading` y se llama a
   `authRepository.signInWithEmail(email, password)` dentro de una corrutina
   (`viewModelScope.launch`).
5. `FirebaseAuthRepository` llama a `firebaseAuth.signInWithEmailAndPassword(...)`,
   espera la respuesta con `.await()`, y:
   * Si es exitosa, convierte el `FirebaseUser` recibido a un `User` (modelo propio)
      y devuelve `Result.success(user)`.
   * Si falla, atrapa la excepción, la traduce a un mensaje en español mediante
      `traducirError()`, y devuelve `Result.failure(...)`.
6. `AuthViewModel` recibe el `Result` y actualiza `AuthState` a `Success` o `Error`
   según corresponda.
7. `LoginScreen`, que está observando el `StateFlow` mediante `collectAsState()`,
   se redibuja automáticamente reflejando el nuevo estado (spinner de carga,
   mensaje de error, o navegación a la siguiente pantalla).

## Registro de nuevos usuarios

Sigue el mismo flujo que el inicio de sesión, con dos diferencias:

- `RegisterScreen` incluye un campo adicional de "Confirmar contraseña", validado
  localmente en la propia pantalla (no involucra al ViewModel ni a Firebase) antes
  de llamar a `viewModel.register(...)`.
- Internamente, `FirebaseAuthRepository` llama a
  `firebaseAuth.createUserWithEmailAndPassword(...)` en vez del método de inicio
  de sesión.
 
## Manejo de errores

Firebase Auth lanza excepciones específicas según el tipo de error. El repositorio
las traduce a mensajes en español mediante un `when` que distingue:

| Excepción de Firebase | Mensaje mostrado |
|---|---|
| `FirebaseAuthInvalidUserException` | No existe una cuenta con este correo |
| `FirebaseAuthInvalidCredentialsException` | Correo o contraseña incorrectos |
| `FirebaseAuthUserCollisionException` | Ya existe una cuenta con este correo |
| Cualquier otra | Ocurrió un error, intenta de nuevo |

## Cierre de sesión

`authRepository.signOut()` llama directamente a `firebaseAuth.signOut()` (no
requiere corrutina, ya que es una operación local instantánea), y el `AuthViewModel`
reinicia el estado a `AuthState.Idle`.