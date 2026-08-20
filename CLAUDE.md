# CLAUDE.md — Contexto del proyecto ArtDaily

Este archivo resume las decisiones ya tomadas en una conversación previa con Claude
(vía claude.ai) durante el diseño de este proyecto. Léelo antes de proponer cambios
de arquitectura — la mayoría de estas decisiones ya están evaluadas y acordadas con
el usuario; si crees que alguna debería cambiar, coméntalo explícitamente en vez de
asumir.

## Qué es esta app

App Android nativa que muestra una obra de arte distinta cada día, filtrable por
periodo/siglo/movimiento/artista/museo, con un widget de pantalla de inicio (varios
widgets simultáneos, cada uno con su propia configuración independiente). Estética
minimalista, la obra es la protagonista. Favoritos e historial. Funcionamiento
offline-first.

## Decisiones ya cerradas (no las reabras sin razón)

### Fuentes de datos
- **The Met Open Access API** y **Art Institute of Chicago API** — las dos fuentes
  del MVP. Ambas CC0, sin API key, sin restricción de uso comercial. **Excepción
  puntual (2026-08-19):** el campo `description` de AIC (reseña curatorial real, texto
  editorial) SÍ está licenciado CC BY 4.0, no CC0 — el resto de los campos de AIC siguen
  siendo CC0. Decisión tomada: usarlo igual, mostrando atribución visible en la UI
  ("Art Institute of Chicago, CC BY 4.0") junto al texto. Ver `Artwork.
  descriptionAttribution` y `docs/bitacora.md` (2026-08-19).
