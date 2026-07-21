# Configuración de Firebase

Esta guía explica cómo configurar tu propio proyecto de Firebase para correr la app localmente.

## 1. Crear el proyecto en Firebase Console

1. Ve a [Firebase Console](https://console.firebase.google.com/).
2. Clic en **"Agregar proyecto"**.
3. Ponle un nombre (puede ser cualquiera, es solo organizativo).
4. Puedes desactivar Google Analytics si no lo necesitas.

## 2. Registrar la app Android

1. Dentro del proyecto, clic en el ícono de Android.
2. **Package name**: debe coincidir exactamente con el `applicationId` definido en
   `app/build.gradle.kts` del proyecto.
3. Descarga el archivo **`google-services.json`** generado.
4. Colócalo dentro de la carpeta `app/` del proyecto (mismo nivel que el
   `build.gradle.kts` del módulo).

> Este archivo está en `.gitignore` a propósito: cada persona que clone el repositorio
> debe usar su propio proyecto de Firebase.

## 3. Habilitar los métodos de autenticación

En el menú lateral de Firebase Console:

1. Ve a **Authentication** → **Sign-in method**.
2. Habilita **Correo electrónico/contraseña**.
3. Habilita **Google**:
   * Se te pedirá un correo de asistencia del proyecto.
   * Los campos "ID de cliente web" y "Secreto de cliente web" se autogeneran, no
      deben editarse manualmente.

## 4. Agregar la huella digital SHA-1 (necesario para Google Sign-In)

Google Sign-In en Android requiere una huella digital (SHA-1) que identifica la
app para que Google confíe en las solicitudes de autenticación.

1. En la raíz del proyecto, ejecuta:
   ```bash
   ./gradlew signingReport
   ```
2. Busca la sección `Variant: debug` y copia el valor de `SHA1`.
3. En Firebase Console, ve a **Configuración del proyecto** → **Tus apps** → tu app
   Android → **Agregar huella digital**.
4. Pega el SHA1 y guarda.

## 5. Dependencias necesarias en el proyecto

En el `build.gradle.kts` de la raíz:
```kotlin
plugins {
    id("com.google.gms.google-services") version "4.4.2" apply false
}
```

En el `build.gradle.kts` del módulo `app`:
```kotlin
plugins {
    id("com.google.gms.google-services")
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:34.16.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.11.0")
}
```

> Nota: se usa `firebase-auth` (sin el sufijo `-ktx`), ya que las extensiones de
> Kotlin fueron integradas directamente en el artefacto principal en versiones
> recientes del BOM de Firebase.