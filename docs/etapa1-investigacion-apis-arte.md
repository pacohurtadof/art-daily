# Etapa 1 — Investigación de APIs, arquitectura y riesgos

> Antes de escribir código. Esto responde a los puntos 1–8 de tu "Primera tarea".

## 1. Tabla comparativa de APIs de museos

| API | ¿Pública? | API key | Nº obras aprox. | Imágenes | Licencia | Dominio público | Calidad de metadatos | Límites / notas |
|---|---|---|---|---|---|---|---|---|
| **The Met (Open Access API)** | Sí, REST/JSON | **No requiere key** | ~470.000 objetos catalogados, ~406.000+ imágenes en dominio público | JPEG alta resolución, varias por objeto | **CC0** para datos e imágenes de obras en dominio público | Sí, campo `isPublicDomain` explícito por objeto | Buena: título, artista, fechas, cultura, departamento, dimensiones, `objectDate`, `period` (texto libre) | Sin límite de rate publicado formalmente, pero se recomienda uso razonable/backoff. Metadatos no siempre completos. |
| **Art Institute of Chicago (AIC)** | Sí, REST/JSON, IIIF | **No requiere key** | ~131.000 objetos catalogados, +50.000 imágenes CC0 | IIIF, alta resolución configurable | **CC0** para el resto de datos; el campo `description` es **CC-BY 4.0** (ojo, licencia distinta a la del resto) | Marcado por objeto — si no hay botón de descarga en la ficha, no es de dominio público | Muy buena, estructurada, incluye `style_titles`, `classification_titles`, `place_of_origin`, fechas de inicio/fin | API pública, paginación estándar, sin key. Es la misma API que usa su web/app oficial. |
| **Rijksmuseum** | Sí, REST/JSON | **Sí requiere key** (gratuita, autoregistro) | +800.000 objetos digitalizados, la mayoría con imagen de alta resolución | JPEG ~300 dpi | **CC0** para gran parte del catálogo; algunas obras con copyright vigente marcadas con **CC BY 4.0** o restringidas | Mayormente sí, marcado explícitamente por objeto (Public Domain / CC0 / CC BY / restringido) | Excelente para arte neerlandés y europeo (Siglo de Oro), con `principalMaker`, `dating`, `objectTypes` | Requiere key gratuita. Buen candidato para "arte holandés/flamenco" como filtro específico. |
| **Cleveland Museum of Art (CMA)** | Sí, REST/JSON | **No requiere key** | ~64.000–68.700 objetos, +37.000 imágenes | JPEG en varias resoluciones, incluida sin comprimir | **CC0** para obras marcadas como tal (`share_license_status`); resto sin imagen si hay restricción | Marcado explícitamente por objeto | Muy buena: `creation_date`, `culture`, `technique`, `department`, biografías de artistas | Sin key, sin límite publicado formalmente. Buena fuente secundaria/complementaria al Met. |
| **Harvard Art Museums** | Sí, REST/JSON | **Sí requiere key** (gratuita, autoregistro) | ~224.000 objetos | Vía IIIF | Metadatos variables por objeto; **uso no comercial únicamente** según sus términos | No siempre marcado con la claridad de Met/AIC/CMA | Buena pero "viva"/con huecos según su propia documentación | **Límite de 2.500 llamadas/día**. Términos indican **"API is for non-commercial use only"** — riesgo si el proyecto se monetiza en el futuro (revisar antes de integrar). |
| **Wikidata (SPARQL)** | Sí, servicio SPARQL público | No requiere key | Cientos de miles de pinturas modeladas como entidades | Enlaza a Wikimedia Commons | **CC0** para los datos estructurados de Wikidata | Depende de la imagen enlazada (normalmente dominio público si viene de Commons) | Metadatos muy ricos para relaciones (movimiento `P135`, género `P136`, creador `P170`, fecha `P571`) pero de calidad heterogénea (crowdsourced) | Ideal como capa de **enriquecimiento/normalización** (mapear "Impresionismo" entre idiomas y fuentes), no como fuente primaria de imágenes. |
| **Wikimedia Commons** | Sí, API REST | No requiere key (opcional para volumen alto) | Millones de imágenes | Sí, variable resolución | Licencia por archivo (CC0, PD, CC-BY-SA, etc.) — **hay que leer la licencia de cada imagen individualmente** | Variable, se debe verificar por archivo | Metadatos poco estructurados para "periodo/movimiento" (categorías de texto libre) | Buena fuente de respaldo de imágenes, pero la variabilidad de licencias por archivo la hace más arriesgada para automatizar sin revisión. |
| **Europeana** | Sí, REST/JSON | Sí requiere key (gratuita, "Personal API key"; hay "Project API key" para uso productivo) | Decenas de millones de objetos de patrimonio cultural (agregador de cientos de instituciones) | Variable: el **metadato siempre es CC0**, pero la **imagen (`edm:isShownBy`) tiene licencia propia por proveedor**, indicada con "badges" de derechos | Metadatos: CC0. Imágenes: **hay que verificar el badge de cada objeto** (Public Domain Mark, CC0, CC BY, In Copyright, etc.) | Variable por objeto — no asumir dominio público | Heterogénea porque agrega cientos de fuentes distintas mapeadas a EDM (Europeana Data Model) | Es una gran fuente de volumen y diversidad cultural (útil para "arte japonés", "arte mexicano", etc. vía sus proveedores), pero exige el filtrado de licencias más cuidadoso de todas las fuentes. |