- **Cleveland Museum of Art** — integrada (2026-08-18), mismo patrón que Met/AIC.
- **Rijksmuseum** — integrada (2026-08-18). Su API vieja (la que motivó "requiere key
  gratuita" en esta nota) se apagó el 5 de enero de 2026; la nueva
  (`data.rijksmuseum.nl`) ya NO requiere key. Se investigó primero y se pospuso por
  parecer Linked Open Data pura (JSON-LD/CIDOC-CRM, ~4000 líneas por objeto, sin
  imagen incluida — 3 saltos más para llegarle). Al retomarla se encontró un atajo real:
  pedir `?_profile=edm-framed` (representación EDM/Europeana) da todo — metadatos,
  imagen y licencia — en una sola llamada, tan simple como Met/AIC/CMA. Sin
  `movement`/`period` limpio igual que Met/CMA (ver regla de clasificación). Detalle
  completo en `docs/bitacora.md` (2026-08-18).
- **Harvard Art Museums** — descartada por ahora: sus términos dicen "uso no
  comercial únicamente" y limitan a 2500 llamadas/día.
- **Europeana / Wikimedia Commons** — descartadas como fuente primaria de imágenes
  porque la licencia varía por archivo individual, no por fuente completa.
- **Wikidata** — solo como capa de enriquecimiento/normalización futura, no como
  fuente de imágenes.

### Arquitectura general
- **Kotlin nativo**, no React Native ni Flutter — porque el widget (la feature
  central) es inherentemente nativo en cualquier framework, así que no hay ganancia
  real de "escribir una vez". Se dejó organizado pensando en una futura migración a
  Kotlin Multiplatform (KMP) si algún día se hace versión iOS, separando la lógica
  de datos en el módulo `core-model` (Kotlin puro, sin dependencias de Android).
- **MVVM / UDF** con ViewModel + StateFlow.
- **Cosecha de datos propia (harvester), no fetch en vivo desde el dispositivo.**
  Un módulo Kotlin/JVM independiente (`:harvester`, corre fuera del APK, vía
  `./gradlew :harvester:run` o en CI) llama a Met/AIC, normaliza, filtra por
  licencia/calidad, calcula un ranking, y genera:
  - `artworks.db` — SQLite pre-poblada, empaquetada en `assets/` de la app para el
    primer arranque sin red.
  - `artworks-delta-YYYYMMDD.json` — solo lo nuevo, publicado como archivo estático
    (ej. GitHub Releases) para sync incremental.
  La app **nunca llama directamente a Met/AIC**; solo lee de Room y, opcionalmente,
  sincroniza el delta JSON vía WorkManager.
- **Sin backend con estado** — todo son archivos estáticos. Se revisará esta
  decisión solo si se añade el sistema de recomendaciones (pospuesto, fuera del MVP).

### Widget
- **Jetpack Glance** (no RemoteViews clásico) para la UI del widget.
- **WorkManager** con `PeriodicWorkRequest` (~24h) para calcular la obra del día y
  actualizar el widget — no `AlarmManager` (de más bajo nivel, pensado para alarmas
  exactas) ni el `updatePeriodMillis` nativo de `AppWidgetProviderInfo` (no admite
  menos de 30 min y no es fiable para "una vez al día").
- Cada instancia de widget (`widgetId`/`GlanceId`) tiene su propia fila en
  `WidgetConfigEntity` (Room) con filtros independientes — múltiples widgets con
  distinta configuración conviven sin problema.

### Modelo de datos
Ver el modelo `Artwork` completo y los mappers de Met/AIC en el documento de la
Etapa 2 (`etapa2-diseno-arquitectura.md`, entregado por Claude). Incluye
diccionarios de normalización estáticos (`PeriodNormalizer`, `MovementNormalizer`,
`ClassificationNormalizer`) mantenidos a mano — si un valor no matchea el
diccionario, se deja `null` en vez de clasificarlo mal.

### Stack y versiones (verificadas contra developer.android.com el 2026-08-17)
- Kotlin 2.3.21, AGP 9.3.0 — **AGP 9.x trae Kotlin integrado, no se aplica el
  plugin `org.jetbrains.kotlin.android` explícitamente** (aplicarlo junto a Kotlin
  2.3.x + AGP 9.x da error de configuración). Si tu entorno local tiene otra
  versión de AGP/Kotlin, avisa antes de asumir que este detalle sigue aplicando.
- Compose BOM 2026.06.01, Material 3.
- **Room 2.8.4** (línea 2.x estable) — deliberadamente NO se usa Room 3.0 (existe,
  pero seguía en alpha con cambio de paquete grande a `androidx.room3` en el
  momento del diseño).
- Hilt 2.56.2, Retrofit 3.0.0 (con OkHttp 4.12 y converter de kotlinx.serialization,
  no Gson), Coil 3.4.0 (coordenadas `io.coil-kt.coil3`, no `io.coil-kt`), Glance
  1.1.1, WorkManager 2.11.0.
- Estas versiones pueden haber quedado desactualizadas — libera de verificarlas de
  nuevo si ha pasado tiempo desde el diseño original.

### Estructura de módulos
```
art-daily/
 ├─ core-model/   (Artwork, normalizadores, ranking — Kotlin puro)
 ├─ harvester/     (MetApi, AicApi, mappers, genera artworks.db + delta.json)
 ├─ app/
 │   ├─ data/      (Room DAOs/entities, repositorio)
 │   ├─ domain/     (SelectionEngine, casos de uso)
 │   ├─ ui/          (home, detail, filters, favorites, history, settings)
 │   ├─ widget/       (GlanceAppWidget, config activity, receiver)
 │   └─ worker/        (DailyArtworkWorker)
```

## Dónde está el proyecto en el plan de etapas

> Estado actualizado el 2026-08-19. Para el detalle día a día (qué se hizo, bugs
> encontrados, entorno configurado), ver `docs/bitacora.md` — este bloque es solo el
> resumen de alto nivel.

1. ✅ Investigación de APIs y riesgos legales — hecho.
2. ✅ Diseño de arquitectura (Room, contratos de red, normalización, harvester) — hecho.
3. ✅ Proyecto Android creado — estructura de módulos y `build.gradle.kts`.
4. ✅ Harvester real — Met + AIC, normalización, ranking, `artworks.db` + `delta.json`.
   Verificado corriendo contra las APIs reales (no simulado).
5. ✅ Base de datos (Room) en la app — entidades/DAOs/`AppDatabase`, `createFromAsset`
   con el `artworks.db` del harvester. Verificado con prueba instrumentada en emulador.
6. ✅ Filtros — `ArtworkFilter` + `ArtworkRepository` (el modelo/motor). **Falta la UI**
   de filtros (`ui/filters`) — hoy el widget se agrega sin filtro.
7. ✅ Motor de selección/anti-repetición — `SelectionEngine` + `GetArtworkOfTheDayUseCase`.
8. ✅ Widget — Glance + WorkManager + Hilt, con imagen. Probado en vivo en un emulador
   real (no solo compilado).
9. ✅ Pruebas unitarias — 55 tests (normalizadores, mappers de Met/AIC/CMA con test de
   regresión del bug de movimiento, `SelectionEngine` con fakes en memoria). UI de
   filtros ya hecha (ver punto 6). Pantalla principal (`ui/home`) también hecha:
   `MainActivity` + `HomeScreen` (imagen a pantalla completa, favoritos) — la app ya
   tiene ícono de launcher, no depende solo del widget para verse.
10. ✅ Cleveland y Rijksmuseum integradas (ver arriba). 4 fuentes en total.
11. ✅ Navegación real (no numerado originalmente, surgió en la práctica): tabs abajo
    (Hoy/Explorar/Favoritos) + pantalla de detalle compartida + click en el widget
    abre el detalle de esa obra + splash screen. Ver `docs/bitacora.md` (2026-08-18).
12. ✅ Ronda de bugs + funcionalidad nueva pedida por el usuario (2026-08-19, ver
    `docs/bitacora.md` para el detalle completo de cada uno):
    - Bug de favoritos desincronizados entre pantallas (+ el bug de navegación de fondo
      que lo causaba: `saveState`/`restoreState` del bottom nav mezclado con Detalle
      como destino compartido).
    - Botón "Ver detalles" invisible en Hoy.
    - Iconos de tabs consistentes (`material-icons-core`, no emoji).
    - Fondo de pantalla: manual (botón en Detalle) + automático opt-in (Ajustes,
      `DailyArtworkWorker`).
    - Bug nuevo encontrado por el usuario: la obra del día se re-sorteaba en cada
      apertura de la app en vez de quedar fija hasta el día siguiente.
    - Reseñas curatoriales reales de CMA/AIC (antes se usaba por error el credit line)
      + campo `creditLine` separado. AIC es CC BY 4.0 (no CC0), se muestra con
      atribución visible.
    - Traducción on-device de las reseñas (ML Kit) — opt-in, botón "Traducir" en
      Detalle.
    - Localización del texto propio de la app a inglés (`res/values-en/`) — el resto
      de idiomas cae al español.
13. ✅ **Publicación real de `artworks.db`/`delta.json` + sync** (2026-08-19): repo
    público en GitHub (`github.com/pacohurtadof/art-daily`, requirió `git init` +
    `gh auth login` del usuario — el proyecto no era un repo git hasta hoy), releases
    con `gh release create` (`harvester/publish-release.sh` automatiza la próxima
    publicación), y `ArtworkSyncService` nuevo en `:app` (primer uso real de Retrofit
    dentro de la app, no solo del harvester) que `DailyArtworkWorker` llama en cada
    corrida para bajar el `delta.json` del último release y hacerle `upsertAll` a Room.
    Verificado en vivo end-to-end. Detalle completo en `docs/bitacora.md`.
14. ✅ **Filtro por rango de años reemplaza a Museo/Siglo** (2026-08-19, pedido del
    usuario): `ArtworkFilter`/`AvailableFilterOptions` cambiaron `museum`/`century` por
    `yearFrom`/`yearTo`; `YearRangeSelector` nuevo (`RangeSlider` de Material3),
    compartido entre Explorar y la config de un widget. Filtro de museo eliminado del
    todo (no solo oculto). Room subió a v3. Verificado en vivo. Detalle en
    `docs/bitacora.md`.
15. Pendiente, ninguno bloqueante (orden sugerido, no un compromiso):
    - **Clasificar movimiento a mano, obra por obra** (2026-08-19, decisión explícita
      del usuario tras la investigación de por qué había tan pocos movimientos — ver
      punto anterior/`docs/bitacora.md`). Se evaluaron 3 caminos: diccionario
      artista→movimiento, Wikidata como fuente automática, o etiquetar obras
      individuales; el usuario eligió el tercero ("más acertado") a propósito, aunque
      sea el que más trabajo manual pide — más preciso que inferir por artista o
      fuente automática. Alcance (cuántas/cuáles obras, mecanismo de captura) sin
      definir todavía — retomar cuando el usuario lo pida.
    - Pantalla de **historial** — `HistoryDao` existe y se usa para el anti-repetición,
      pero no hay UI que lo muestre (el README original habla de "favoritos e
      historial").
    - **Ícono de launcher propio** — hoy usa el genérico del sistema
      (`AndroidManifest.xml` sin `android:icon`).
    - Traducir los *valores* de period/movement del catálogo (ej. "Barroco",
      "Impresionismo") — hoy siempre en español sea cual sea el idioma de la app,
      porque son datos normalizados (`PeriodNormalizer`/`MovementNormalizer`), no
      texto de la UI. Distinto del punto de localización ya hecho.

## Estilo de trabajo que el usuario espera

- Código real y ejecutable, no pseudocódigo, con ruta de archivo indicada.
- Verificar versiones/documentación oficial antes de fijar dependencias — no asumir
  de memoria, las librerías de Android cambian rápido.
- Ir por etapas, sin adelantarse a implementar todo de golpe.
- Priorizar mantenibilidad, offline-first, bajo consumo de batería, y facilidad
  para añadir nuevas fuentes de museos.
