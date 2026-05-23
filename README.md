# SeriesFav Mobile 🎬

App Android minimalista para gestionar y ver tus series favoritas.
Catálogo cargado desde GitHub Gist, extracción automática de M3U8 y reproducción sin anuncios.

**Repositorio:** https://github.com/keiriblest/SeriesFavMobile

## ✨ Características
- Catálogo con pósters tipo Netflix (grid 3 columnas)
- Filtros: Todas / Vistas / Viendo / Pendiente
- Extracción automática de stream M3U8 (sin anuncios, sin interrupciones)
- Reproductor HLS nativo con ExoPlayer (Media3)
- Compatible con Chromecast para enviar a TV
- CI/CD con GitHub Actions → APK descargable en cada push

## 📦 Descargar APK compilado
1. Ve a la pestaña **Actions** del repositorio
2. Entra en el último workflow **Build Debug APK**
3. Descarga el artifact **SeriesFavMobile-debug**

## 🔗 Fuente de datos
Gist JSON: `https://gist.githubusercontent.com/keiriblest/4634024c08cf2c794b4c6aa0b68ce7e8/raw/series-datos%20(1).json`

## 🏗 Tech Stack
| Capa | Librería |
|---|---|
| UI | Jetpack Compose + Material3 |
| Navegación | Navigation Compose |
| Red | Retrofit2 + Gson |
| Imágenes | Coil |
| Reproductor | ExoPlayer Media3 HLS |
| Cast | Media3 Cast (Chromecast) |
| Extracción M3U8 | WebView + shouldInterceptRequest |
