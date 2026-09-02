# TODO — ArtDaily

Lista de tareas pendientes puntuales. Para el resumen de etapas del proyecto y decisiones
ya cerradas, ver `CLAUDE.md` (raíz del repo) — ese archivo no se duplica acá. Para el
detalle día a día de lo ya hecho, ver `docs/bitacora.md`.

## Pendientes abiertos

- [ ] **Publicar en Google Play** (retomado el 2026-09-01, ver `docs/bitacora.md`). Ya
  resuelto: firma de release, `targetSdk` cumple el requisito 2026, tamaño del APK. Falta:
  - [x] Política de privacidad pública — publicada en
    https://pacohurtadof.github.io/art-daily/ (rama `gh-pages`, bilingüe ES/EN).
  - [ ] Formulario **Data safety** en Play Console — llenar como "no se recopila
    información" (sin Firebase/Analytics/Ads/Crashlytics, red solo lee imágenes de museos
    y `delta.json`, traducción 100% on-device — verificado el 2026-09-01).
  - [ ] Cuestionario de **clasificación de contenido** — el catálogo tiene desnudos
    artísticos clásicos (Met/AIC), hay que contestarlo en la consola.
  - [x] **Ficha de la tienda** — armada el 2026-09-01, ver `docs/store-listing/`:
    ícono 512×512 (`hires_icon_512.png`), feature graphic 1024×500 en ES/EN
    (`feature_graphic_es.png`/`_en.png`), 6 capturas reales tomadas en el emulador
    (`screenshots/`: Hoy, Explorar, Detalle, Favoritos, Ajustes, Widget en la pantalla
    de inicio) y textos ES/EN con los límites de caracteres ya verificados
    (`listing-es.txt`/`listing-en.txt`). Falta solo pegarlo en la consola.
  - [ ] **Testing cerrado**: arrancar cuanto antes — mínimo **12 testers** reales
    (bajó de 20 a 12 en dic. 2024) con opt-in continuo 14 días corridos. Es lo que más
    tarda en el calendario, conviene arrancarlo en paralelo al resto.
  - [ ] Subir el `.aab` firmado (`./gradlew :app:bundleRelease`) y considerar si
    `versionName = "0.1.0-mvp"` debería pasar a algo tipo `1.0.0` para el primer release
    público (hoy es un detalle cosmético, no bloquea).

- [ ] **Decidir qué hacer con las fotografías documentales/de viaje del siglo XIX del
  Rijksmuseum** (encontrado el 2026-08-28 durante la tanda 35 de movimiento). Su
  `classification_title` real (algo como "photographic print") matchea el substring
  "print" en `ClassificationNormalizer`, así que quedan como `classification="print"` —
  elegibles para "obra del día" igual que un grabado real. Ejemplos: ruinas de Sri
  Lanka, el Gran Cañón, el valle de Cachemira, perros premiados en una exposición canina
  de 1891. Ninguna tiene movimiento artístico aplicable (por eso el rendimiento de las
  tandas se derrumbó al llegar a este bloque). Es una decisión de producto, no un bug:
  ¿cuentan como "obra" para esta app, o deberían excluirse (nueva clasificación
  "photograph" separada de "print", o filtro adicional en `isEligibleForCatalog`)?
  Aproximación (imprecisa): ~385 de las 1443 filas `rijks` sin movimiento en el tramo
  rankScore 3.0-3.99 tienen año ≥1839 (invención de la fotografía). Ver
  `docs/bitacora.md` para el detalle completo.

- [x] ~~Continuar las tandas de movimiento~~ — **agotadas del todo el 2026-08-31**, no
  solo continuadas. Se revisó el rango rankScore 2.0-7.0 completo en las 4 fuentes (y se
  confirmó que por debajo de 2.0 tampoco queda nada sin revisar) — no es una pausa,
  no hay más candidatos por este mecanismo. 2715 → 3220 obras clasificadas en esta
  sesión (+505), con foco pedido explícitamente en impresionismo (349 → 425, +76).
  Lo que sigue sin `movement` (7719 de 10939) es por decisión ya tomada, no por falta de
  revisión: sobre todo Siglo de Oro holandés/Barroco/Renacimiento (es periodo, no
  movimiento, por diseño intencional del diccionario — ver `MovementNormalizer.kt`),
  retratos anónimos de otros artistas, y reproducciones fotográficas. Si se cosechan
  obras nuevas en el futuro (nuevos artistas, nuevas fuentes), sí van a aparecer
  candidatos nuevos — retomar el mismo mecanismo (`movement-overrides.csv` +
  `reviewed_ids.txt`) en ese caso. Ver `docs/bitacora.md` (2026-08-31) para el detalle
  completo de las últimas tandas (36-47). Release `data-20260831` republicado con el
  catálogo completo (10939 obras, 3220 clasificadas). Verificado en vivo el mismo día:
  instalación limpia, 10939/3220/425 impresionismo/39 icónicas, todo exacto y estable.

