# Bitácora — ArtDaily

## 2026-08-27 (continuación 4) — Tandas 16-24, se pasa las 1800, entra MET al pool

Continuación directa de la tanda 15 (1012 obras), sosteniendo el mismo tramo de
`rankScore` (3.0-3.99) y la misma metodología obra-por-obra. Progreso por tanda:
16→1097, 17→1220, 18→1391 (incluye las 118 obras de ilustración temprana de Winslow
Homer, siempre Realismo sin importar la fecha — a diferencia de Whistler/Manet, su
estilo no varía), 19→1524, 20→1576 (cayó a ~21%, primer bloque grande de miniaturas
persas/indias del "Tuti-nama" de CMA sin movimiento aplicable), 21→1640, 22→1711
(cluster grande de pinturas Mughal/Rajput y álbumes budistas/taoístas chinos, todos
`null`), 23→1757 (bloque de caricaturas "Vanity Fair" — Leslie Ward/Tissot/Pellegrini
— dejado `null` a propósito: es sátira de revista, no encaja en ningún movimiento del
diccionario, y mezclar por artista habría sido inconsistente), 24→**1869** (el pool
de CMA en este tramo ya estaba muy agotado de arte occidental — el rendimiento subió
de nuevo a ~45% al empezar a aparecer filas de MET en la consulta, que trae su propio
inventario sin tocar en este tramo).

Movimientos nuevos que aparecieron por primera vez en estas tandas (ya estaban en el
diccionario, solo no se habían usado): ninguno nuevo — se siguió reutilizando el set
de 29 movimientos existente. Reglas por artista aplicadas por primera vez en esta
tanda que vale la pena recordar para el futuro:
- **Rodolphe Bresdin** → Simbolismo (precursor visionario directo de Redon).
- **Frank Short, Charles Meryon, Auguste Lepère, Maxime Lalanne** → grabadores del
  "Etching Revival" británico/francés; Meryon/Lepère/Lalanne se clasificaron Realismo
  (vistas urbanas naturalistas), Frank Short se dejó `null` (revival británico propio,
  sin encaje limpio).
- **Turner, Blake, Constable (vía David Lucas), Bonington, Cotman, Fuseli, Ernst
  Fries** → Romanticismo (paisaje/visión romántica anglo-alemana).
- **Northern Mannerism** (Hans Vredeman de Vries, Philips/Philip Galle, Giorgio Ghisi,
  Adamo Scultori, Jan Muller, Matthias Zündt, Jacopo Palma il Giovane, Sodoma) →
  Manierismo — primer uso real de este movimiento del diccionario en volumen.
- **David Roberts / Louis Haghe "Egypt and Nubia"** (litografías, Haghe litografió los
  dibujos de Roberts) → Orientalismo.
- **John Rubens Smith / John Hill, "Hudson River Portfolio"** → Escuela del río
  Hudson (por tema, no solo por pertenencia del artista al círculo original).
- Se decidió dejar `null`, a propósito, todo el arte no-occidental sin escuela
  occidental clara (miniatura persa/india/mughal, pintura china de época Ming/Qing,
  ukiyo-e sí se sigue marcando pero Kano/Rinpa no) y las caricaturas de revista
  (Vanity Fair, Thomas Nast) — no porque falte tiempo, sino porque forzar un
  movimiento occidental sobre ellas sería clasificarlas mal.

**Total: 1869 obras clasificadas** en `harvester/data/movement-overrides.csv` (1826
líneas, incluye cabecera). Distribución completa: Realismo 383, Ukiyo-e 361,
Romanticismo 299, Impresionismo 182, Postimpresionismo 173, Tonalismo 124, Simbolismo
87, Nabis 52, Escuela de Barbizon 40, Escuela del río Hudson 35, Neoclasicismo 33,
Expresionismo 30, Orientalismo 27, Manierismo 17, Luminismo 9, Modernismo 5, Art
Nouveau 4, Prerrafaelismo 3, Futurismo 2, y Fauvismo/Dadaísmo/Cubismo con 1 cada uno.

Pausado a pedido del usuario tras la tanda 24. Quedan **3406 obras sin revisar** en el
tramo `rankScore` 3.0-3.99 (cma: 1494, rijks: 1469, met: 427, aic: 16) — rijks
(Rijksmuseum) todavía no se ha tocado en ninguna tanda de este tramo, sería la fuente
más nueva a explorar si el rendimiento vuelve a caer con cma/met. Retomar generando
la próxima tanda con el mismo patrón de consulta de las tandas 14-24 (ver
`/tmp/movement_batch_24.csv` como referencia de formato), excluyendo los ids de
`/tmp/reviewed_ids.txt` (efímero, se pierde entre sesiones — si no existe, hay que
reconstruirlo o aceptar que se van a re-mostrar algunos ids ya decididos, sin
problema real ya que el apply es idempotente vía `WHERE movement IS NULL`).

## 2026-08-27 (continuación 3) — Tandas 13-15, cambio de estrategia, se pasa las 1000

Tanda 13 (rankScore alto) siguió con rendimiento bajísimo (14/250, ~5.6% — casi toda
Rembrandt/grabadores renacentistas). El usuario propuso probar un tramo distinto:
**bajar el piso de `rankScore` de 4.0 a 3.0**, en vez de seguir agotando la cola del
tramo alto. Cambio de estrategia acertado — el rendimiento subió fuerte:
- Tanda 14 (rankScore 3.0-3.99): 140/250 (~56%) — obra gráfica completa de Manet y
  Whistler (con la misma distinción por fecha de antes: Whistler temprano/pre-1870 =
  Realismo, tardío = Tonalismo), Charles-Émile Jacque y Alphonse Legros (Barbizon/
  Realismo), decenas más de ukiyo-e.
- Tanda 15: 179/250 (~72%) — las tres series completas de grabados de Goya (Caprichos,
  Desastres de la Guerra, Tauromaquia, las tres a Romanticismo) y el resto casi completo
  del corpus de grabados de Whistler.

**Total: 1012 obras clasificadas** (pasó las 1000) en 22 movimientos. Distribución:
Ukiyo-e 235, Realismo 150, Romanticismo 132, Impresionismo 106, Tonalismo 99, Simbolismo
67, Postimpresionismo 60, Nabis 46, Escuela del río Hudson 27, Escuela de Barbizon 22,
Expresionismo 20, Neoclasicismo 18, Luminismo 9, Orientalismo 6, Modernismo 5,
Prerrafaelismo 3, Manierismo 2, y 4 con 1 obra cada uno.

**Lección para retomar**: cuando el rendimiento caiga mucho en un tramo de `rankScore`,
probar un tramo más bajo en vez de seguir agotando la cola del actual — la composición
del catálogo varía bastante entre tramos (el tramo 3.0-3.99 tiene mucha más obra
gráfica de artistas ya identificados, en vez de solo Rembrandt/Renacimiento alemán).

## 2026-08-27 (continuación 2) — Tandas 9-12, pausada de nuevo

Retomado tras el commit del README/catálogo. Tandas 9, 10, 11 y 12 agregaron 36 + 54 +
66 + 21 = 177 obras más, para un total de **679 obras clasificadas** en 22 movimientos.
La tanda 12 (mayoría Rijksmuseum, Siglo de Oro holandés) tuvo un rendimiento mucho más
bajo (21/250, ~8%) que las anteriores — señal clara de que se está entrando a la parte
del catálogo donde casi todo corresponde a periodo (Barroco/Renacimiento), no movimiento.

Distribución acumulada: Ukiyo-e 188, Impresionismo 93, Realismo 93, Simbolismo 62,
Postimpresionismo 59, Nabis 46, Romanticismo 39, Escuela del río Hudson 25, Expresionismo
19, Neoclasicismo 17, Luminismo 9, Escuela de Barbizon 6, Tonalismo 8, Modernismo 5,
Prerrafaelismo 3, Orientalismo 2, Manierismo 2, Futurismo 1, Fauvismo 1, Dadaísmo 1,
Cubismo 1, Art Nouveau 1.

Pausado de nuevo a pedido del usuario, con la recomendación explícita de pausar dado el
rendimiento decreciente. Mismo mecanismo para retomar (ver entrada de abajo).

## 2026-08-27 (continuación) — Clasificación de movimiento, tandas 6-9, pausada de nuevo

Retomado tras reconectar el celular (se instaló primero el lote pendiente de la pausa
anterior — confirmado en el dispositivo, mismo tamaño de archivo). Tandas 6, 7 y 8
agregaron 35 + 46 + 52 = 133 obras más, para un total de **502 obras clasificadas**.
Distribución acumulada: Ukiyo-e 147, Impresionismo 71, Realismo 68, Simbolismo 53,
Postimpresionismo 48, Romanticismo 19, Escuela del río Hudson 19, Nabis 18, Expresionismo
15, Neoclasicismo 13, Tonalismo 8, Luminismo 6, Modernismo 5, Escuela de Barbizon 4,
Prerrafaelismo 2, Orientalismo 2, Manierismo 2, Fauvismo 1, Art Nouveau 1.

Pausado de nuevo a pedido del usuario — quedan ~2629 obras sin revisar en el tramo de
`rankScore` alto, con rendimiento cada vez más bajo (predominan Chen Hongshou, Min Zhen,
Dürer, Schongauer, y series enteras de pintura china/japonesa tradicional que
correctamente no tienen movimiento en este modelo). Mismo mecanismo para retomar: ver la
entrada de abajo.

## 2026-08-26 — Clasificación de movimiento obra por obra (en curso, pausada)

Tras la expansión del catálogo, el usuario probó filtrar por "Impresionismo" en Explorar y
vio solo 27 obras — investigado: de las 4 fuentes, **solo AIC** trae dato de movimiento
limpio (44 de sus 138 obras); Met/CMA/Rijksmuseum (9946 obras) no traen ninguno. Se
descartó (a propósito, decisión del usuario) un diccionario artista→movimiento automático:
un mismo artista puede cambiar de movimiento a mitad de carrera (ej. Matisse pasa de
Fauvismo ~1906 a un estilo posterior sin categoría clara hacia 1921). Se eligió clasificar
**obra por obra**, priorizando por `rankScore` descendente (las que más se muestran en la
app primero).

**Mecanismo**: `harvester/data/movement-overrides.csv` (versionado, `artworkId,movement`),
aplicado por `MovementOverrides.kt` en el harvester como fallback cuando
`MovementNormalizer` no encontró nada — durable, sobrevive a futuras cosechas.

**Diccionario de movimientos ampliado** (`MovementNormalizer`, pedido explícito del
usuario — "los actuales no son todos los que existieron"): Simbolismo, Ukiyo-e, Escuela
del río Hudson, Luminismo, Tonalismo, Escuela de Barbizon, Prerrafaelismo, Nabis,
Precisionismo, Orientalismo, Escuela Ashcan.

**Dos bugs reales de normalización encontrados y arreglados de paso**:
- El matching por substring de `PeriodNormalizer`/`MovementNormalizer` no respetaba
  límites de palabra — verificado en vivo contra la API real de AIC: dos Delacroix con
  `style_titles = [..., "romantic"]` quedaban con `period = "Antigua Roma"` porque "roman"
  calzaba dentro de "roman**tic**". Fix: regex con `\b...\b` + el match más largo/específico
  gana entre varios candidatos.
- `Neoclasicismo`/`Romanticismo` estaban duplicados en `PeriodNormalizer` Y
  `MovementNormalizer` (el propio comentario de la clase decía que no debían mezclarse) —
  sacados de `PeriodNormalizer`, quedan solo como movimiento.

**Progreso a la pausa** (5 tandas, ~900 obras revisadas de mayor a menor `rankScore`):
**369 obras clasificadas** en 17 movimientos (Ukiyo-e 93, Realismo 59, Impresionismo 59,
Simbolismo 39, Postimpresionismo 37, Nabis 17, Escuela del río Hudson 12, Neoclasicismo 11,
Romanticismo 9, Expresionismo 9, Tonalismo 7, Modernismo 5, Luminismo 4, Escuela de
Barbizon 3, Orientalismo 2, Manierismo 2, Fauvismo 1). Aplicado a
`app/src/main/assets/artworks.db`, pendiente de instalar en el celular (se desconectó a
mitad de la sesión) y de commitear/pushear (no pedido todavía).

**Quedan ~3400 obras sin revisar** solo en el nivel de `rankScore` más alto (y ~8200 en
total) — la mayoría van a seguir correctamente en `null` (Renacimiento/Barroco/arte
tradicional chino-coreano-japonés no-ukiyo-e, ya cubiertos por `period`, no por
`movement`). Pausado a pedido del usuario — retomar generando la próxima tanda con:
```sql
SELECT id, title, artistName, creationDateText, creationYearStart, museum, sourceApi, rankScore, period
FROM artworks WHERE classification IN ('painting','print') AND movement IS NULL
ORDER BY rankScore DESC, id LIMIT 250;
```
(excluyendo los ids que ya aparecen en `harvester/data/movement-overrides.csv`, para no
revisar dos veces los que ya se clasificaron — los que se dejaron en `null` a propósito no
tienen esa protección todavía, pueden reaparecer y está bien, se vuelven a decidir igual).

## 2026-08-25 — Tres bugs reales, ícono nuevo, y expansión del catálogo

### Tres bugs reportados por el usuario

1. **El fondo de pantalla automático no cambiaba a medianoche.**
   `DailyArtworkWorker.schedulePeriodic` armaba un `PeriodicWorkRequest` de 24h sin
   `setInitialDelay` — la primera corrida (y por lo tanto todas las siguientes, cada ~24h
   desde ahí) quedaba anclada a la hora en la que se llamó por primera vez (ej. la hora en
   que se abrió la app la primera vez), nunca a medianoche. Fix: `setInitialDelay` calculado
   hasta la próxima medianoche local (`millisUntilNextLocalMidnight`, `internal` y con `now`
   inyectable para poder testearlo sin depender del reloj real). Se renombró el trabajo único
   (`daily_artwork_worker_v2`, cancelando el nombre viejo) para forzar que los dispositivos ya
   instalados recojan el nuevo horario en vez de seguir con el anclaje viejo.
2. **La imagen del widget a veces no aparecía.** `WidgetImageDownloader.downloadToFile`
   tragaba cualquier excepción de red en silencio y el worker igual reportaba
   `Result.success()` — un hipo de red dejaba el widget sin imagen hasta la corrida
   siguiente (~24h después), sin reintento. Fix: si había una imagen que descargar y la
   descarga falló, el worker devuelve `Result.retry()` en vez de `Result.success()`.
3. **El widget mostraba una obra distinta a la de "Hoy".** Cada widget (aunque no tuviera
   filtro propio configurado) tenía su propia fila de historial por `widgetId`, así que
   hacía su propio sorteo aleatorio independiente del de "Hoy" (`widgetId=0`), aunque el
   pool de candidatas fuera idéntico. Fix: un widget SIN filtro propio ahora comparte la
   misma clave de historial que "Hoy" (`GetArtworkOfTheDayUseCase`, `HOME_HISTORY_KEY`) —
   literalmente la misma obra, no solo "una obra parecida". Un widget CON filtro propio
   sigue con su historial independiente (su pool puede ser distinto). El fondo automático
   con fuente "obra del día" usa la misma llamada (`widgetId=0`) que "Hoy", así que queda
   cubierto por construcción.

**Tests nuevos**: `DailyArtworkWorkerSchedulingTest` (unitario, la cuenta de medianoche),
2 tests nuevos en `GetArtworkOfTheDayUseCaseTest` (widget sin filtro = misma obra que Hoy;
widget con filtro = historial independiente), y `DailyArtworkFlowTest` (instrumentado, Room
real en memoria — no el `artworks.db` real del dispositivo, para no contaminar el historial
real de quien corra los tests) probando el cruce de día y la consistencia Hoy/widget de
punta a punta contra SQL real, no fakes.

### Ícono de launcher nuevo

El usuario compartió una nueva imagen (foto de un lienzo con impasto beige/crema y una "A"
naranja) para reemplazar el ícono anterior (estilo Monet, puente/estanque). Mismo proceso
que la vez pasada: recorte al borde real del lienzo (excluyendo el telón de fondo gris del
estudio y la sombra), fondo del ícono adaptativo muestreado de ese telón (`#E4E4E4`, antes
`#FCFCFC`), arte insertado al 70% del lienzo de 108dp, set completo regenerado (adaptativo +
legacy + `_round` con máscara circular real vía Pillow) en las 5 densidades. Fuente nueva en
`docs/assets/app_icon_source.png` (reemplaza la anterior). Instalado y verificado en vivo.

### Expansión del catálogo hacia ~10MB

Pedido del usuario: subir la cantidad de obras sin que la app pase de 50MB. Antes de tocar
el harvester, se midió el tamaño real de la app (no asumido): el APK universal de debug/
release pesa 87/72MB, pero **no por las imágenes** (nunca se guardan en el `.db`, son URLs) —
sino por `libtranslate_jni.so` de ML Kit, 11-17MB por arquitectura de CPU, empaquetado 4
veces (arm64/armv7/x86/x86_64) = 64MB. Como Play Store reparte por ABI (`.aab`), lo que un
usuario real baja es ~28-30MB (arm64) — dejando ~20MB reales de margen antes de los 50MB.
Con ese dato, el usuario eligió apuntar a ~10MB/~10.000 obras (recomendado, sobre 20MB/20k)
para dejar margen de sobra.

Cambios en `:harvester` (`Main.kt`):
- **Filtro de elegibilidad** (`isEligibleForCatalog`): de ahora en más solo se guarda
  `classification IN ('painting','print')` y `creationYearStart == null || >= 740` — igual
  que ya filtra la app (`ArtworkDao`/`ArtworkRepositoryImpl`), para no seguir guardando
  peso muerto que nunca se muestra. Se podaron a mano, con el mismo criterio, las 871 obras
  no-painting/print que ya estaban en `harvester/output/artworks.db` (quedó en 1031 obras,
  975KB — confirma que casi la mitad de lo cosechado hasta ahora era peso muerto).
- **`BULK_QUERY_TERMS`** ampliado de 30 a ~190 términos (más sujetos/escenas + apellidos de
  pintores conocidos — nunca movimientos/periodos, esos los sigue derivando
  `PeriodNormalizer`/`MovementNormalizer` de los campos propios de cada fuente).
- **Bloqueo real encontrado en vivo**: la primera corrida (con `BATCH_SIZE` subido a 250)
  disparó un bloqueo temporal del WAF de Met (Incapsula — no un 403 normal de la API, un
  bot-mitigation con `incident_id`), cientos de fallos seguidos. Se cortó a mano y se
  esperó; un `curl` de prueba a los pocos minutos ya respondía 200 normal, confirmando que
  fue el volumen de la corrida, no un cambio permanente de la API. Fix: `BATCH_SIZE` bajado
  a 150, delay por ítem subido de 150ms a 300ms, y un circuit breaker nuevo (corta la ronda
  de esa fuente/término tras 5 fallos seguidos en vez de insistir contra un bloqueo activo).
  También se envolvieron las 4 búsquedas iniciales en try/catch — antes un solo término
  fallido podía tumbar toda la corrida de horas.
- Cosecha (`bulk 10000`) relanzada en segundo plano con estos fixes — en curso al momento de
  este commit, sin bloqueos. Falta, cuando termine: copiar el `artworks.db` resultante a
  `app/src/main/assets/`, publicar el release en GitHub (`harvester/publish-release.sh`), y
  verificar el tamaño final de la app instalada.

## 2026-08-21 (continuación) — Primer build de release firmado, camino a Play Store

El usuario ya creó la cuenta de Google Play Console. Primeros pasos técnicos:

**Firma**: keystore de upload generado (`~/.android/art-daily-keystore/upload-keystore.jks`,
RSA 2048, validez 10000 días, PKCS12 — por eso store/key password terminan siendo la
misma, PKCS12 no soporta que sean distintas). Contraseñas + alias en
`keystore.properties` en la raíz del repo, **gitignoreado** (igual que `*.jks`/
`*.keystore`, por las dudas — el .jks real vive fuera del repo de todos modos, este es un
repo público). `app/build.gradle.kts` lee ese archivo si existe y arma
`signingConfigs.release`; sin el archivo, el build de release falla con un error claro en
vez de firmar con nada silenciosamente.

**Verificado de verdad, no solo compilado**: `bundleRelease` + `assembleRelease` compilan,
pero eso NO garantiza que R8 no rompa algo en tiempo de ejecución — así que se instaló el
`.apk` de release (firmado, minificado) en el emulador y se probó a mano:
- Bug real encontrado: **ML Kit (traducción + detección de idioma) crasheaba** con
  `NoSuchMethodException` en las clases `*Registrar` (`LanguageIdRegistrar`,
  `NaturalLanguageTranslateRegistrar`, etc.) — ML Kit las descubre reflexivamente en
  tiempo de ejecución vía nombres declarados en el manifest, y las reglas de ProGuard que
  traen sus propios `.aar` (`com.google.mlkit:common/translate/language-id`) NO cubren
  este caso (se verificó leyendo el `proguard.txt` real empaquetado en cada uno). Fix:
  `-keep class com.google.mlkit.** { *; }` en `app/proguard-rules.pro`. Sin probar el
  release de verdad, esto no se hubiera notado hasta que un usuario tocara "Traducir" en
  producción.
- Multi-selección en Explorar, filtro de pinturas, wallpaper (WorkManager+Hilt) y Room con
  el fix de listas — todo probado en el release firmado, sin crashes.

**Pendiente para publicar** (no bloqueante para seguir developando, orden sugerido):
política de privacidad pública, formulario de "data safety" de Play Console, clasificación
de contenido (el catálogo tiene desnudos artísticos — Met/AIC clásico), ficha de la tienda
(capturas, feature graphic), y el requisito de Google de 20+ testers en testing cerrado
durante 14 días para cuentas nuevas antes de habilitar producción.

## 2026-08-21 — Multi-selección en Explorar, filtro de solo pinturas, rotación de fondo por Favoritos

Tres pedidos del usuario en la misma sesión:

