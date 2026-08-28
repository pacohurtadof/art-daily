# TODO — ArtDaily

Lista de tareas pendientes puntuales. Para el resumen de etapas del proyecto y decisiones
ya cerradas, ver `CLAUDE.md` (raíz del repo) — ese archivo no se duplica acá. Para el
detalle día a día de lo ya hecho, ver `docs/bitacora.md`.

## Pendientes abiertos

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
  Ver `docs/bitacora.md` ("continuación 7") para la tabla completa antes/después. Si se
  quiere seguir ampliando, candidatos sin cosechar todavía: Cassatt, Seurat, Manet
  (reforzar), Rubens, Vermeer (reforzar), Chagall, Schiele, Kandinsky.

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