- [x] ~~Bloqueo de Cloudflare en el CDN de imágenes de AIC~~ — cerrado el 2026-08-31,
  no era lo que parecía. Monitoreado ~2 días seguidos (`curl` y hasta el fetcher de
  Anthropic, desde una red totalmente distinta a la del usuario, ambos siguieron dando
  403 "Just a moment..." sin parar). Pero la prueba que importa — la app real, en el
  celular real, con Coil/OkHttp — cargó "The Bedroom" de Van Gogh perfecto al primer
  intento. Conclusión: el challenge de Cloudflare en `www.artic.edu` (imágenes Y la
  ficha de obra) es contra herramientas sin motor JS (`curl`, crawlers) específicamente,
  no un bloqueo de IP ni nada que afecte al tráfico real de la app. **No era un
  problema real, nunca lo fue para un usuario** — era un artefacto de cómo se estaba
  probando. Ver `docs/bitacora.md` para el detalle completo. Lección para el futuro: si
  hace falta verificar de nuevo si AIC responde, probar directo en la app/dispositivo
  real primero — `curl` puede dar un falso positivo de "está caído".

- [x] ~~Conectar el celular e instalar/verificar todo lo acumulado desde la tanda 25 de
  movimiento~~ — hecho el 2026-08-28. Al instalar, el sync automático pisó la
  clasificación de movimiento de las tandas 25-32 (2651 → 1943 en el dispositivo, el
  APK bundleaba los datos correctos — confirmado desunzipeándolo — pero el último
  release publicado era de antes de esas tandas). Se republicó el release del mismo día
  (se borró el viejo con `gh release delete` y se recreó con el catálogo completo
  actual) y se reinstaló limpio: **2651 clasificadas, 39 icónicas**, estable después de
  esperar. Mismo patrón que el hallazgo del 2026-08-28 (continuación 2) — si esto sigue
  pasando cada vez que se retoman las tandas, considerar automatizar "publicar release"
  como último paso del pipeline en vez de un paso manual aparte.

- [x] ~~Reconectar el celular de prueba, instalar y verificar en vivo los cambios del
  2026-08-27~~ — hecho. Verificado directo contra la base del dispositivo (`run-as` +
  `cat`): "The Bedroom" de Van Gogh presente y bien clasificado, CMA/Met/Rijksmuseum
  calzan exacto. Salió un hallazgo nuevo (ver siguiente ítem).

- [x] ~~Publicar un release nuevo en GitHub con los datos de la semana~~ — hecho el
  2026-08-28 (`data-20260828`, github.com/pacohurtadof/art-daily/releases). El sync
  automático venía pisando datos de la app de prueba con un release viejo (dos features
  verificados a medias por esto — ver `docs/bitacora.md`). En vez del `delta.json`
  incremental de la última corrida (que solo hubiera traído la última búsqueda, no toda
  la semana), se publicó un delta con el **catálogo completo actual** (10373 obras) para
  que cualquier dispositivo quede al día en un solo sync. Verificado: instalación limpia
  se mantiene estable, ya no se pisa. Si se vuelve a publicar en el futuro y
  `publish-release.sh` toma el delta incremental de la última corrida nada más, revisar
  si conviene seguir generando el delta completo a mano o si ya alcanza con el
  incremental (depende de si pasó mucho tiempo/muchas cosechas desde el último release).

- [ ] **Evaluar agregar nuevas fuentes de museos para obras verdaderamente icónicas
  puntuales** que las 4 fuentes actuales (Met/AIC/CMA/Rijksmuseum) no tienen — ej. la
  Mona Lisa y La última cena (Louvre/Uffizi/Santa Maria delle Grazie), Guernica (Reina
  Sofía), La joven de la perla (Mauritshuis), La noche estrellada (MoMA), Las meninas y
  El 3 de mayo de 1808 (Prado). Candidatos a evaluar: Rijksmuseum ya cubre parte de
  Holanda; faltaría investigar si Louvre/Prado/MoMA/Reina Sofía/Mauritshuis tienen APIs
  de open access CC0 (varias probablemente no — el Prado y el Louvre no son conocidos
  por tener open data así de generoso, a diferencia de Met/AIC/CMA/Rijks). **Solo
  evaluar, no decidir de antemano** — si alguna fuente nueva es viable, coordinar con el
  usuario antes de integrarla (afecta la arquitectura del harvester, ver CLAUDE.md).
  Nota aparte: Picasso/Matisse tienen poca disponibilidad CC0 en general por derechos de
  autor vigentes (no es solo un problema de fuente), eso no se resuelve agregando museos.