**1. Multi-selección de Period/Movement en Explorar** ("¿podemos seleccionar varias
opciones en el filtro? como impresionismo y expresionismo"). Confirmado con el usuario:
multi-select en Period y Movement, solo en Explorar — la config de un widget nuevo se
queda single-select como estaba. `ArtworkFilter.period`/`movement` (`String?`) pasaron a
`periods`/`movements` (`List<String>?`); `FilterSection` (compartido con
`ArtWidgetConfigActivity`) pasó de `selected: T?` a `selected: Set<T>` — el widget adapta
envolviendo su único valor en un set de 0-o-1 elemento, sin tocar
`ArtWidgetConfigViewModel`.

**Bug real encontrado probando en el emulador**: seleccionar 2+ movimientos a la vez
crasheaba la app ("ArtDaily keeps stopping"). Causa: el patrón Room
`:param IS NULL OR columna IN (:param)` que funciona con un valor escalar se rompe con
una lista — Room expande `:movements` a tantos `?` como elementos tenga en TODAS sus
apariciones del SQL, así que con 2 elementos `:movements IS NULL` se volvía literalmente
`?,? IS NULL` (comparación de tupla), que SQLite rechaza con "row value misused". Fix:
un booleano aparte (`hasPeriods`/`hasMovements`) decide si se filtra, en vez de comparar
la lista contra NULL.

Esto no lo detectan los tests unitarios (`FakeArtworkRepository` no ejecuta SQL real) —
solo se vio probando en vivo. Se agregó `AppDatabaseSmokeTest
.filteringByTwoOrMoreMovementsDoesNotThrow`, un test instrumentado (Room/SQLite real) de
regresión. De paso se encontró que ese archivo ya estaba roto desde el refactor de rango
de años (referenciaba parámetros `century`/`museum` eliminados) sin que nadie lo notara,
porque los tests instrumentados no corren en `./gradlew test` — se actualizó a la firma
actual.

**2. Filtro de solo pinturas** ("¿podemos limitar las imágenes a pinturas? veo muchas
fotos de esculturas"). El campo `classification` ya existía normalizado
(`ClassificationNormalizer`) y ya estaba en la base — no hizo falta recosechar nada.
Decisión: regla fija (no un filtro que el usuario elige), `classification IN
('painting', 'print')` agregado directo al `WHERE` de `ArtworkDao.getFiltered`/
`countFiltered` — cubre Hoy, Explorar y el widget de una sola vez. Favoritos no se toca
(lee por id directo).

**3. Rotación de fondo de pantalla por Favoritos** ("debería haber una función en
configuración para que el fondo de pantalla rote entre las obras que tengo en
favoritos"). `WallpaperPreferences` suma `source: WallpaperSource`
(`DAILY_ARTWORK`/`FAVORITES_ROTATION`) — el destino (inicio/bloqueo/ambas) sigue
aplicando a cualquiera de las dos. Rotación secuencial (no aleatoria) sobre el orden de
`FavoriteDao` (más reciente guardado primero); si la última obra aplicada ya no está en
Favoritos, arranca de nuevo desde el principio. El cálculo del ciclo
(`FavoriteRotation.next`) se separó a `core-model` (Kotlin puro) específicamente para
poder testearlo en JVM sin Robolectric — `GetNextFavoriteWallpaperUseCase` solo conecta
`FavoriteDao`/`WallpaperPreferences` (con dependencias de Android) con esa función pura.
Nueva sección "Fuente del fondo automático" en Ajustes, con aviso si se elige rotar sin
tener favoritos guardados.

Verificado en vivo de punta a punta en el emulador y en el Pixel 10 real: multi-selección
sin crash, Explorar/Hoy/widget sin esculturas, y el fondo de pantalla de inicio cambiando
a la obra favorita exacta al activar la rotación. Unit tests + instrumentados en verde
(incluye 6 tests nuevos de `FavoriteRotation` y el de regresión del bug de Room).

De paso: correr `./gradlew :app:connectedDebugAndroidTest` desinstala la app del
dispositivo al terminar (comportamiento normal de AGP, no un bug) — explica por qué el
usuario reportó "no la veo en mi celular" en un momento sin que nadie la hubiera
desinstalado a mano. Hay que reinstalar con `:app:installDebug` después si se va a seguir
usando la app.

## 2026-08-20 (continuación) — Rango del selector de años: 740–año actual, no 3050 a.C.–1980

Feedback del usuario al usar el selector en el Pixel 10 real: "¿desde qué año tenemos
pinturas? no veo el punto en ir hasta años a.C. si no hay pinturas ahí, y el selector
se vuelve complicado; preferiría que llegara hasta el año actual".

Se investigó con datos reales antes de tocar nada:
- **Pinturas**: `MIN(creationYearStart) WHERE classification='painting'` = 740, 813
  obras, hasta 1929.
- Todo lo anterior al año 1000 en el catálogo entero (~114 obras de ~2000) es casi
  todo `other`/escultura/cerámica/joyería — 2 pinturas nada más. El rango completo
  (hasta ~3050 a.C.) volvía el slider difícil de manejar por un puñado de obras que
  no son pintura.

Se le preguntó al usuario dónde fijar el piso — eligió **740** (el año real de la
pintura más antigua), sabiendo que deja las ~114 obras muy antiguas fuera del alcance
de ESTE filtro específico (siguen viéndose en Favoritos/Hoy si ya están ahí).

**Cambio:** `AvailableFilterOptions.minYear`/`maxYear` dejaron de venir de
`MIN`/`MAX(creationYearStart)` reales de la base (se sacaron esas queries de
`ArtworkDao`, quedaban sin uso) — `ArtworkRepositoryImpl` ahora fija el piso en
**740** (constante, con nota de por qué) y el techo en `java.time.Year.now().value`
(el año actual de verdad, no la obra más nueva cosechada — así el selector no queda
pisado en 1980 para siempre por casualidad de qué se cosechó).

Verificado en vivo: el selector ahora muestra "740" – "2026". Reinstalado en el
Pixel 10.

## 2026-08-20 — Primera prueba en dispositivo real (Pixel 10) + 2 bugs reales de wallpaper

Primera vez que se prueba en un teléfono físico (hasta ahora todo era emulador). Costó
conectarlo: `adb devices` no veía el Pixel 10 pese al cable estar bien (sí cargaba y
mostraba notificación USB) — la causa real era que el modo de conexión USB del
teléfono estaba en "Solo carga" en vez de "Transferencia de archivos"; una vez
cambiado, apareció enseguida. Quedó verificado también que esto no era un problema del
entorno de Claude Code (se le pidió al usuario correr `adb devices` en su propia
terminal real con `!`, y daba el mismo resultado — descartó la hipótesis inicial de
que el entorno de comandos no tuviera acceso al USB físico).

**Bug real #1 — activar "cambiar fondo automáticamente" no hacía nada visible**:
el toggle en Ajustes solo guarda una preferencia; el cambio real corre dentro de
`DailyArtworkWorker`, ya programado una vez al abrir la app
(`ExistingPeriodicWorkPolicy.KEEP`) con su propio ciclo de ~24h — activar el toggle
no lo vuelve a disparar. El usuario podía estar esperando hasta 24h para el primer
cambio. Fix: `SettingsViewModel.setAutoChangeEnabled(true)` ahora aplica el fondo YA
MISMO (mismo `WallpaperApplier` que usa el worker y el botón manual de Detalle), con
feedback visible (ícono de carga + toast) — mismo criterio que ya usaba
`DailyArtworkWorker.enqueueOneTime()` para no esperar 24h al agregar un widget nuevo.
`WallpaperResult` (antes solo en `DetailViewModel`) se movió a `wallpaper/` para
compartirlo entre las dos pantallas.

**Decisión revertida — el selector de destino vuelve a Ajustes**: se había sacado el
2026-08-19 por parecer redundante con el diálogo manual de Detalle (que pregunta lo
mismo cada vez). Al probarlo en el dispositivo real, el usuario notó el problema real:
el cambio AUTOMÁTICO no tiene ningún diálogo — corre solo, sin UI — así que sin un
lugar donde guardarlo, no hay forma de configurarlo para ese caso. Se restauró
`WallpaperPreferences.target`, con el subtítulo reescrito para dejar claro que es
*solo* para el automático (el manual de Detalle sigue preguntando cada vez,
independiente de esto). Cambiar el destino mientras el automático ya está activo
también re-aplica al toque, mismo criterio que activar el toggle.

Verificado en el emulador y reinstalado en el Pixel 10 real.

## 2026-08-19 (continuación) — Paleta de colores: beige + naranja en vez de blanco/morado

Pedido del usuario. Antes de tocar nada se confirmó (grep) que ningún componente tenía
colores hardcodeados — todo usaba `MaterialTheme.colorScheme.*` por default (el morado
venía del esquema base de Material3, sin overridear) — así que un solo cambio
centralizado cascadea a toda la app sin tocar cada pantalla.

**Trabajo:**
- `ui/theme/Color.kt` + `ui/theme/Theme.kt` nuevos — antes no existían, `MainActivity`/
  `ArtWidgetConfigActivity` usaban `MaterialTheme { ... }` liso (el esquema de color
  baseline de M3, morado). `ArtDailyTheme` nuevo los reemplaza en los dos lugares.
- Paleta: naranja quemado (`#C2662B`) como `primary`, marrón cálido como `secondary`,
  beige (`#F5EEE1`) como `background`/`surface`.
- **Encontrado al verificar en vivo**: con solo `primary`/`background`/`surface`
  overrideados, la barra de tabs de abajo seguía lavanda — Material3 usa roles
  "surface container" (`surfaceContainer`, `surfaceContainerHigh`, etc., el sistema de
  tokens tonal de M3 para `NavigationBar`/`TopAppBar`/cards elevadas) que si no se
  especifican también, caen al tono morado por defecto aunque `surface` ya esté en
  beige. Se completó la paleta con esos roles (`surfaceDim`, `surfaceBright`,
  `surfaceContainer*`, `inverseSurface`, `inversePrimary`, `surfaceTint`) — recién ahí
  desapareció el lavanda del todo.
- `res/values/colors.xml` + `themes.xml`: `windowBackground` nativo también a beige
  (evita un flash blanco antes de que Compose renderice el primer frame).

Verificado en vivo en Hoy/Ajustes/Explorar: botones, toggle, chips, slider de años,
tab seleccionado — todo naranja/beige consistente, sin rastro de morado en ningún
lado.

## 2026-08-19 (continuación) — Ícono de launcher propio

El usuario compartió una imagen (puente/estanque estilo Monet con una "A" superpuesta,
generada con IA) para usar como ícono de la app — cierra el punto que quedaba pendiente
en CLAUDE.md ("Ícono de launcher propio — hoy usa el genérico del sistema").

**Herramientas**: solo había `sips` disponible (sin ImageMagick/PIL) — se instaló
Pillow (`pip install pillow`) para poder armar bien las capas del ícono adaptativo
(padding transparente, máscara circular), algo que `sips` no puede hacer solo.

**Análisis de la imagen fuente** (1254×1254, `docs/assets/app_icon_source.png` — se
guardó una copia en el repo para poder regenerar el ícono más adelante): el cuadrado
redondeado con el arte real ocupa aprox. x:[70,1182] y:[51,1190] del lienzo, con un
margen blanco/crema (~#FCFCFC) alrededor — separación limpia entre "el arte" y "el
fondo", ideal para un ícono adaptativo (API 26+, que es lo único que corre en este
`minSdk`).

**Se generó el set completo:**
- **Ícono adaptativo** (`mipmap-anydpi-v26/ic_launcher.xml`/`_round.xml`): capa de
  fondo = color sólido `#FCFCFC` (muestreado del margen de la imagen, para que se vea
  continuo con el arte en vez de un recuadro de otro color) + capa de primer plano =
  el arte recortado, insertado al ~70% del lienzo de 108dp (zona segura estándar de
  Android, para que ningún launcher lo recorte mal).
- **Íconos legacy** (`mipmap-{m,h,xh,xxh,xxxh}dpi/ic_launcher.png` +
  `ic_launcher_round.png`, este último con máscara circular real vía Pillow) — de
  respaldo para contextos que no leen el ícono adaptativo; en la práctica, con
  `minSdk=26`, el adaptativo es el que se ve en todos los dispositivos reales.
- `AndroidManifest.xml`: `android:icon`/`android:roundIcon` agregados al fin (antes
  ausentes a propósito, "se agrega más adelante" — ya llegó ese momento).

**Verificado en vivo**: reinstalado en el emulador, el selector de apps muestra el
ícono nuevo recortado en círculo (el launcher de este Pixel usa máscara circular),
consistente con el resto de los íconos de Google alrededor. La app sigue abriendo
normal.

## 2026-08-19 (continuación) — Se saca el selector de destino del fondo de pantalla de Ajustes

Observación del usuario: "no veo propósito en la parte de configuración el tener la
opción de definir si setear la imagen de fondo en home o en lock screen o ambas, ya
que al seleccionar una imagen te da esas mismas opciones" — el selector de Ajustes y
el diálogo manual de Detalle preguntaban lo mismo dos veces, y como el diálogo
manual siempre vuelve a preguntar, el valor guardado en Ajustes no se notaba nunca en
la práctica para ese flujo.

Se le presentaron 3 caminos: (1) sacar el diálogo manual y aplicar directo con lo de
Ajustes, (2) sacar el selector de Ajustes y fijar el automático en "ambas pantallas",
o (3) sacar el selector de Ajustes pero que el diálogo recuerde la última elección.
Eligió el **camino 2**.

**Cambios:**
- `WallpaperPreferences`: se sacó `target`/`setTarget` — solo queda `autoChangeEnabled`.
- `SettingsScreen`/`SettingsViewModel`: se sacó toda la sección "Dónde aplicar el
  fondo" (título, subtítulo, 3 radio buttons) — Ajustes queda solo con el toggle,
  cuyo subtítulo ahora aclara "se aplica a pantalla de inicio y bloqueo".
- `DailyArtworkWorker`: el cambio automático usa `WallpaperTarget.BOTH` fijo en el
  código (antes leía la preferencia) — no hay a quién preguntarle cuando corre solo.
- `DetailViewModel`: ya no depende de `WallpaperPreferences` — el diálogo manual de
  Detalle sigue preguntando cada vez (sin cambios ahí), simplemente arranca siempre
  en `WallpaperTarget.BOTH` (el default del data class) en vez de una preferencia
  guardada.
- Strings de Ajustes para el destino (`settings_wallpaper_target_*`) eliminadas de
  ambos idiomas — las de `WallpaperTarget` (`wallpaper_target_home/lock/both`) se
  mantienen, las sigue usando el diálogo de Detalle.

Verificado en vivo: Ajustes solo muestra el toggle; el diálogo de Detalle sigue
funcionando igual, precargado en "Home and lock screen" por defecto.

## 2026-08-19 (continuación) — Decisión: clasificar movimiento a mano, obra por obra

Reacción del usuario al hueco estructural que quedó documentado arriba ("solo AIC
tiene el dato, el techo sigue bajo"): "parece que vamos a tener que agregar esos
datos manualmente, los movimientos". Antes de ponerse a escribir algo, se le
plantearon 3 caminos con esfuerzo muy distinto:

1. Diccionario **artista → movimiento** (chico, a mano, se aplica solo a todas las
   obras de ese artista — mucho menos trabajo que por obra).
2. **Wikidata** como fuente automática (P135 "movement" de cada artista — esto ya
   estaba anotado en CLAUDE.md como plan futuro de enriquecimiento; el Met además ya
   da el ID de Wikidata de cada artista en varios registros).
3. Etiquetar **obras individuales** a mano.

El usuario eligió el **camino 3** a propósito — "más acertado" — aunque sea el de
más trabajo manual: prioriza precisión sobre apalancamiento. Pidió posponerlo (queda
anotado como pendiente en CLAUDE.md, sin alcance definido todavía — cuántas obras,
mecanismo de captura). No se tocó código en este punto.

## 2026-08-19 (continuación) — Diccionario de movimientos ampliado

Pregunta del usuario: "¿por qué tenemos tan pocos movimientos?" (Explorar mostraba
solo 4 chips: Cubismo, Impresionismo, Postimpresionismo, Realismo). Investigado con
datos reales, no supuesto:

- Solo **87 de 2001 obras** (4.3%) tenían movimiento asignado.
- Causa raíz: **solo AIC** tiene un campo real de movimiento (`style_title`/
  `style_titles`) — Met usa un fallback débil contra period/culture con el mismo
  diccionario chico, y **CMA/Rijksmuseum nunca lo llenan** (sin campo limpio
  equivalente, así desde el diseño original). El diccionario de `MovementNormalizer`
  (core-model) es chico a propósito — 14 movimientos, solo arte occidental
  ~1850-1950 — y si no matchea exacto, `null` en vez de adivinar.
- Consulta real a la API de AIC (curl, 100 obras de muestra) mostró movimientos
  reales presentes en los datos que el diccionario no reconocía: **Mannerism,
  Modernism, Neoclassicism, Romanticism**. (Baroque/Renaissance/Gothic también
  aparecían, pero se dejaron afuera a propósito — esos ya son `period`, agregarlos a
  `movement` duplicaría la clasificación.)
- Se le preguntó al usuario si ampliar el diccionario con esos 4 — confirmó que sí.

**Cambios:** `MovementNormalizer.kt` — 4 entradas nuevas (Manierismo/Modernismo/
Neoclasicismo/Romanticismo) + tests de regresión (incluida una prueba explícita de que
Baroque/Renaissance/Gothic siguen sin matchear). **Hizo falta re-cosechar** (a
diferencia del cambio de esquema de `widget_config` de antes) — el harvester ya había
procesado y descartado como `null` estas obras, agregar el diccionario solo no las
reclasifica retroactivamente. Re-corrida completa (`bulk 2000`): 2002 obras.

**Resultado real, con expectativas realistas** (no se "arregló" del todo, el techo
sigue bajo): 95/2002 con movimiento ahora (antes 87/2001), 5 movimientos distintos
en pantalla (antes 4) — Modernismo (5) y Neoclasicismo (5) aparecieron nuevos y
visibles en Explorar, verificado en vivo. Manierismo/Romanticismo no aparecieron en
esta corrida particular (el muestreo del harvester no garantiza tocar cada obra que
los tenga), pero la clasificación ya está lista para cuando aparezcan. La limitación
estructural de fondo (solo AIC tiene datos reales, y ahí solo una fracción de las
obras trae un `style_title` que sea realmente un movimiento) sigue — ampliar el
diccionario ayuda, pero no cambia que 3 de las 4 fuentes nunca van a tener movimiento.

**Publicación:** `artworks.db` regenerado y copiado a `assets/`, reinstall completo
del emulador para verificar, release de GitHub `data-20260819` reemplazado (mismo
tag del día — se borró el anterior con `gh release delete --cleanup-tag` y se
republicó) para que el sync también tome esta reclasificación sin esperar un
reinstall.

## 2026-08-19 (continuación) — Filtro por rango de años reemplaza a Museo/Siglo

Pedido del usuario: quitar las secciones de Museo y Siglo del filtro (Explorar y
configuración de widget, mismo componente compartido — se confirmó que aplica a los
dos), y en su lugar un selector de rango donde se puedan establecer años directamente.
También se confirmó: el filtro de museo se elimina del todo (no solo se oculta) — sin
campos muertos en el motor de filtros.

**Modelo (core-model):**
- `ArtworkFilter`: se sacaron `century`/`museum`, se agregaron `yearFrom`/`yearTo`
  (comparan contra `creationYearStart`, igual que hacía `century`).
- `AvailableFilterOptions`: se sacaron `museums`/`centuries`, se agregaron `minYear`/
  `maxYear` (bordes reales del catálogo, para los extremos del slider).

**App:**
- `ArtworkDao`: `getFiltered`/`countFiltered` cambiaron el filtro de igualdad por
  siglo/museo por un rango (`creationYearStart >= :yearFrom AND <= :yearTo`, con el
  mismo patrón `:param IS NULL OR ...` de siempre). Nuevas `getMinYear()`/
  `getMaxYear()`, se sacaron `getDistinctMuseums()`/`getDistinctCenturies()`.
- `WidgetConfigEntity`: mismo cambio de esquema que `ArtworkFilter` — **Room subió a
  `version = 3`** (v2 fue hoy mismo, por `creditLine`). Sin migración a propósito,
  `fallbackToDestructiveMigration` ya cubre esto.
- `YearRangeSelector` nuevo (`ui/common/`) — `RangeSlider` de Material3, compartido
  entre `ExploreScreen` y `ArtWidgetConfigActivity` (mismo criterio que
  `FilterSection`). Arrastre en vivo actualiza la posición visual en cada frame, pero
  solo dispara la búsqueda/conteo al soltar (`onValueChangeFinished`), no en cada
  píxel. `formatCentury` (ya no se usa) se eliminó de `FilterSection.kt`.
- `ExploreViewModel`/`ArtWidgetConfigViewModel`: `selectedMuseum`/`selectedCentury` →
  `yearFrom`/`yearTo`, inicializados a los bordes reales (`available.minYear/maxYear`)
  al cargar — equivale a "sin filtrar" hasta que el usuario arrastra.
- `harvester/storage/ArtworkSqliteWriter.kt`: mismo cambio de esquema en la tabla
  `widget_config` que crea. **No hizo falta re-cosechar las 2001 obras** — la tabla
  `widget_config` siempre está vacía en el `artworks.db` empaquetado (se llena en el
  dispositivo), así que se recreó directo por sqlite3 (`DROP TABLE` + `CREATE TABLE`)
  sin tocar la tabla `artworks`, mucho más rápido que un harvest completo.

**Tests:** `FakeArtworkRepository` (test double) actualizado al nuevo filtro por año;
`SelectionEngineTest` — el test que usaba `museum` para probar "el filtro se aplica
antes que el historial" pasó a usar `period` (sigue existiendo), más un test nuevo de
regresión específico para el rango de años (incluye un caso con `creationYearStart =
null`, que no debe colarse solo por no tener dato).

**Verificado en vivo en el emulador** (reinstall completo, esquema de Room cambió):
Museo desapareció de la UI, Siglo fue reemplazado por "Years" con los bordes reales del
catálogo ("3050 BCE" – "1980"). Nota de proceso: los primeros intentos de arrastrar el
slider vía `adb shell input swipe` terminaban navegando "atrás" en la app en vez de
mover el thumb — el thumb en su posición default (extremo izquierdo) cae dentro de la
zona de gestos de "volver atrás" del sistema Android (edge swipe), y `adb input swipe`
no respeta la exclusión de gestos que sí protege a un toque real de usuario. Iniciando
el arrastre un poco más adentro del track (no exactamente en el borde) el slider
respondió bien: el label se actualizó en vivo (3050 BCE → 622 BCE → 1393), y se
confirmó por fuera de la app (consulta sqlite directa a la base del dispositivo) que el
rango [1393, 1980] efectivamente excluye ~185 de las 2001 obras (1816 coinciden) —
la reducción del grid no se notó a simple vista porque las obras top-ranked mostradas
ya caían dentro de ese rango, pero el conteo real confirma que el filtro sí actúa.

## 2026-08-19 (continuación) — Repo en GitHub + publicación real de artworks.db/delta.json

Último pendiente explícito que quedaba del diseño original (CLAUDE.md, punto 13): la
app nunca recibía obras nuevas después de instalada — `assets/artworks.db` solo
alimentaba el primer arranque, y no había ningún mecanismo de sync.

**Infraestructura nueva (requirió al usuario, no se podía decidir solo):**
- El proyecto NO era un repo git todavía (`git status` confirmaba "not a git
  repository"). Se decidió con el usuario: repo en **GitHub Releases**, **público**
  (un repo privado necesitaría un token en la app solo para bajar los assets — sin
  secretos que proteger en el código de todas formas, público evita esa complejidad
  entera).
- `gh` (CLI de GitHub) no estaba instalado — se instaló vía `brew install gh`.
- Git no tenía identidad configurada en esta máquina (ni `user.name` ni `user.email`)
  — se configuró (solo para este repo, no global) con el email de la sesión.
- El usuario corrió `gh auth login` a mano (flujo interactivo con navegador).
- **Limpieza antes del primer commit**: se encontraron y excluyeron dos archivos
  `.hprof` (heap dumps de ~730MB y ~740MB, sobras del bug de OOM ya arreglado el
  2026-08-17, documentado en esta misma bitácora) — se borraron del disco, no tenían
  ningún valor. También se agregó `.claude/` al `.gitignore` (settings/locks locales de
  la sesión de Claude Code, no son parte del proyecto).
- Repo creado: **https://github.com/pacohurtadof/art-daily** — commit inicial con las
  114 archivos reales del proyecto (excluyendo lo anterior), pusheado a `main`.
- Primer release publicado: `data-20260819`, con `artworks.db` (2179072 bytes) y
  `delta.json` (copia del `artworks-delta-20260819.json` de hoy, con nombre de asset
  ESTABLE sin fecha — la app siempre busca el asset literal `delta.json` del último
  release, no le importa el nombre del archivo local).
- `harvester/publish-release.sh` nuevo — automatiza este proceso para la próxima vez
  que se corra el harvester (toma el delta más reciente por fecha, lo copia con nombre
  estable, corre `gh release create` con el tag `data-YYYYMMDD`).

**App — `ArtworkSyncService` nuevo (`app/data/sync/`):**
- `GitHubApi` (Retrofit): `GET /repos/pacohurtadof/art-daily/releases/latest` (sin key,
  límite de tasa generoso para uso no autenticado) + un método `@Url` para bajar el
  asset real — mismo patrón que `RijksApi.resolve` en el harvester, porque GitHub
  redirige la descarga a otro dominio con una URL firmada
  (`release-assets.githubusercontent.com`, verificado en vivo — puede cambiar sin
  aviso, por eso no se lista en el network security config, solo `api.github.com`/
  `github.com`).
- `SyncPreferences` (`SharedPreferences`, mismo patrón que `WallpaperPreferences`):
  guarda el tag del último release ya sincronizado, para no volver a descargar/procesar
  el mismo delta en cada corrida diaria del worker.
- `ArtworkSyncService.syncIfNeeded()`: compara el tag del último release contra el
  guardado: si es el mismo, no hace nada (`AlreadyUpToDate`); si es distinto, descarga
  `delta.json` (deserializa directo a `List<Artwork>` — el harvester ya lo escribe en
  ese formato exacto vía kotlinx.serialization) y hace `ArtworkDao.upsertAll()` (ese
  método YA EXISTÍA, con un comentario que anticipaba exactamente este uso — quedó sin
  llamar hasta hoy). Todo error cae a `Failed` en vez de relanzar — es best-effort.
- **`NetworkModule.kt` nuevo** — primer uso real de Retrofit DENTRO de `:app` (antes
  solo el harvester llamaba APIs en vivo; la app únicamente leía de Room). Mismo patrón
  que `harvester/network/HttpClientFactory.kt`, como módulo Hilt.
- `DailyArtworkWorker` ahora llama `artworkSyncService.syncIfNeeded()` como primer paso
  de `doWork()`, antes de calcular la obra del día — así una obra recién sincronizada
  ya es candidata inmediatamente. Se le agregó `Constraints(NetworkType.CONNECTED)` al
  `PeriodicWorkRequest`/`OneTimeWorkRequest` (antes no tenía restricción de red
  explícita — ahora si no hay conexión, WorkManager espera en vez de intentarlo a
  ciegas y fallar la parte de red).
- `network_security_config.xml`: agregado `api.github.com`/`github.com` a la lista
  documentada (no es una restricción real — Android permite HTTPS a cualquier dominio
  por defecto salvo que se bloquee explícito — pero mantiene la práctica ya establecida
  de documentar qué dominios toca la app).

**Verificado en vivo, end-to-end, en el emulador** (reinstall completo para que
WorkManager re-programe el `PeriodicWorkRequest` con las constraints nuevas — con
`ExistingPeriodicWorkPolicy.KEEP` un install incremental no reemplaza un work ya
encolado): logcat mostró la secuencia real completa — `GET
api.github.com/repos/.../releases/latest` → 200, descarga de `delta.json` desde
`release-assets.githubusercontent.com` → 200 (3292790 bytes, coincide exacto con el
tamaño real del archivo), `WM-WorkerWrapper: Worker result SUCCESS`. Confirmado por
fuera de la app: `sync_prefs.xml` con `last_synced_tag=data-20260819`, y la tabla
`artworks` de Room con exactamente 2001 filas (coincide con el release).

## 2026-08-19 (continuación) — Localización del texto propio de la app (strings.xml)

Pregunta de seguimiento del usuario: "¿por qué no traduce directamente al idioma del
dispositivo todo el texto de la app?" — distinto del punto anterior (ML Kit traduce
texto AJENO, de los museos). El texto de la app (botones, títulos, mensajes) estaba
100% hardcodeado en español, directo en el código Kotlin, sin pasar por ningún sistema
de idiomas — nada que "elija" el idioma del teléfono. El mecanismo correcto acá es el
nativo de Android (`res/values/strings.xml` + `res/values-<idioma>/`), no ML Kit: es
gratis, instantáneo, sin descargar nada.

Se le preguntó qué idiomas quería — pidió **solo inglés** por ahora (el resto cae al
español por defecto, que ya no es hardcodeado sino el `values/strings.xml` sin
calificador, el fallback natural de Android).

**Trabajo:**
- `res/values/strings.xml` (español, default) + `res/values-en/strings.xml` (inglés) —
  ~50 strings extraídos de: `MainActivity` (tabs), `HomeScreen`, `ExploreScreen`,
  `FavoritesScreen`, `DetailScreen` (incluidos los toasts y el diálogo de wallpaper),
  `SettingsScreen`, `ArtWidget` (el widget de Glance), `ArtWidgetConfigActivity`, y el
  layout XML nativo `widget_loading.xml` (el placeholder que Android muestra antes de
  que Glance tome el control).
- `WallpaperTarget` pasó de `label: String` fijo a `labelRes: Int` (`@StringRes`) —
  un enum no es contexto `@Composable`, no puede llamar `stringResource()` directo; se
  resuelve en cada sitio donde se muestra (Ajustes, diálogo de Detalle).
- Mismo problema en `DetailViewModel`: el mensaje de "no se tradujo" (ya estaba en tu
  idioma / falló) se guardaba como `String` ya armado — se cambió a un enum
  (`TranslationMessage`) que `DetailScreen` resuelve a texto real vía `stringResource`.
- `FilterSection.label` pasó de `(T) -> String` a `@Composable (T) -> String`, y
  `formatCentury` (que arma "Siglo N"/"Siglo N a.C.") pasó a ser `@Composable` usando
  `stringResource` con formato (`%1$d`) — antes era una función plana, no podía llamar
  `stringResource`.
- **Ojo con Glance** (`ArtWidget.kt`, el widget real, no `ArtWidgetConfigActivity` que
  es Compose normal): Glance NO corre dentro de un `ComposeView` de Android — tiene su
  propia composición que se traduce a `RemoteViews`, así que
  `androidx.compose.ui.res.stringResource()` no aplica ahí (depende de
  `LocalContext`/`LocalConfiguration`, que Glance no provee). Se usó en cambio el
  `context: Context` ya capturado por el closure de `provideGlance()` +
  `context.getString(...)` — el equivalente correcto para Glance.
- Bug propio evitado a tiempo: al escribir el código para Glance usé por reflejo
  `stringResource()` (el de Compose UI normal) — hubiera compilado pero fallado en
  runtime al no encontrar `LocalContext`. Se corrigió antes de instalar, no fue un bug
  real llegado a producción.

**Verificado en vivo en el emulador (que resulta estar en `en-US`)**: sin tocar nada
más que instalar el APK, toda la navegación/Detalle/Ajustes/Explorar aparecieron en
inglés automáticamente — "Today/Explore/Favorites/Settings", "Add to favorites",
"Set as wallpaper", "About this artwork", "Translate" → "It's already in your
language." (con una reseña que ya estaba en inglés), "Century 31 BCE", etc. Los
*valores* de period/movement en los chips de filtro (ej. "Barroco", "Impresionismo")
siguen en español a propósito — son datos normalizados del catálogo
(`PeriodNormalizer`/`MovementNormalizer` en core-model), no texto de la UI; traducir
eso sería un trabajo aparte (localizar el diccionario de normalización), no pedido hoy.

## 2026-08-19 (continuación) — Traducción on-device de las reseñas (ML Kit)

Pregunta del usuario: "¿podemos traducir las descripciones al idioma del dispositivo?"
Las reseñas llegan en inglés (AIC/CMA) o inglés/neerlandés (Rijksmuseum), nunca en el
idioma real del usuario. Se le preguntó cómo prefería resolverlo — se descartó a
propósito una API de traducción en la nube (rompería "sin backend"/"nunca llama a APIs
externas en vivo", necesitaría API key + facturación) y traducir en el harvester (solo
cubriría un set fijo de idiomas). Eligió **ML Kit on-device**: gratis, sin key, el
modelo de cada par de idiomas se descarga una vez (unos MB) y después funciona offline.

**Implementación:**
- Dependencias nuevas, versión verificada en vivo contra `dl.google.com`/Maven Central
  (2026-08-19): `com.google.mlkit:translate:17.0.3`, `com.google.mlkit:language-id:
  17.0.6`, `kotlinx-coroutines-play-services:1.10.2` (misma versión exacta que el resto
  de kotlinx-coroutines del proyecto — no la 1.11.0 más reciente, mismo motivo de
  siempre: `androidx.test:core` fuerza 1.10.2 en runtime).
- `TranslationService` nuevo (`app/data/translation/`): como Rijksmuseum puede traer la
  reseña en inglés O neerlandés según el objeto (`RijksMapper`, `edmLangMapFirst("en",
  "nl")`), no se puede asumir el idioma de origen por la fuente — se identifica el
  idioma real del texto con `LanguageIdentification` antes de traducir. Si ya coincide
  con el idioma del dispositivo, o no se pudo identificar, no traduce (`NotNeeded`).
- **Opt-in a propósito, no automático**: la traducción solo se dispara al tocar
  "Traducir" en Detalle — mismo criterio que el fondo de pantalla automático (no bajar
  datos/modelos sin que el usuario lo pida explícitamente). Botón "Ver original" para
  volver al texto tal cual llegó del museo. La atribución de AIC (CC BY 4.0) se sigue
  mostrando aunque el texto esté traducido — sigue siendo un derivado del mismo
  original.
- Verificado en vivo en el emulador con una obra de Rijksmuseum cuya reseña solo
  existía en neerlandés ("Stilleven met muilen, een pijp, een ketting met munten en
  een fles.") — con dispositivo en inglés: tocar "Traducir" identificó el idioma
  (`nl`), descargó el modelo neerlandés↔inglés (confirmado en logcat: carga de
  `libtranslate_jni.so`, diccionarios `merged_dict_en_nl_*`, traducción NMT real) y
  mostró "Still life with builds, a pipe, a chain with coins and a bottle." — "Ver
  original" vuelve correctamente al neerlandés. Nota de calidad (no es un bug): el
  modelo tradujo "muilen" (pantuflas) como "builds" — imprecisión propia del modelo
  NMT compacto on-device de ML Kit, no de la integración.
- **Nota de rendimiento**: la descarga del modelo tardó ~5 minutos en el emulador (red
  virtualizada lenta) — en un teléfono real con WiFi debería ser cuestión de segundos,
  pero vale la pena que el usuario sepa que la primera traducción de cada par de
  idiomas puede demorar visiblemente.

## 2026-08-19 (continuación) — Reseñas curatoriales reales (CMA/AIC) + campo `creditLine`

Pregunta del usuario: "¿tenemos más descripción de las APIs, como un breve resumen de
su historia?". Se verificó en vivo (curl real, no de memoria) contra las 4 fuentes:

- **Rijksmuseum**: ya se usaba correctamente (`cho["description"]` del EDM) — nada que
  cambiar. 3/3 objetos de prueba con reseña real.
- **Cleveland (CMA)**: bug real encontrado — la API SÍ manda un campo `description` con
  reseñas curatoriales reales (98% fill-rate en una muestra de 100), pero
  `CmaArtworkDto` nunca lo declaraba, así que kotlinx.serialization lo descartaba en
  silencio. Mientras tanto, `Artwork.description` para CMA en realidad guardaba
  `creditline` (la línea de donación, ej. "Gift of..."), etiquetado como si fuera una
  reseña. Confirmado por curl: `share_license_status: CC0` y `copyright: None` a nivel
  de objeto — sin carve-out de licencia, igual que el resto de los datos de CMA.
- **Art Institute of Chicago**: mismo bug (usaba `credit_line` como `description`), más
  una reseña real disponible en `description`/`short_description` (86% fill-rate en
  muestra de 50) — PERO la API responde explícitamente que ese campo está licenciado
  **CC BY 4.0**, no CC0 como el resto de los datos de AIC. Se le preguntó al usuario
  qué hacer: decidió usarla igual, mostrando atribución visible.
- **The Met**: confirmado que no existe ningún campo de reseña/historia en su API (se
  revisaron los 46 campos del objeto completo) — se deja `description = null` a
  propósito, con nota en el código de por qué.

**Cambios de modelo/esquema:**
- `Artwork` (core-model): 2 campos nuevos — `creditLine: String?` (línea de
  donación/crédito, separada de la reseña) y `descriptionAttribution: String?`
  (no-null solo cuando la fuente exige mostrar atribución visible, hoy solo AIC).
- `StringNormalization.stripHtmlTags()` nuevo — la `description` de AIC viene con HTML
  simple (`<p>`, `<em>`), se limpia a texto plano.
- `AicApi.FIELDS` ahora pide `description,short_description` (antes no se pedían).
- `CmaArtworkDto` ahora declara `description` (ya venía en la respuesta, solo faltaba
  el campo en el DTO).
- `ArtworkSqliteWriter` (harvester) y `ArtworkEntity`/`ArtworkMappers` (app): 2 columnas
  nuevas en la tabla `artworks`. **Room subió a `version = 2`** con
  `fallbackToDestructiveMigration(true)` en `DatabaseModule` — sin usuarios reales
  todavía, no se escribió una migración real a propósito.
- `DetailScreen`: nueva sección "Sobre esta obra" (antes el texto de `description`
  aparecía sin encabezado, mezclado con el resto de metadata) + fila "Crédito" separada
  + línea de atribución visible cuando `descriptionAttribution != null`.
- Tests actualizados (3 archivos que construían `Artwork(...)` a mano) — todos los
  tests de los 3 módulos (core-model/harvester/app) siguen pasando.

**Regeneración completa de `artworks.db`:** se corrió `./gradlew :harvester:run
--args="bulk 2000"` de nuevo contra las 4 APIs reales (no se podía solo alterar el
esquema del archivo viejo — hacía falta re-cosechar para que las filas existentes
trajeran los campos nuevos). Resultado: **2001 obras** (antes 2249 — el pool de
`BULK_QUERY_TERMS` es el mismo pero el orden/contenido de resultados de cada API varía
entre corridas). Fill-rate real en el archivo final: AIC 240/409 con reseña, CMA
518/530, Rijksmuseum 570/750, Met 0/312 (esperado). Copiado a
`app/src/main/assets/artworks.db` y **reinstalado completo** (`adb uninstall` +
`installDebug` — no solo `install -r`, mismo gotcha de siempre con `createFromAsset`).

**Verificado en vivo en el emulador:** obra de Rijksmuseum mostrando reseña real en
neerlandés (sin versión en inglés disponible, cae al fallback `edmLangMapFirst("en",
"nl")` ya existente); obra de AIC ("Adriaen van de Velde") mostrando reseña completa +
la línea "Art Institute of Chicago, CC BY 4.0" debajo, y "Crédito: Sidney A. Kent Fund"
como fila separada.

## 2026-08-19 (continuación) — Bug nuevo: la obra del día cambiaba en cada apertura

Reportado por el usuario después de cerrar los 4 ítems priorizados: "la obra de hoy
cambia cada vez que entro a la app, debería quedarse la misma hasta el día siguiente".
Confirmado — de hecho se había estado viendo todo el día durante las pruebas de esta
sesión sin identificarlo como bug (cada `force-stop`+reinicio del emulador mostraba una
obra distinta).

**Causa raíz:** `GetArtworkOfTheDayUseCase` no tenía ningún concepto de "ya se eligió
hoy" — cada invocación llamaba a `SelectionEngine.pickForWidget()` (que sortea al azar
entre los candidatos no mostrados recientemente) y grababa un registro nuevo en
`history`, sin importar si ya había uno de hoy mismo. `HomeViewModel` llama a este
use case en su `init{}`, que se re-ejecuta cada vez que se crea una instancia nueva
(cada arranque de la app) — de ahí el sorteo repetido.

**Fix:** `GetArtworkOfTheDayUseCase` ahora primero pregunta
`HistoryDao.getMostRecentSince(widgetId, inicioDeHoy)` — si ya hay un registro de HOY
(medianoche local real vía `LocalDate`/`ZoneId`, nativos desde API 26, sin necesidad de
desugaring) para ese `widgetId`, devuelve esa misma obra (resuelta vía
`ArtworkRepository.getById`) sin sortear ni grabar de nuevo. Si no hay ninguna, o la
que había ya no resuelve (ej. catálogo regenerado), cae al flujo normal de siempre
(sortear + grabar). Cambiar los filtros de un widget a mitad del día NO hace que
resortee — se considera correcto: ya se mostró algo hoy, el resorteo espera al día
siguiente.

Se agregaron 3 tests de regresión (`GetArtworkOfTheDayUseCaseTest`, con un
`FakeWidgetConfigDao` nuevo) — el más importante prueba con 2 candidatos que una
segunda llamada el mismo día devuelve LA MISMA obra (sin este fix, ~50% de las
corridas del test fallarían por el azar del sorteo).

**Verificado en vivo:** 3 reinicios seguidos de la app en el emulador
(`force-stop` + `am start`), misma obra ("Elles: Woman in Bed") las 3 veces — antes
del fix cambiaba en cada uno.

## 2026-08-19 (continuación) — Ítem #4: fondo de pantalla (manual + automático)

Última funcionalidad de la lista priorizada. Antes de tocar código se confirmaron tres
decisiones de diseño con el usuario (ver conversación): (1) el cambio automático usa el
mismo mecanismo que ya calcula la obra del día (`DailyArtworkWorker`, sin timers
nuevos), (2) el destino (inicio/bloqueo/ambas) lo elige el usuario, no fijo, y (3) el
cambio automático es **opt-in, apagado por defecto** — cambiar el fondo de pantalla sin
que se pida es invasivo.

**Piezas nuevas:**
- `wallpaper/WallpaperTarget.kt` — enum `HOME`/`LOCK`/`BOTH`, mapea a las flags reales
  de `WallpaperManager` (`FLAG_SYSTEM`/`FLAG_LOCK`).
- `wallpaper/WallpaperApplier.kt` — descarga la imagen vía Coil (mismo patrón que
  `WidgetImageDownloader`, `allowHardware(false)` porque un bitmap "hardware" no se
  puede pasar a `setBitmap()` de forma confiable) y llama a
  `WallpaperManager.setBitmap(bitmap, null, true, flags)`.
- `data/settings/WallpaperPreferences.kt` — dos valores simples (booleano + enum),
  `SharedPreferences` de toda la vida alcanza, no hizo falta traer DataStore. Expuestos
  como `StateFlow` para que la UI se recomponga sola.
- `ui/settings/` (`SettingsScreen`/`SettingsViewModel`) — pantalla nueva, **4to tab**
  "Ajustes" en el bottom nav (ícono `Icons.Filled/Outlined.Settings`, del mismo
  `material-icons-core` del ítem #3). Toggle de cambio automático + selector de destino
  por radio buttons.
- **Permiso agregado al manifest**: `SET_WALLPAPER` (permiso "normal", sin diálogo en
  runtime).
- **Botón manual "Usar como fondo de pantalla"** en `DetailScreen` — abre un diálogo con
  las mismas 3 opciones de destino, precargado con la preferencia guardada en Ajustes,
  aplica al confirmar. Feedback vía `Toast` (éxito/error) y el botón se deshabilita
  ("Aplicando…") mientras la descarga+aplicación está en curso.
- **`DailyArtworkWorker` extendido**: además de actualizar cada widget colocado, si
  `WallpaperPreferences.autoChangeEnabled` está activo, aplica la obra del día de la app
  principal (`widgetId=0`, mismo convenio que usa "Hoy") como wallpaper — este paso es
  independiente del loop de widgets, así que corre igual aunque no haya ningún widget
  colocado en el home screen.

**Verificado en vivo, end-to-end, en el emulador:**
- Ajustes: toggle apagado por defecto, target por defecto "ambas pantallas"; se cambió
  a "solo inicio" + toggle activado, y **sobrevivió un `force-stop` + reapertura**
  (confirma que `SharedPreferences` persiste entre procesos, no solo en memoria).
- Botón manual desde Detalle: diálogo pre-cargado con la preferencia guardada, botón
  pasa a "Aplicando…" y vuelve a su texto normal al terminar.
- **Confirmación real, no solo de UI:** se volvió al launcher del emulador
  (`KEYCODE_HOME`) antes y después de aplicar — el fondo de pantalla del sistema
  cambió de verdad, de la imagen default de Android al autorretrato de Rembrandt
  usado en la prueba.

### Con esto se cierran los 4 ítems que el usuario pidió priorizar ayer

Los 4 arreglados/agregados y verificados en vivo en esta sesión: bug de favoritos
desincronizados (+ el bug de navegación más profundo que lo causaba), botón invisible
en Hoy, iconos de tabs consistentes, y fondo de pantalla manual + automático opt-in.

## 2026-08-19 — Bug #1 (favoritos desincronizados) y bug #2 (botón invisible) arreglados

Primeros dos ítems de la lista priorizada que dejó el usuario ayer (bugs primero, luego
funcionalidad nueva). Ambos reproducidos en vivo en el emulador antes de tocar código, y
ambos re-verificados en vivo después del fix.

### Bug #1 — "Quitar de favoritos" no se reflejaba en otras pantallas

**Reproducido:** favorito desde Hoy → Detalle → "Quitar de favoritos" → cambiar de tab
y volver a Hoy → seguía diciendo "Quitar de favoritos" (como si siguiera favorito).

**Causa raíz real (no era lo que se sospechaba ayer):** `HomeViewModel` y
`DetailViewModel` leían `favoriteDao.isFavorite(id)` **una sola vez** en su `init {}`
(`suspend fun`, no reactivo). Si el favorito se quitaba/agregaba desde OTRA pantalla, el
`StateFlow` de la primera nunca se enteraba — quedaba con el valor cacheado del momento
en que se creó el ViewModel.

**Fix:** se agregó `FavoriteDao.observeIsFavorite(id): Flow<Boolean>` (mismo `SELECT
EXISTS`, pero como `Flow`) y tanto `HomeViewModel` como `DetailViewModel` ahora
`collect` ese flow dentro de su `init {}`, actualizando `_uiState` cada vez que Room
emite un cambio — venga de donde venga el toggle. `toggleFavorite()` en ambos ya no
actualiza `isFavorite` a mano; el `collect` lo hace solo, así el estado en pantalla
nunca puede desincronizarse de la base real.

**Segundo bug encontrado en el camino, más grave, mientras se verificaba el fix
anterior:** el patrón de tabs (`popUpTo(startDestination){ saveState = true };
launchSingleTop = true; restoreState = true` — el "recipe" oficial de Google para bottom
nav) asume que cada tab es una sola pantalla top-level. Acá `Detalle` se empuja ENCIMA
de cualquier tab (Hoy, Explorar o Favoritos) como pantalla compartida. Reproducido:
parado en Detalle (empujado desde Hoy), tocar el tab "Hoy" no hacía nada visible; y
peor — parado en Explorar, tocar "Hoy" mostraba **Detalle**, no Hoy. Mecanismo: al
cambiar de tab con `Detalle` encima, `popUpTo(...){ saveState = true }` guarda y luego
`restoreState = true` restaura Detalle **junto con** el tab de abajo, como una sola
unidad — no la pantalla del tab sola.

**Fix:** se quitó `saveState`/`restoreState` del `onClick` de `NavigationBarItem` en
`MainActivity.kt`; queda solo `popUpTo(startDestinationId){ inclusive = false };
launchSingleTop = true`. Costo: cada cambio de tab colapsa el stack de ese tab de
verdad, así que el scroll/filtros de Explorar se resetean al volver (antes se
conservaban). A cambio, cambiar de tab siempre muestra la pantalla correcta — se
consideró el trade-off correcto dado que el bug anterior mostraba datos incorrectos.

Ambos fixes verificados end-to-end en el emulador: favorito → Detalle → quitar →
Explorar → Hoy, dos veces seguidas, mostrando siempre la pantalla y el estado correctos.

### Bug #2 — botón "Ver detalles" en Hoy casi invisible

`OutlinedButton` con colores default de M3 (borde/texto en el color "primary" del
tema) sobre una foto de fondo oscura — muy bajo contraste, confirmado visualmente en
captura. Fix: `border = BorderStroke(1.dp, Color.White)` +
`colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)`, mismo
blanco que ya usa el resto del texto superpuesto en el degradado de `HomeScreen`.

### Ítem #3 — iconos de tabs transparentes y consistentes

Se reemplazaron los emoji (🏠🔍♥, cada uno con su propio estilo visual — casita a
color, lupa, corazón de texto) por `material-icons-core` (NO `-extended`, que trae
miles de iconos y agrega varios MB al APK innecesariamente): `Icons.Filled/Outlined.
Home`, `.Search`, `.Favorite`/`.FavoriteBorder`. Los tres ahora comparten el mismo
trazo monocromo, sin fondo propio (lo pinta `NavigationBarItem`), con el patrón M3
estándar de outline-cuando-inactivo / relleno-cuando-seleccionado. Dependencia nueva:
`androidx.compose.material:material-icons-core` (sin versión propia — la resuelve el
Compose BOM ya declarado). Verificado en vivo en los tres tabs.

### Pendiente que queda

4. Fondo de pantalla automático (`WallpaperManager` + permiso `SET_WALLPAPER`, desde
   `DailyArtworkWorker`, con opt-in en Ajustes) — la más compleja, para el final.

## 2026-08-18 — UI de filtros del widget, bug real de datos en AIC

### Estado al cierre

- **UI de filtros construida** dentro de `ArtWidgetConfigActivity`: chips por
  periodo/movimiento/museo/siglo (`FlowRow` + `FilterChip`), con contador en vivo de
  cuántas obras coinciden (`ArtworkDao.countFiltered`) y las opciones disponibles
  sacadas de la base real (`ArtworkDao.getDistinct*`), no de un catálogo fijo. Filtro
  por artista queda fuera (necesita buscador, no chip) — pendiente explícito.
- **Bug real de datos encontrado y arreglado**: `AicMapper` usaba `department_title`
  (categoría curatorial, ej. "Arts of the Americas") como candidato de **movimiento**
  cuando `style_title` no matcheaba — el departamento no es un movimiento artístico,
  y eso producía asignaciones falsas (ej. "Sunflowers" o "Self-Portrait" etiquetados
  con un movimiento que no les correspondía). Se separaron los candidatos:
  `movement` ahora SOLO usa `style_title`/`style_titles` (dato real del museo);
  `department_title` se mantiene como último recurso solo para `period` (señal más
  débil pero razonable ahí). Verificado con diff antes/después: 21 obras de AIC que
  tenían un movimiento mal puesto ahora quedan correctamente en `null`.
- **Base de datos regenerada y re-copiada a `assets/`** con el fix ya aplicado.

### Investigación larga: el widget a veces se borraba solo al confirmar

Reproducido varias veces: agregar un widget con filtro, tocar "Agregar widget", y
Android lo eliminaba automáticamente unos segundos después (`AppWidgetServiceImpl:
deleteAppWidgetId()` + `Duplicate finish request` en logcat). Diagnóstico con logging
temporal en cada paso del ciclo de vida (`onCreate`/`confirmAndFinish`/`onPause`/
`onStop`/`onDestroy`) confirmó:
- El guardado en Room **sí funciona correctamente** (verificado widgetId=13, luego
  widgetId=19 — ambos persistieron limpios, con la config correcta en `widget_config`).
- Al menos un intento (widgetId=20) mostró `deleteAppWidgetId()` disparándose **en el
  mismo milisegundo** que nuestro propio `confirmAndFinish()` — una condición de
  carrera externa (posible toque duplicado/fantasma del emulador, o el usuario
  interactuando muy rápido entre intentos), no un bug de nuestra lógica.
- Mitigación aplicada de todas formas (buena práctica, resuelva o no la causa raíz):
  el botón "Agregar widget" ahora se deshabilita y muestra "Guardando…" apenas se
  toca, para que no invite a tocar de nuevo o presionar "atrás" mientras el guardado
  (async) todavía no termina.
- **Conclusión:** la lógica de guardado/filtrado está probada y funciona. Si el
  problema de auto-eliminación reaparece, sospechar primero de interacción rápida
  repetida o touch del emulador antes que del código — ya se descartó como causa el
  guardado en sí.

### Tercera fuente agregada: Cleveland Museum of Art (CMA)

Mismo patrón que Met/AIC (`CmaApi`/`CmaArtworkDto`/`CmaMapper`), verificado en vivo. A
diferencia de Met/AIC, la búsqueda de CMA ya trae el objeto completo (no hace falta
llamada de detalle por obra). `period`/`movement` quedan siempre `null` para CMA —
no tiene campo dedicado; lo más cercano es `current_location` (nombre de sala física,
ej. "222 Impressionism & Post-Impressionism") o `department`, y ya se decidió como
regla del proyecto no adivinar clasificación desde ese tipo de texto (mismo motivo
del fix de AIC de este mismo día). Corrido contra la API real: 25 obras nuevas
(Monet, Degas, Van Gogh, Cézanne, Gauguin, Pissarro...). Total en `artworks.db`: 63
obras (met=43, aic=48, cma=25 — hay overlaps de query, no suma exacta).

**Rijksmuseum investigado y pospuesto deliberadamente**: su API vieja se apagó el
5 de enero de 2026. La nueva (`data.rijksmuseum.nl`) ya no requiere key, pero es
Linked Open Data real (JSON-LD/CIDOC-CRM vía especificación Linked Art) — un objeto
individual son ~4000 líneas de JSON anidado, con clasificación en vocabulario Getty
AAT (URIs, no texto plano). Es la fuente con la clasificación potencialmente más
confiable de las cuatro (vocabulario controlado real), pero integrarla bien es un
tipo de trabajo distinto al patrón Api+Dto+Mapper de las otras tres — se deja
explícitamente pendiente, no se fuerza al mismo molde.

**Regla de clasificación formalizada** (ya no solo un fix puntual de AIC): `movement`/
`period` solo se llenan desde un campo dedicado y limpio de la fuente. Nunca desde
departamento curatorial, nombre de sala/ubicación física, o texto libre — mejor
`null` que adivinar mal.

### Pruebas unitarias agregadas (55 en total, todas pasan)

- `:core-model` (26): `PeriodNormalizerTest`, `MovementNormalizerTest`,
  `ClassificationNormalizerTest`, `CenturyCalculatorTest`, `RankScoreCalculatorTest`.
- `:harvester` (22): `MetMapperTest`, `AicMapperTest`, `CmaMapperTest` — incluye un test
  de regresión explícito (`REGRESSION - department_title alone never produces a
  movement`) para el bug de datos encontrado hoy mismo, así no puede volver a colarse
  sin que un test falle.
- `:app` (7): `SelectionEngineTest`, con `FakeArtworkRepository`/`FakeHistoryDao` (test
  doubles en memoria, sin Room/emulador) — cubre: sin candidatos, único candidato,
  evitar repetidos, reinicio de ciclo cuando se agota el pool, aislamiento de
  historial entre widgets, ventana de `avoidRepeatDays`, filtro aplicado antes que
  historial.
- **Bug propio detectado al escribir las pruebas**: dos tests usaban `assert(...)` de
  Kotlin en vez de `assertTrue`/`assertEquals` de JUnit — `assert()` de Kotlin es un
  no-op si las aserciones de JVM no están habilitadas (Gradle no las habilita por
  defecto), así que esos tests habrían "pasado" sin verificar nada. Corregido antes
  de darlos por buenos.
- Correr todo: `./gradlew test testDebugUnitTest` (sin emulador — son unit tests
  puros, no instrumentados). El smoke test de Room (`AppDatabaseSmokeTest`) sigue
  siendo el único que sí necesita el emulador.

### Pantalla principal (`ui/home`) agregada

- `MainActivity` (ícono de launcher — antes la app no se podía abrir más que a través
  del widget), `HomeViewModel` (obra del día vía `GetArtworkOfTheDayUseCase(widgetId=0)`
  + toggle de favorito usando `FavoriteDao`), `HomeScreen` (Compose: imagen a pantalla
  completa vía Coil — esto sí funciona directo, a diferencia de Glance — con degradado
  y texto encima, botón de favorito).
- **Bug real de UX encontrado probando en el emulador** (con capturas de pantalla y
  `uiautomator dump` para coordenadas exactas, no a ojo): el botón de favorito quedaba
  dentro de la zona de gestos del sistema (borde inferior) — con navegación por gestos
  (modo por defecto hoy), los toques se interpretaban como "ir a inicio"/recientes en
  vez de llegar al botón. Arreglado con `Modifier.navigationBarsPadding()` en el
  contenedor de texto (el degradado de fondo sigue llegando hasta el borde real, solo
  el contenido interactivo respeta el inset). Verificado end-to-end: toqué el botón,
  confirmé en la base de datos real (`favorites` table) que se guardó.
- Con esto, prácticamente todo el plan original del `CLAUDE.md` está en verde.

### Rijksmuseum integrada después de todo — encontramos el atajo real

Se había pospuesto esta misma tarde por parecer Linked Open Data pura (ver más
arriba). Al investigar a fondo para implementarla:

- El endpoint por defecto (`id.rijksmuseum.nl/{id}`, modelo Linked Art) confirma lo
  temido: ~4000 líneas de JSON-LD anidado (CIDOC-CRM) por objeto, y la imagen no
  viene incluida — hay que resolver 3 saltos más (objeto → `VisualItem` →
  `DigitalObject` → `access_point`), es decir, **4 llamadas HTTP por obra** solo para
  tener metadata + imagen.
- Pero la misma API ofrece negociación de contenido a otros modelos. Pidiendo
  `?_profile=edm-framed` (representación EDM/Europeana, JSON-LD) en el MISMO
  endpoint, se obtiene metadatos + `isShownBy` (imagen, ya resuelta) + `edmRights`
  (licencia) **en una sola llamada** — tan simple como Met/AIC/CMA. Verificado
  descargando una imagen real (JPEG 400x556) desde este mismo entorno (que sí
  bloqueaba las de AIC por IP de datacenter — esta fuente no tuvo ese problema).
- El JSON-LD "framed" sigue siendo más irregular que un REST plano — el mismo tipo de
  campo (título, fecha) llega en formas distintas según el objeto (`{"@language",
  "@value"}` suelto, arreglo de esos objetos, o mapa `{"en": [...], "nl": [...]}`).
  Se optó por parsear con `JsonElement` crudo + funciones helper
  (`RijksJsonHelpers.kt`) en vez de data classes estrictas por campo.
- **Confirma la regla de clasificación**: tampoco Rijksmuseum tiene un campo de
  movimiento limpio en la representación EDM (`dcType` es tipo de objeto —
  "painting" —, `subject` son términos de iconografía Iconclass, ninguno es
  "Barroco"/"Edad de Oro neerlandesa"). `period`/`movement` quedan `null`, igual que
  Met/CMA.
- `RijksApi`/`RijksDto`/`RijksMapper`/`RijksJsonHelpers` en `harvester/rijks/`, 10
  tests unitarios (`RijksMapperTest`). Corrido contra la API real con `title=Rembrandt`:
  24 obras nuevas. Total en `artworks.db`: 96 obras (met=51, aic=72, cma=50,
  rijks=24 — hay overlaps de query, no suma exacta).
- **Lección para recordar**: antes de descartar una fuente por "parece demasiado
  compleja", vale la pena revisar si el mismo endpoint ofrece una representación más
  simple vía negociación de contenido — la documentación lo mencionaba
  (`_profile`/token `edm`), pero no se investigó a fondo la primera vez.

### Cosecha "bulk" a escala de prueba (2249 obras)

Se notó que solo había 197 obras en total (pruebas de humo con 5 búsquedas sueltas) —
irrisorio comparado con los catálogos reales (Met ~406K imágenes CC0, AIC +50K,
Cleveland ~37K, Rijksmuseum +800K). Se agregó un modo `bulk` al harvester
(`./gradlew :harvester:run --args="bulk 2000"`): itera una lista de ~30 términos de
búsqueda genéricos (portrait, landscape, still life, flowers...) sobre las 4 fuentes,
subiendo `BATCH_SIZE` de 25 a 100 por consulta, hasta acumular el total pedido.

- Corrida real en segundo plano: **2052 obras nuevas** en 9 rondas (de 30 disponibles,
  se detuvo sola al pasar el objetivo), ~16 minutos. Total en `artworks.db`: **2249**
  (met=335, aic=489, cma=620, rijks=805).
- Tardó ~16 min a propósito: cada obra de Met/AIC/Rijks necesita su propia llamada de
  detalle (a diferencia de CMA, que trae todo en el search), con una pausa de 150ms
  entre cada una — cortesía hacia APIs gratuitas sin key ni rate limit publicado, no
  un descuido. Decisión explícita del usuario: dejarlo así, no acelerar.
- Tamaño real de `artworks.db`: 1.59 MB para 2249 obras (~770 bytes/obra, calculado
  antes de correr la cosecha y confirmado casi exacto después) — la razón por la que
  subir el catálogo no dispara el peso del APK, al no empaquetar imágenes.
- **Medido con datos reales, no estimados**: build de release real de la app =
  **2.5 MB** (vs. 16.3 MB del build de debug sin optimizar) — más liviana que Muzei
  (la app real más comparable conceptualmente: arte del día + widget, 5.9 MB según
  AppBrain) incluso con las 197 obras de antes. Con las 2249 actuales, rondaría los
  4 MB — se puede recompilar para confirmar el número exacto.
- Se encontró y arregló en el camino: faltaba `app/proguard-rules.pro` (referenciado
  en `build.gradle.kts` pero nunca creado) — bloqueaba cualquier build de release.
  Creado (vacío, con nota explicativa; las libs ya traen sus propias reglas en sus
  `.aar`).
- Verificado que la app sigue funcionando bien con la base 11× más grande — captura
  de pantalla real mostrando una obra nunca antes vista en las pruebas (un relicario
  bizantino del Met, "The Fieschi Morgan Staurotheke").

### Navegación real: Explorar, Favoritos y Detalle (más splash screen)

Se agregó la UI que le faltaba a la app para ser usable de verdad — hasta ahora solo
existía "Hoy" (obra del día) sin ninguna forma de navegar a otro lado.

- **Tabs abajo** (`NavigationBar` de Material3, íconos con emoji por ahora — sin
  `material-icons-extended` a propósito, para no meter una librería grande solo por
  unos iconos): Hoy / Explorar / Favoritos.
- **`ExploreScreen`**: mismos chips de filtro que ya existían para el widget (se
  extrajeron a `ui/common/FilterSection.kt`, compartidos entre ambas pantallas) +
  cuadrícula de resultados (`LazyVerticalGrid`, tope de 200 obras por búsqueda).
- **`FavoritesScreen`**: lista de lo guardado, usando `FavoriteDao.observeAll()` que
  ya existía — solo faltaba la pantalla.
- **`DetailScreen`**: pantalla compartida de info completa (descripción, dimensiones,
  clasificación, cultura, país, N° de acceso, licencia, link a la ficha oficial del
  museo) — se llega desde Hoy, Explorar, Favoritos, o tocando el widget.
- **Click en el widget**: antes no hacía nada al tocarlo. Ahora usa
  `androidx.glance.action.actionStartActivity<MainActivity>` con el id de la obra
  como `ActionParameters` — llega como extra del Intent con el mismo nombre de la
  key (`"artworkId"`), y `MainActivity` navega directo a `detail/{id}` vía
  `LaunchedEffect`. **Verificado en vivo**: tocar el widget abrió el detalle correcto.
- **Splash screen real** (`androidx.core:core-splashscreen` 1.2.0): pantalla estática
  de arranque en frío antes de que Compose monte nada, vía `installSplashScreen()`
  (debe llamarse ANTES de `super.onCreate()`). Requirió crear `res/values/themes.xml`
  — el proyecto no tenía ningún tema propio, solo apuntaba al tema del sistema.

**Dos bugs reales encontrados probando en el emulador (no solo compilando):**

1. **`createFromAsset` de Room solo copia la base la primera vez que se instala la
   app.** Reinstalar con `adb install -r` (lo que se venía haciendo toda la sesión)
   NUNCA vuelve a copiar el asset — el emulador seguía con una base de 81 filas de
   hace horas, de antes de agregar Cleveland/Rijksmuseum, mientras `assets/
   artworks.db` ya tenía 2249. Esto es exactamente para lo que sirve el mecanismo de
   sync del `delta.json` (todavía no implementado en la app) — sin él, la única forma
   de refrescar es desinstalar y reinstalar. Detectado comparando
   `SELECT COUNT(*)` en la base real del dispositivo contra la esperada.
2. **La sección de filtros de `ExploreScreen` no era scrolleable junto con los
   resultados** — con 4 museos y una decena de periodos/movimientos/siglos, los
   chips por sí solos ya no caben en una pantalla, y como vivían en un `Column` fijo
   separado del `LazyVerticalGrid` de resultados, empujaban la cuadrícula fuera de
   vista sin forma de bajar a verla. Arreglado metiendo TODO — encabezado de filtros
   incluido — dentro de un solo `LazyVerticalGrid` (el encabezado como un ítem de
   ancho completo vía `GridItemSpan(maxLineSpan)`).

**Pendiente menor, no bloqueante:** `hiltViewModel()` de
`androidx.hilt:hilt-navigation-compose` salió deprecado en favor de un paquete nuevo
(`androidx.hilt.lifecycle.viewmodel.compose`) — sigue funcionando, solo un warning.

### Pendientes para la próxima sesión (2026-08-18, fin de día — pedido explícito del usuario)

Orden acordado: bugs primero, luego funcionalidades nuevas por complejidad creciente.

1. **Bug: "Quitar de favoritos" en Detalle hace que Explorar muestre la imagen que se
   estaba viendo en Favoritos, y que vuelva a decir "en favoritos".** No reproducido/
   confirmado en vivo todavía. Hipótesis: `MainActivity` usa un solo `NavHost` con
   `Detail` como destino compartido, pero el patrón `popUpTo + saveState +
   restoreState` en los tabs está pensado para destinos de nivel superior — mezclarlo
   con un destino al que se llega empujando navegación desde dentro de cada tab
   (Detalle) es fuente conocida de sangrado de estado entre las pilas guardadas de
   cada tab. Revisar si cada tab necesita su propio `NavHost` anidado, o replantear
   el manejo de la pila de Detalle.
2. **Bug: el botón "Ver detalles" en Hoy es casi invisible** — es un `OutlinedButton`
   de Material3 con colores default (`primary`), que se pierde contra la foto de
   fondo. Fix: colores explícitos claros, como ya tiene el resto del texto de esa
   pantalla.
3. **Iconos de los tabs**: hoy son emoji (🏠🔍♥), puestos a propósito para no meter
   `material-icons-extended` (pesada). El usuario pide íconos transparentes y con
   diseño consistente — revisar agregar `material-icons-core` (la versión chica) en
   vez de emoji.
4. **Nueva función: fondo de pantalla** — la más compleja de las cuatro. Requiere
   permiso `SET_WALLPAPER` (normal, sin diálogo de runtime) + `WallpaperManager
   .setBitmap()`. Decidir alcance: ¿botón manual "usar como fondo" primero, o de
   una vez automático (que `DailyArtworkWorker` también actualice el wallpaper cada
   corrida, probablemente detrás de un switch en Ajustes para que no sea
   sorpresivo)? El Worker ya descarga el bitmap del día para el widget — reusable.

### Lección de proceso para la próxima sesión

Pulling la base de datos del emulador con `adb shell run-as ... cat archivo.db` puede
dar una copia inconsistente si la app tiene el archivo abierto en modo WAL (los
cambios recientes viven en `-wal` hasta que se hace checkpoint). Mejor: usar
`sqlite3` directo en el dispositivo (`adb shell run-as com.artdaily.app sqlite3
/data/data/.../artworks.db 'SELECT ...'`) — el emulador ya lo trae instalado en
`/system/bin/sqlite3`, no hace falta copiar nada. Evitar `am force-stop` para
"forzar" un checkpoint — puede tener efectos secundarios (llegó a borrar un widget
en esta sesión).

Registro de sesiones de trabajo con Claude Code. Propósito: poder retomar el proyecto
otro día sin tener que reconstruir el contexto desde cero. Para el resumen de arquitectura
y decisiones ya cerradas, ver `CLAUDE.md` (raíz del repo) — este archivo es el "diario",
ese es el "manual".

---

## 2026-08-17 — Harvester (Met + AIC), Room, filtros/selección, widget

### Estado al cierre de la sesión

Completos y **verificados en vivo** (no solo compilando):
- **Punto 4 del plan (harvester)**: Met + AIC, normalización, ranking, `artworks.db`
  (SQLite) + `artworks-delta-YYYYMMDD.json`. Corrido de verdad contra las APIs reales.
- **Punto 5 (Room en :app)**: entidades/DAOs/`AppDatabase`, `createFromAsset` con el
  `artworks.db` del harvester. Verificado con una prueba instrumentada
  (`app/src/androidTest/.../AppDatabaseSmokeTest.kt`) corriendo en un emulador real.
- **Puntos 6 y 7 (filtros + selección/anti-repetición)**: `ArtworkFilter`,
  `ArtworkRepository`, `SelectionEngine`, `GetArtworkOfTheDayUseCase`.
- **Punto 8 (widget)**: Glance + WorkManager + Hilt. Widget agregado y probado en el
  emulador de verdad — muestra título/artista/museo/fecha **y la imagen** (cubriendo todo
  el widget, con el texto superpuesto sobre una franja semitransparente).

### Lo que falta (siguiente sesión, en orden sugerido)

1. **Verificar visualmente que el layout de imagen a pantalla completa quedó bien** — se
   instaló el último cambio pero no se confirmó con el usuario antes de cerrar la sesión.
2. **UI de filtros** (`ui/filters`) — hoy `ArtWidgetConfigActivity` agrega el widget SIN
   filtro (cualquier obra con buen `rankScore`). El motor de selección y el repositorio ya
   soportan filtrar por periodo/siglo/movimiento/artista/museo; falta la pantalla.
2. **Pantalla principal de la app** (`ui/home`) — hoy la app NO tiene ícono de launcher ni
   ninguna `Activity` que se abra desde el drawer. Todo lo que existe se ve solo a través
   del widget o de tests.
3. **Fuentes adicionales** (Cleveland, Rijksmuseum) — punto 10 del plan, pospuesto a
   propósito hasta cerrar MVP con Met+AIC.
4. **Favoritos e historial en UI** — los DAOs/entities ya existen (`FavoriteDao`,
   `HistoryDao`), falta la pantalla.
5. **Pruebas** más allá del smoke test de Room — el `SelectionEngine` y los mappers de
   Met/AIC no tienen tests unitarios todavía.
6. Publicar `artworks.db` / `delta.json` en algún lado real (GitHub Releases) — hoy solo
   viven en `harvester/output/` local. El mecanismo de sync del delta en la app
   (`DailyArtworkWorker` comparando `harvestedAt` y descargando el delta) tampoco está
   implementado — hoy el worker solo recalcula la obra del día, no sincroniza datos nuevos.

### Pendiente de que el usuario confirme (no bloqueante, pero anotado)

- Correr `curl -sI https://www.artic.edu/iiif/2/.../full/843,/0/default.jpg` **desde su Mac**
  (no desde este entorno en la nube) para confirmar si el bloqueo de Cloudflare que se vio
  al probar en este entorno era solo cosa del entorno (IP de datacenter) o algo real que
  afectaría a los usuarios finales de la app. No se volvió a retomar este hilo.

### Bugs reales encontrados y arreglados esta sesión (todos verificados, no solo teorizados)

Estos son útiles de recordar porque cuestan tiempo repetirlos:

1. **`ksp = "2.3.21-2.0.2"` no existía** en Maven Central — probablemente alucinado en la
   sesión de diseño original (claude.ai, sin acceso en vivo a los registries). KSP ya no usa
   sufijo `-kspVersion` desde su 2.x. Corregido a `2.3.11` (última disponible).
2. **Hilt 2.56.2 no soporta AGP 9** (soporte agregado hasta Hilt 2.59) — daba
   `Android BaseExtension not found`. Subido a 2.60.1.
3. **`kotlinx-coroutines-core` no estaba declarado** en `:harvester` — Retrofit con
   `suspend` lo necesita en runtime.
4. **`androidx.hilt` en 1.2.0** (2 versiones atrás de la 1.4.0 real) y faltaba la
   dependencia `androidx.hilt:hilt-compiler` (annotation processor de `@HiltWorker` — sin
   él no compila). Al subir Hilt, trajo `androidx.lifecycle` 2.11.0 que exige
   `compileSdk >= 37` → se subió `compileSdk`/`targetSdk` de 36 a 37.
5. **`androidx.test:core:1.7.0` fuerza `kotlinx-coroutines-bom` a `strictly 1.10.2`** —
   pedir 1.11.0 causaba `NoSuchMethodError` en runtime (diagnosticado con
   `./gradlew :app:dependencies`). Se bajó el proyecto entero a 1.10.2.
6. **Gradle se quedaba sin memoria (`OutOfMemoryError`) en R8** al armar el APK completo —
   no existía `gradle.properties`. Se creó con `org.gradle.jvmargs=-Xmx4096m`.
7. **Implementar `Configuration.Provider` en la `Application` NO basta** para que
   WorkManager use `HiltWorkerFactory` — hay que ADEMÁS desactivar el
   `WorkManagerInitializer` por defecto en el manifest (si no, se auto-inicializa con
   reflexión antes de que el `Configuration.Provider` aplique, y `@HiltWorker` truena con
   `NoSuchMethodException`).
8. **Faltaba el permiso `INTERNET`** en el manifest — sin él, cualquier llamada de red
   (incluida la del widget descargando su imagen) crashea la app entera con
   `SecurityException`.
9. **`ImageProvider` de Glance no acepta `Uri`**, solo `Bitmap`/`Icon`/resource id — hubo
   que leer el archivo de vuelta a `Bitmap` con `BitmapFactory.decodeFile`.
10. **`updateAppWidgetState` tiene el orden de parámetros `(context, definition, glanceId,
    block)`**, no `(context, glanceId, definition, block)`.

### Entorno de desarrollo dejado configurado en esta Mac

- **Gradle 9.7.0** instalado vía Homebrew (`brew install gradle`) — se usó una vez para
  generar el wrapper (`./gradlew` ya committeado, no hace falta Gradle global de nuevo).
- **`JAVA_HOME`/`ANDROID_HOME`/`PATH`** agregados de forma permanente a `~/.zshrc` — ya no
  hace falta exportarlos a mano en terminales nuevas.
- **Android SDK cmdline-tools** instalado en `~/Library/Android/sdk/cmdline-tools/latest`
  (no venía con el SDK original, solo traía `platform-tools`/`emulator`/`build-tools`).
- **Platforms instalados**: android-36 y android-37.1 (compileSdk real del proyecto).
- **Emulador `ArtDaily_Test`** creado (Pixel 6, Android API 36 google_apis arm64-v8a — la
  plataforma del *emulador* se quedó en 36, el *compileSdk* del proyecto está en 37; no es
  necesario que coincidan). Arrancar con:
  ```
  emulator -avd ArtDaily_Test
  ```
  (ya no hace falta `export ANDROID_HOME=...` antes, quedó en `~/.zshrc`).
- **DB Browser for SQLite** instalado (`brew install --cask db-browser-for-sqlite`) para
  inspeccionar `harvester/output/artworks.db` visualmente.

### Decisiones de producto tomadas en esta sesión (no estaban en los docs de diseño)

- **No clonar imágenes a un storage propio por ahora** — se enlazan directo a
  `images.metmuseum.org`/`www.artic.edu`. Si la app crece, revisitar contratar S3/R2 (no
  requiere cambio de esquema, solo cambiar el valor de la URL guardada). Detalle completo
  en la memoria de sesión de Claude (`image-hosting-decision`).
- **Network Security Config agregado** (`app/src/main/res/xml/network_security_config.xml`)
  — bloquea HTTP sin cifrar y documenta explícitamente los dominios que la app toca hoy
  (Met + AIC). No es un firewall real (Android no ofrece uno a nivel de manifest), pero es
  buena práctica barata.
