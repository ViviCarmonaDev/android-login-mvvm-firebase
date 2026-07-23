# Android-login-mvvm-firebase
Proyecto de práctica que implementa un flujo de inicio de sesión y registro de usuarios
con Firebase Authentication, aplicando la arquitectura MVVM en Android nativo
con Kotlin y Jetpack Compose.

📖 Documentación técnica completa (arquitectura, decisiones de diseño, flujo de auth):

[![Documentación en GitBook](https://img.shields.io/badge/Docs-GitBook-3884FF?style=for-the-badge&logo=gitbook&logoColor=white)](https://v-c-myproyects.gitbook.io/android-login-mvvm-firebase/)

## ✨ Funcionalidades

- Registro de usuario con correo y contraseña
- Inicio de sesión con correo y contraseña
- Validaciones locales (campos vacíos, contraseñas coincidentes, longitud mínima)
- Manejo de estados de carga y error en la UI, con mensajes traducidos al español
- Navegación entre Login y Registro con Navigation Compose
- Arquitectura MVVM con separación clara de capas (Model, Repository, ViewModel, View)

## 🛠️ Stack técnico

| Capa | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Arquitectura | MVVM |
| Backend / Auth | Firebase Authentication |
| Concurrencia | Coroutines + StateFlow |
| Navegación | Navigation Compose |

## 📂 Estructura del proyecto
app/src/main/java/com/vivicarmonadev/loginmvvm/
├── model/ # Clases de datos y estados de UI (User, AuthState)
├── repository/ # Abstracción y acceso a Firebase Auth
├── viewmodel/ # Lógica de presentación (AuthViewModel)
├── ui/screens/ # Composables de cada pantalla (Login, Registro)
├── ui/theme/ # Tema visual de Compose
└── navigation/ # Grafo de navegación


## 🚀 Cómo correr el proyecto

### 1. Clona el repositorio
```bash
git clone https://github.com/TU-USUARIO/android-login-mvvm-firebase.git
```

### 2. Crea tu propio proyecto de Firebase
1. Ve a [Firebase Console](https://console.firebase.google.com/) y crea un proyecto nuevo.
2. Agrega una app **Android** con el package name: `com.vivicarmonadev.loginmvvm`.
3. Descarga el archivo **`google-services.json`**.
4. Colócalo en la carpeta `app/` del proyecto (mismo nivel que `build.gradle.kts` del módulo).

> ⚠️ Este archivo está en `.gitignore` a propósito: cada persona que clone el repo debe usar su propio proyecto de Firebase.

### 3. Habilita el método de autenticación
En Firebase Console → **Authentication** → **Sign-in method**, habilita:
- Correo electrónico/contraseña

(Google Sign-In está planeado pero aún no integrado en la app — ver sección "En progreso" arriba).

### 4. Abre el proyecto en Android Studio
- Sincroniza Gradle.
- Corre la app en un emulador o dispositivo físico.

## 📄 Licencia

Este proyecto está bajo la licencia MIT — úsalo libremente para practicar o como base de tus propios proyectos.
Cambios que hice respecto a la versión original:





