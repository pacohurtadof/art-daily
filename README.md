# ArtDaily

App Android nativa que muestra una obra de arte de dominio público distinta cada día,
filtrable por periodo/movimiento/artista/año, con un widget de pantalla de inicio y fondo
de pantalla automático. Offline-first: el catálogo viaja empaquetado en la app, no hace
falta red para usarla.

## Qué hace la app

### "Hoy" — la pantalla principal

Al abrir la app (o en el tab "Hoy"), se ve **una sola obra a pantalla completa** — imagen,
título, artista, fecha, museo de origen, y (cuando la fuente la tiene) una reseña
curatorial real, no una ficha técnica. Esa obra queda fija todo el día: se sortea una vez
(ponderada por un `rankScore` de calidad/interés, evitando repetir lo mostrado en los
últimos N días) y se vuelve a mostrar la misma sin importar cuántas veces se abra la app,
hasta la medianoche siguiente.

Desde ahí se puede:
- Marcarla como favorita.
- Ver el detalle completo (dimensiones, número de acceso, crédito, licencia).
- Traducir la reseña curatorial al idioma del dispositivo (on-device, sin conexión
  después de la primera vez — ver "Traducción" abajo).
- Aplicarla como fondo de pantalla manualmente.

### Explorar

Catálogo completo navegable, con filtros combinables:
- **Periodo** y **Movimiento** (multi-selección — se puede filtrar por "Impresionismo +
  Expresionismo" a la vez, por ejemplo).
- **Artista**.
- **Rango de años** (slider).

Solo se muestran pinturas y grabados (`painting`/`print`) — el catálogo cosechado incluye
otras clasificaciones (esculturas, fotografías, etc.) pero la app las excluye a propósito
en todas las pantallas salvo Favoritos (si ya se guardó una desde antes, se sigue viendo).

### Favoritos

Las obras guardadas, para volver a verlas o usarlas en la rotación de fondo de pantalla
(ver abajo).

### Widget de pantalla de inicio

Se pueden agregar **varios widgets a la vez**, cada uno con su propia configuración
independiente (periodo/movimiento/artista/rango de años/días sin repetir). Un widget
**sin filtro propio configurado** muestra literalmente la misma obra que "Hoy" — no un
sorteo aparte. Un widget **con** filtro propio tiene su propio sorteo y su propio
anti-repetición, porque su pool de candidatas puede ser distinto.

Tocar el widget abre el detalle de esa obra puntual (por id, no "lo que sea que muestre
Hoy en ese momento").

### Fondo de pantalla

- **Manual**: botón en Detalle, pregunta destino (pantalla de inicio / bloqueo / ambas)
  cada vez.
- **Automático** (opt-in en Ajustes): elige una fuente —
  - *Obra del día* (default): la misma obra que "Hoy", se actualiza a medianoche.
  - *Rotación de Favoritos*: recorre los favoritos guardados en orden, uno por día.

### Traducción on-device

Las reseñas curatoriales (Art Institute of Chicago, Cleveland Museum of Art) suelen venir
solo en inglés. Un botón "Traducir" en Detalle usa ML Kit para traducir el texto en el
dispositivo (el modelo de idioma se descarga una vez la primera vez que se usa; después
funciona sin conexión). Si el texto ya está en el idioma del dispositivo, avisa en vez de
traducir igual.

### Idioma de la app

La interfaz está en español e inglés (`res/values` / `res/values-en`); cualquier otro
idioma del sistema cae a español. Los *valores* de periodo/movimiento del catálogo (ej.
"Barroco", "Impresionismo") están normalizados y hoy siempre en español, sin importar el
idioma de la app — son datos, no texto de interfaz.

## De dónde salen las obras

Cuatro fuentes, todas de acceso abierto y sin necesidad de API key:

| Fuente | Licencia de los datos |
|---|---|
| [The Met Open Access](https://metmuseum.github.io/) | CC0 |
| [Art Institute of Chicago](https://api.artic.edu/docs/) | CC0, excepto el campo `description` (reseña curatorial), que es CC BY 4.0 — se muestra con atribución visible en la UI |
| [Cleveland Museum of Art Open Access](https://openaccess-api.clevelandart.org/) | CC0 |
| [Rijksmuseum](https://data.rijksmuseum.nl/) | CC0 |

Solo se guardan obras de dominio público / CC0, con imagen disponible, y (filtro propio de
esta app) clasificadas como pintura o grabado con año de creación conocido y posterior al
740 (o sin año conocido) — el resto se descarta en la cosecha, no solo se oculta en la app.

## Arquitectura

**"Cosecha propia, no fetch en vivo."** La app nunca llama directamente a las APIs de los
museos. Un módulo aparte (`:harvester`, corre fuera del APK) las consulta, normaliza los
datos, calcula un ranking de calidad, y genera:

- `artworks.db` — una base SQLite pre-poblada que se empaqueta en `assets/` de la app, para
  que funcione sin red desde el primer arranque.
- `artworks-delta-*.json` — solo lo nuevo/cambiado desde la última cosecha, publicado como
  [release de GitHub](https://github.com/pacohurtadof/art-daily/releases); la app lo
  descarga en segundo plano (`WorkManager`) para ir sumando obras nuevas sin necesitar una
  actualización del APK.

No hay backend con estado — todo son archivos estáticos.

### Módulos

```
art-daily/
├─ core-model/   Kotlin puro (sin Android) — el modelo Artwork, normalizadores de
│                periodo/movimiento/clasificación, el motor de ranking, y la interfaz
│                ArtworkRepository. Compartido entre :harvester y :app; pensado para una
│                futura migración a Kotlin Multiplatform si algún día hay versión iOS.
├─ harvester/    Kotlin/JVM — clientes Retrofit de las 4 APIs, mappers a Artwork, y el
│                escritor de artworks.db/delta.json. Se corre a mano (./gradlew
│                :harvester:run) o en CI, nunca desde el dispositivo.
└─ app/
    ├─ data/      Room (DAOs/entidades), repositorio, preferencias, sync del delta.json
    ├─ domain/    Casos de uso (obra del día, rotación de favoritos) y el motor de selección
    ├─ ui/        Compose: Hoy, Explorar, Favoritos, Detalle, Ajustes, componentes comunes
    ├─ widget/    Glance (UI del widget) + su Activity/ViewModel de configuración
    ├─ wallpaper/ Aplicar una imagen como fondo de pantalla real del sistema
    └─ worker/    DailyArtworkWorker — calcula la obra del día, actualiza widgets, aplica
                  el fondo automático, y sincroniza el delta.json; una corrida periódica
                  (~24h, anclada a medianoche local) más una puntual al agregar un widget
```

### Decisiones de diseño que valen la pena explicar

- **Kotlin nativo, no multiplataforma.** El widget (la feature central de la app) es
  inherentemente nativo en cualquier framework — no había ganancia real de "escribir una
  vez". `core-model` queda listo para KMP si hace falta más adelante.
- **MVVM / UDF** con `ViewModel` + `StateFlow` en toda la UI.
- **Jetpack Glance** para el widget (no `RemoteViews` clásico) + **`WorkManager`** con
  `PeriodicWorkRequest` para la actualización diaria — no `AlarmManager` (pensado para
  alarmas exactas) ni el `updatePeriodMillis` nativo de los widgets (no admite menos de 30
  min y no es confiable para "una vez al día").
- **Cada widget es independiente**: su propia fila de configuración y su propio historial
  de anti-repetición en Room, para que convivan varios con filtros distintos sin pisarse.
- **Diccionarios de normalización mantenidos a mano** (`PeriodNormalizer`,
  `MovementNormalizer`, `ClassificationNormalizer`): si el valor crudo de una fuente no
  matchea, se deja `null` en vez de clasificarlo mal.

## Stack técnico

| | |
|---|---|
| Lenguaje | Kotlin 2.3.21 |
| Build | AGP 9.3.0, KSP 2.3.11 |
| UI | Jetpack Compose (BOM 2026.06.01), Material 3 |
| Base de datos | Room 2.8.4 |
| Inyección de dependencias | Hilt 2.60.1 |
| Red | Retrofit 3.0.0 + OkHttp 4.12.0, `kotlinx.serialization` (no Gson) |
| Imágenes | Coil 3.4.0 (`io.coil-kt.coil3`) |
| Widget | Jetpack Glance 1.1.1 |
| Trabajo en segundo plano | WorkManager 2.11.2 |
| Traducción on-device | ML Kit Translate 17.0.3 + Language ID 17.0.6 |
| `minSdk` / `compileSdk` / `targetSdk` | 26 / 37 / 37 |

## Cómo correr el proyecto

```bash
# App (necesita un emulador/dispositivo conectado)
./gradlew :app:installDebug

# Tests unitarios (JVM, rápidos)
./gradlew :app:testDebugUnitTest :core-model:test :harvester:test

# Tests instrumentados (necesitan un emulador/dispositivo real)
./gradlew :app:connectedDebugAndroidTest

# Cosechar obras nuevas (modo bulk: itera varios términos de búsqueda sobre las 4 fuentes)
./gradlew :harvester:run --args="bulk 500 output/artworks.db"
```

El build de release necesita un `keystore.properties` en la raíz (gitignoreado, no incluido
en el repo) apuntando a un keystore de firma — sin eso, `assembleRelease`/`bundleRelease`
fallan con un error claro en vez de firmar con nada.

## Estado

MVP funcional, en pruebas antes de publicar en Google Play. Pendiente, sin bloquear el uso
actual: pantalla de historial (el dato ya se guarda, falta la UI), clasificación de
movimiento artístico obra por obra para ampliar la cobertura del filtro, y los trámites de
publicación en la store (política de privacidad, clasificación de contenido, testing
cerrado).
