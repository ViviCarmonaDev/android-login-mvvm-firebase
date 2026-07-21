DECISIONES TÉCNICAS

Este documento explica el porqué de algunas decisiones tomadas durante el desarrollo, a modo de registro (similar a un ADR - Architecture Decision Record).

¿Por qué Jetpack Compose en vez de XML/Views?

Compose permite describir la UI de forma declarativa y reactiva, integrándose de forma natural con StateFlow y ViewModel. Reduce el código repetitivo (boilerplate) comparado con XML + View Binding, y es el enfoque recomendado actualmente por Google para proyectos nuevos.

¿Por qué separar AuthRepository (interfaz) de FirebaseAuthRepository (implementación)?

Para que el AuthViewModel no dependa directamente del SDK de Firebase. Esto facilita sustituir la implementación en pruebas unitarias (con una implementación falsa) y permitiría cambiar de proveedor de autenticación en el futuro sin modificar la capa de presentación.

¿Por qué un modelo User propio en vez de usar FirebaseUser directamente?

FirebaseUser es una clase controlada por el SDK de Firebase, con nombres de campos fijos. Usarla directamente en el ViewModel y las pantallas acoplaría fuertemente toda la app a Firebase. Al definir un modelo User propio, cualquier cambio futuro en el proveedor de autenticación solo requiere ajustar la función de mapeo (toDomainUser()), sin tocar el resto del código.

¿Por qué sealed class AuthState en vez de variables booleanas sueltas?

Usar variables independientes (isLoading: Boolean, errorMessage: String?, etc.) permite estados contradictorios o imposibles (por ejemplo, isLoading = true junto con un errorMessage no nulo al mismo tiempo). Una sealed class garantiza que el estado de la pantalla sea siempre uno y solo uno de los casos definidos, y obliga (en tiempo de compilación) a manejar todos los casos posibles al reaccionar al estado.

¿Por qué traducir los errores de Firebase en el Repository y no en el ViewModel o la UI?

Firebase lanza excepciones específicas (FirebaseAuthInvalidUserException, etc.) que son parte de su SDK. Traducirlas en la capa de Repository mantiene esa dependencia técnica contenida en un solo lugar; el ViewModel y las pantallas solo manejan String en español, sin necesidad de conocer las clases de excepción propias de Firebase.