## 2. Fuentes recomendadas para el MVP

**Prioridad 1 (arrancar con estas dos):**
1. **The Met Open Access API** — sin key, CC0 total, mejor combinación de volumen + calidad de metadatos + claridad legal. Es la fuente más segura para empezar.
2. **Art Institute of Chicago API** — sin key, CC0 (con la salvedad del campo `description` en CC-BY), muy buena estructura de datos, complementa bien al Met en cobertura (impresionismo, arte moderno).

**Prioridad 2 (añadir en la Décima Etapa / expansión):**
3. **Cleveland Museum of Art** — mismo patrón que Met/AIC (CC0, sin key), buena tercera fuente para evitar depender de solo dos.
4. **Rijksmuseum** — requiere key pero es la mejor fuente específica para "Siglo de Oro neerlandés" como categoría de descubrimiento.
5. **Wikidata** — no como fuente de imágenes primaria, sino como **capa de normalización** para mapear movimientos/periodos entre fuentes y enriquecer biografías de artistas.

**Se deja fuera del MVP (no descartadas, solo pospuestas):**
- **Harvard Art Museums**: por el límite de 2.500 req/día y, sobre todo, porque sus términos actuales limitan el uso a **no comercial**. Antes de integrarla habría que confirmar si el modelo de la app (gratuita, con o sin anuncios/compras futuras) encaja en esa cláusula.
- **Europeana**: gran potencial para diversidad cultural, pero la licencia de imagen varía objeto a objeto (no por fuente), lo que obliga a un filtrado por "badge" antes de mostrar nada. Se puede incorporar cuando el pipeline de normalización/licencias ya esté maduro.
- **Wikimedia Commons directo**: mismo problema que Europeana (licencia por archivo), más útil como respaldo puntual que como fuente masiva automatizada.

## 3. Modelo de licencias — cómo lo vamos a tratar

Tu modelo `Artwork` ya contempla `license` e `isPublicDomain`, lo cual es correcto. Reglas que aplicaremos:

- El pipeline de ingestión **solo persiste una obra si la fuente marca explícitamente** dominio público / CC0 (o CC-BY con atribución, en el caso puntual de campos como `description` de AIC).
- Nunca se infiere licencia por la antigüedad de la obra ("es del siglo XVII, debe ser libre") — se exige el campo explícito de la API.
- Se guarda `license`, `sourceApi`, `sourceUrl` y, cuando aplique, el texto de atribución requerido (AIC pide una leyenda tipo "Artista. Título, Fecha. Art Institute of Chicago" aunque no sea obligatorio legalmente bajo CC0 — lo trataremos como buena práctica, no como bloqueante).
- Riesgo identificado: los metadatos de "artista" pueden estar bajo copyright de terceros (biografías) aunque la imagen sea CC0 — se marcará el origen de cada campo de texto largo (descripción/biografía) para poder auditar esto.

## 4. Arquitectura técnica propuesta

