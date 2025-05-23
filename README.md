# The Animals App
## Proyecto Android - Animales con Jetpack Compose y Retrofit

Este proyecto es una aplicación Android que muestra información sobre animales utilizando Jetpack Compose para la interfaz de usuario y Retrofit para consumir una API externa que proporciona datos sobre animales.

## Funcionalidades

- **Mostrar lista de animales**: La aplicación consulta una API y muestra una lista de animales con sus características.
- **Detalles de animales**: Al seleccionar un animal de la lista, el usuario puede ver más detalles sobre ese animal.
- **Navegación fluida**: Usando Jetpack Compose, se implementa una navegación intuitiva entre las pantallas de lista y detalles.

## Requisitos

- Android Studio (preferentemente la versión más reciente).
- Conexión a Internet para que la aplicación pueda consultar la API.

## Pasos para correr la aplicación

1. **Clonar el repositorio**:
   Si aún no lo has hecho, clona el repositorio en tu máquina local:
   ```bash
   git clone https://github.com/IanMLi/AnimalsApp_Extra.git
   ```

2. **Abrir el proyecto en Android Studio**:
   Abre Android Studio y selecciona "Open an existing project". Navega hasta la carpeta donde clonaste el repositorio.

3. **Configurar las dependencias**:
   Si es la primera vez que ejecutas el proyecto, asegúrate de que las dependencias estén descargadas. Android Studio debería hacer esto automáticamente. Si no es así, puedes ir a `File > Sync Project with Gradle Files`.

4. **Configuración de Retrofit**:
   Asegúrate de que las URL o configuraciones de Retrofit estén correctamente configuradas en la aplicación para poder acceder a la API. Esto puede estar en un archivo de configuración como `AnimalsApi.kt` o `ApiClient.kt`.

5. **Ejecutar la aplicación**:
   Conecta un dispositivo o inicia un emulador Android, y luego presiona el botón de "Run" en Android Studio para ejecutar la aplicación.

## Estructura del Proyecto

- **Composables**: Se encuentran los componentes principales de la interfaz de usuario, como la lista de animales y los detalles de cada animal.
- **Modelo de Datos**: Las clases que representan la estructura de los datos que se reciben de la API.
- **Retrofit**: La configuración de Retrofit para consumir los datos de la API.
- **Navegación**: Implementada con las herramientas de Jetpack Compose.

## Tecnologías utilizadas

- **Jetpack Compose**: Para construir las interfaces de usuario de forma declarativa.
- **Retrofit**: Para realizar las solicitudes HTTP y manejar la comunicación con la API.
- **Coroutines**: Para la ejecución asíncrona de las llamadas a la API y evitar el bloqueo del hilo principal.