- [x] ~~Conseguir más obras de artistas populares~~ — hecho el 2026-08-28. Se cosecharon
  14 artistas más (Sargent, Klimt, Munch, Turner, Dürer, Bosch, Bruegel, Velázquez,
  Gauguin, Toulouse-Lautrec, Degas, Renoir, Hokusai, Hiroshige). Catálogo: 9612 → 10373
  obras. Munch pasó de 7 a 103 e incluye **"El grito"** (aic:17229) — verificado en vivo
  en el dispositivo. Klimt siguió en 0 (no hay obra suya disponible en estas 4 fuentes).
  Ver `docs/bitacora.md` ("continuación 7") para la tabla completa antes/después.

- [x] ~~Seguir ampliando artistas populares (segunda ronda)~~ — hecho el 2026-08-31.
  Cassatt (3→48) y Rubens (7→17) dieron obras nuevas de verdad; Seurat, Vermeer, Chagall
  y Schiele no aportaron nada (ya estaban al tope de lo que estas 4 fuentes tienen, o
  siguen con derechos de autor vigentes — Chagall murió 1985, Schiele curiosamente
  debería ser dominio público desde 1988 pero simplemente no está en estas colecciones).
  Kandinsky +4. Catálogo: 10373 → 10473 obras. Frida Kahlo/Warhol/Dalí no se intentaron
  — misma razón de derechos de autor que Picasso/Matisse/Chagall, altísima probabilidad
  de rendir 0. Verificado en vivo el mismo día (instalación limpia, 10473 confirmadas en
  el dispositivo).

- [x] ~~Seguir ampliando artistas populares (tercera ronda)~~ — hecho el 2026-08-31.
  Rousseau (9→50), Vuillard (19→64) y Moreau (1→24) fueron los hallazgos grandes;
  Bonnard, Millais, Holman Hunt, Mondrian, Frans Hals, Bonheur, Rossetti y Burne-Jones
  aportaron algo menos pero real; Leighton y Grant Wood en 0 (ninguna obra de "American
  Gothic" disponible en estas 4 fuentes). Catálogo: 10473 → 10939 obras. Release
  `data-20260831` publicado en GitHub con el catálogo completo. Verificado en vivo el
  mismo día (instalación limpia, 10939 confirmadas en el dispositivo, conteos por
  artista exactos, 39 icónicas intactas).

- [ ] **Ampliar la curaduría de obras "icónicas"** (`harvester/data/iconic-overrides.txt`,
  feature nuevo del 2026-08-28 — ver `docs/bitacora.md`). Primer lote: 39 obras a mano
  (The Scream, The Bedroom, The Great Wave, A Sunday on La Grande Jatte, las tres
  Meisterstiche de Durero, etc.). Mismo espíritu que las tandas de movimiento — seguir
  revisando el catálogo obra por obra, sin adivinar, para artistas ya bien cosechados
  (Rembrandt, Turner, Hokusai/Hiroshige, Toulouse-Lautrec, Gauguin, Munch tienen mucho
  margen todavía). Al agregar obras nuevas al `.txt`, correr el mismo `UPDATE ... SET
  isIconic = 1 WHERE id = ?` directo sobre `harvester/output/artworks.db` (no hace falta
  re-cosechar), copiar a `assets/`, y publicar un release nuevo si se quiere que llegue
  a dispositivos ya instalados sin reinstalar.

- [ ] **Clasificar movimiento obra por obra de las ~603 obras nuevas agregadas el
  2026-08-27** (Van Gogh, Rembrandt, Goya, Monet, Cézanne, Tiziano, Rafael, etc. — ver
  `docs/bitacora.md` para el detalle exacto por artista). Es una extensión del trabajo ya
  en curso (ver CLAUDE.md, punto 15, "Clasificar movimiento a mano, obra por obra") — usa
  el mismo mecanismo (`harvester/data/movement-overrides.csv` + tandas de revisión
  manual). Algunas ya van a tener movimiento automático real (AIC trae `style_title` —
  ej. "El dormitorio" de Van Gogh ya quedó con "Postimpresionismo" solo, sin trabajo
  manual); lo que falta es sobre todo Met/CMA/Rijks, que no traen ese campo.