**Stack:** Kotlin + Jetpack Compose + arquitectura MVVM (o el patrón oficial recomendado hoy, **UDF/MVI ligero sobre ViewModel + StateFlow**, que es la evolución que Google recomienda actualmente sobre MVVM clásico) + Room + Retrofit + Coroutines/Flow + Hilt + **Jetpack Glance** para el widget + WorkManager + Coil.

```
app/
 ├─ core/
 │   ├─ model/            (Artwork, ArtworkFilter, WidgetConfig — modelo común)
 │   ├─ network/           (Retrofit services por fuente: MetApi, AicApi, ClevelandApi...)
 │   ├─ mappers/           (MetMapper, AicMapper, ClevelandMapper -> Artwork)
 │   └─ database/          (Room: ArtworkEntity, FavoriteEntity, HistoryEntity, WidgetConfigEntity)
 ├─ data/
 │   ├─ repository/        (ArtworkRepository: combina fuentes, cachea, aplica filtros)
 │   └─ selection/         (SelectionEngine: random ponderado, anti-repetición)
 ├─ domain/
 │   └─ usecase/           (GetArtworkOfTheDayUseCase, GetFilteredArtworksUseCase...)
 ├─ ui/
 │   ├─ home, detail, filters, favorites, history, settings/
 │   └─ theme/
 ├─ widget/
 │   ├─ ArtWidget.kt        (GlanceAppWidget)
 │   ├─ ArtWidgetReceiver.kt
 │   └─ ArtWidgetConfigActivity.kt  (config independiente por widgetId)
 └─ worker/
     └─ DailyArtworkWorker.kt (WorkManager, periódico ~24h + re-chequeo tras reinicio)
```

**Por qué esta arquitectura:**
- **Un `Mapper` por fuente** convierte al modelo común `Artwork` — así añadir una API nueva (Rijksmuseum, Cleveland...) es implementar `Api + Mapper`, sin tocar el resto del sistema. Responde directamente a tu prioridad "facilidad para añadir nuevas APIs de museos".
- **Room como fuente de verdad local** — todo lo que la UI y el widget leen viene de Room, nunca directo de la red. Esto resuelve el requisito de funcionamiento offline de raíz, no como parche.
- **Hilt** para inyectar repositorios/DAOs tanto en la app como en el `Worker` y el `GlanceAppWidget` (Glance/WorkManager son compatibles con Hilt vía `HiltWorker` y entry points).

## 5. Decisión sobre el widget: Glance + AppWidget + WorkManager

Investigado en la documentación oficial actual de Android:

- **Jetpack Glance** es la API recomendada actualmente por Google para construir widgets con una sintaxis tipo Compose sobre `RemoteViews`, y es la que usaremos para la UI del widget.
- Glance por sí mismo **ya usa WorkManager internamente** para su ciclo de actualización (desde versiones recientes, `provideGlance` se ejecuta dentro de una sesión gestionada por WorkManager), pero para trabajo pesado (llamadas de red, consultas a Room) la documentación oficial recomienda **delegar explícitamente a WorkManager** y no bloquear el `BroadcastReceiver`, que tiene un límite estricto de ~10 segundos.
- **AlarmManager** se descarta: es de más bajo nivel, pensado para alarmas exactas sensibles a batería (no es el caso de "una vez al día"), y la documentación oficial dirige explícitamente este tipo de actualizaciones periódicas hacia WorkManager.
- El propio `AppWidgetProviderInfo.updatePeriodMillis` **no admite menos de 30 minutos** y no es fiable para "una vez al día exacta"; por eso se usa `PeriodicWorkRequest` de WorkManager (con `~24h` + margen de flex) en vez de depender de ese mecanismo nativo.

**Decisión final:** `GlanceAppWidget` (UI) + `WorkManager` (`PeriodicWorkRequest` diario que decide la obra del día, la persiste en Room/DataStore, y llama a `update()`/`updateAll()`) + `GlanceAppWidgetReceiver` (recibe el ciclo de vida del widget: creación, borrado, cambio de tamaño).

## 6. Múltiples widgets con configuración independiente

Esto es viable de forma nativa: cada instancia de widget en Android tiene un `GlanceId`/`appWidgetId` único. La arquitectura será:

- Tabla Room `WidgetConfigEntity(widgetId, period, century, movement, artistId, museumId, selectionMode, avoidRepeatDays)`.
- `ArtWidgetConfigActivity` (pantalla de configuración que Android abre automáticamente al añadir el widget) escribe esa fila asociada al `widgetId` recién creado.
- El `DailyArtworkWorker` itera sobre **todos** los `widgetId` con configuración guardada, calcula la obra del día para cada uno de forma independiente (respetando su propio historial de anti-repetición) y actualiza cada instancia por separado.
- Al eliminar un widget del home screen, `onDeleted()` del receiver borra su fila de configuración e historial asociado.

## 7. Riesgos principales

**Legales / de licencia:**
- Confiar en el campo de licencia de la fuente sin verificación cruzada → mitigado exigiendo el flag explícito de dominio público por objeto (no inferencia).
- Harvard Art Museums: cláusula de "uso no comercial" — no integrar hasta aclarar el modelo de negocio de la app.
- Wikimedia Commons/Europeana: licencia variable por archivo individual — requieren filtrado por objeto, no por fuente completa; mayor complejidad de pipeline.
- Cambios de política de una institución (una API puede cambiar sus términos) → el modelo `Artwork.license` + fecha de verificación permite auditar/purgar si una fuente cambia condiciones.

**Técnicos:**
- Datos de "periodo/movimiento" son texto libre y heterogéneo entre museos → requiere una tabla/diccionario de normalización mantenida a mano (no hay una taxonomía estándar común entre Met/AIC/Cleveland/Rijksmuseum). Este es probablemente el mayor esfuerzo de ingeniería real del proyecto, más que el widget en sí.
- Calidad desigual de metadatos (fechas ambiguas, sin `movement` explícito en muchas obras del Met) → el "Sistema de ranking" (punto 16) deberá penalizar obras con metadatos incompletos para que no se elijan como "obra del día" con datos pobres.
- Imágenes grandes → cache con Coil + política de descarga (thumbnail para widget vía `ImageRequest` con `size()`, imagen completa solo en pantalla de detalle bajo demanda o WiFi).
- Ejecutar `DailyArtworkWorker` de forma fiable tras reinicio del dispositivo y con Doze/App Standby → usar `PeriodicWorkRequest` con restricciones mínimas (sin exigir red si hay caché) y no depender de temporización exacta al segundo.

**De producto:**
- Riesgo de "obra del día" repetitiva o de baja relevancia si la colección filtrada es pequeña (ej. un artista muy específico con pocas obras en dominio público) → necesitamos definir qué pasa cuando el pool de candidatos es menor que la ventana de "no repetir en X días" (ciclo se reinicia automáticamente, se lo indicamos en el punto 5 del prompt original).

## 8. Backend: ¿necesario para el MVP?

No. El MVP puede funcionar 100% on-device: las APIs de Met/AIC se llaman directamente desde la app (CORS no aplica a apps nativas, y ninguna de las dos exige key ni backend intermediario). Razones para considerar un backend en el futuro (no en el MVP):
- Si se agregan fuentes que requieren ocultar una API key (Rijksmuseum, Harvard, Europeana) de forma más segura que en el propio APK.
- Si se quiere pre-normalizar/enriquecer con Wikidata en batch (para no golpear el endpoint SPARQL público desde miles de dispositivos) y servir un dataset ya normalizado.
- Si se añade el sistema de recomendaciones del punto 10 y se necesita agregación de datos entre usuarios (fuera del MVP, explícitamente pospuesto por ti).

Si se llega a ese punto, la recomendación sería un backend mínimo tipo *serverless* (Cloud Functions/Cloud Run) que actúe solo como proxy/normalizador cacheado, no como sistema con estado — manteniendo el bajo coste que pides.

---

**Siguiente paso propuesto:** si estás de acuerdo con las 2 fuentes prioritarias (Met + AIC) y la decisión de Glance+WorkManager, pasamos a la **Segunda Etapa: diseño detallado de la arquitectura** (esquema de Room, contratos de Retrofit, y el diccionario de normalización de periodos/movimientos) antes de tocar código en la Tercera Etapa.
