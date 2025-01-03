# Introducción

La aplicación **PokemonApp** es una herramienta móvil diseñada para los entusiastas de Pokémon. Permite a los usuarios gestionar una lista de Pokémon capturados, explorar la Pokédex con información detallada sobre cada Pokémon y ajustar configuraciones personales como idioma y preferencias de eliminación. La aplicación combina una experiencia interactiva con una interfaz inspirada en el universo Pokémon.

## Características principales

- **Autenticación de usuarios**: Registro y inicio de sesión mediante Firebase Authentication.
- **Pokédex**: Acceso a la lista completa de Pokémon obtenida desde una API externa, con información detallada de cada Pokémon (nombre, tipos, peso, altura, etc.).
- **Lista de Pokémon capturados**: Permite agregar y gestionar Pokémon capturados, con un diseño intuitivo basado en CardViews.
- **Ajustes personalizables**: Cambia el idioma entre español e inglés, habilita o deshabilita la eliminación de Pokémon capturados y cierra sesión.
- **Interfaz inspirada en Pokémon**: Uso de colores temáticos (amarillo, rojo y azul) para una experiencia inmersiva.

## Tecnologías utilizadas

- **Firebase**: Para autenticación y almacenamiento de datos de Pokémon capturados.
- **API de Pokémon**: Para obtener la información de la Pokédex.
- **Java**: Lenguaje principal para el desarrollo de la aplicación Android.
- **RecyclerView**: Para mostrar listas interactivas de Pokémon.
- **SharedPreferences**: Para almacenar configuraciones locales, como el idioma y las preferencias de eliminación.
- **NavHostFragment**: Para la navegación fluida entre fragmentos.
- **Material Design**: Para crear una interfaz moderna y fácil de usar.

## Instrucciones de uso

### Clonar el repositorio

git clone https://github.com/manuelvargasdam/PokemonAppMvs.git

## Configuración del entorno

### Configurar Firebase:
1. Crea un proyecto en **Firebase Console**.
2. Descarga el archivo `google-services.json` y colócalo en la carpeta `app` del proyecto.
3. Habilita **Firebase Authentication** y **Realtime Database** en la consola de Firebase.

### API de Pokémon:
- No requiere configuración adicional; el código ya incluye el endpoint de la API.

### Dependencias:
- Abre el proyecto en **Android Studio** y sincroniza las dependencias en el archivo `build.gradle`.

## Ejecución

1. Abre el proyecto en **Android Studio**.
2. Conecta un dispositivo o inicia un emulador.
3. Haz clic en el botón de **Run** para compilar y ejecutar la aplicación.

## Conclusiones del desarrollador

El desarrollo de **PokemonApp** ha sido una experiencia enriquecedora. Durante el proyecto, se destacaron los siguientes aprendizajes y desafíos:

- **Integración de APIs**: Aprender a consumir y manejar datos externos desde una API para proporcionar contenido dinámico.
- **Sincronización con Firebase**: Implementar autenticación y almacenamiento en tiempo real, lo cual mejoró la gestión de datos del usuario.
- **Diseño modular**: Utilizar fragmentos y patrones modernos para mantener el código organizado y escalable.
- **Gestión de estado local**: Usar `SharedPreferences` para conservar configuraciones importantes, como el idioma y las preferencias de usuario.

El proyecto no solo ha permitido consolidar conocimientos técnicos, sino también adquirir nuevas habilidades en el desarrollo de aplicaciones móviles.
