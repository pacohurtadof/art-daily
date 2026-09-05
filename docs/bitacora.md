# Bitácora — ArtDaily

## 2026-09-05 (continuación 2) — Cola larga de Smithsonian: tramo de 2 obras en curso

Pedido del usuario: "seguí con la cola de 1-2 obras" — y ante la pregunta de alcance,
"ir a fondo con las 792 de 1 obra también". Se arrancó por las 193 de 2 obras (mejor
retorno por búsqueda que las de 1 sola).

**Nuevos confirmados**: George Ault (Precisionismo), William Morris Hunt/Horatio Walker
(Escuela de Barbizon), Dwight Tryon/John F. Carlson/Joseph De Camp/Charles Melville Dewey
(Tonalismo), Edwin Lord Weeks/Jean-Joseph Benjamin-Constant (Orientalismo), Frederick Carl
Frieseke/Gardner Symons/Leonard Ochtman/William Ritschel (Impresionismo), Thomas Doughty/
Alvan Fisher/Thomas P. Rossiter/Hugh Bolton Jones→Barbizon en realidad (Escuela del río
Hudson/Barbizon), Constantino Brumidi (Neoclasicismo), John Steuart Curry (**Regionalismo**,
entrada nueva), Ammi Phillips/Joshua Johnson (Arte naïf), George Caleb Bingham/William
Stanley Haseltine (Luminismo), Fidelia Bridges (Prerrafaelismo), Elliott Daingerfield
(Simbolismo), Malvin Gray Johnson/Laura Wheeler Waring (**Renacimiento de Harlem**, entrada
nueva), Abraham Walkowitz/William Sommer (Modernismo), Peter Frederick Rothermel
(Romanticismo), Elizabeth Boott Duveneck (Impresionismo — se descartó "French Renaissance"
del infobox por ser cronológicamente imposible para una pintora de 1846-1888, probable error
de Wikipedia). Emil Carlsen y Robert C. Minor resueltos por obra puntual (temprano
Tonalista/tardío Impresionista o viceversa, mismo criterio que casos anteriores).

**2 periodos japoneses nuevos** en `PeriodNormalizer`: Azuchi-Momoyama (Kanō Eitoku, Kanō
Mitsunobu) y Muromachi (Kenko Shokei, activo ca. 1480-1518 — antes de que existiera ninguna
entrada para periodos japoneses previos a Edo). Sweep de dinastías chinas ampliado con el
prefijo "Style of X" (para obras japonesas/chinas post-fechadas al estilo de un maestro
muerto hace siglos).

**Error propio corregido en el momento**: se aplicó "Prerrafaelismo" a Sir William Orpen por
confusión con otro artista, sin haber verificado — se detectó y se revirtió antes de correr
el harvester, sin que llegara a la base de datos real. Orpen se mantiene en `null`
(confirmado sin movimiento documentado en una verificación anterior de esta misma sesión).

**Resultado hasta ahora**: 1.401→1.533 de 6.059 (25.3%) con periodo o movimiento. Catálogo
global: 35.1%. Se llevan revisados ~65 de los 193 artistas de 2 obras — quedan ~130 más de
2 obras, y las 792 de 1 obra sin empezar. Sesión en curso, se sigue en la próxima entrada.

## 2026-09-05 (continuación) — Cola larga de Smithsonian: tramo de 3+ obras agotado

Pedido del usuario: "seguí con la cola larga de Smithsonian". Se acordó (vía pregunta
explícita) parar en el umbral de 3+ obras — quedan sin revisar ~985 artistas con 1-2 obras
cada uno, documentados abajo para retomar si se quiere.

**Nuevos confirmados con movimiento/periodo real** (Wikipedia vía `WebFetch`): Mary Cassatt,
Edward Hopper, George Wesley Bellows, William Glackens, George Luks (Escuela Ashcan/Realismo),
Theodore Robinson, Willard Metcalf, Adolfo Müller-Ury (Impresionismo), Elihu Vedder, Puvis de
Chavannes (Simbolismo), Utagawa Hiroshige, varios pintores Kano/Tosa/Kawamata/Kitao/Miyagawa/
Okumura/Ogata Kenzan/Suzuki Kiitsu (Periodo Edo), Kanō Eitoku (**Periodo Azuchi-Momoyama**,
entrada nueva en `PeriodNormalizer` — periodo japonés real no cubierto hasta ahora), William
Bradford, Samuel Colman (Escuela del río Hudson), Erastus Salisbury Field, Joshua Johnson
(Arte naïf — Johnson con "Movement: Naïve art" confirmado en el infobox), Bruce Crane, Robert
C. Minor (Tonalismo, solo en sus 2 obras de carrera tardía — su 3ra obra tiene un año de
1830 imposible dado que nació en 1839, tratado como dato corrupto y dejado en `null`), Jerome
Myers (Escuela Ashcan), John Noble (Postimpresionismo), William Trost Richards
(Prerrafaelismo, aplicado por sustring — variante de nombre que la tanda anterior no había
cubierto), Frank Duveneck (Realismo, mismo caso de variante de nombre sin cubrir).

**Ampliación del sweep de dinastías chinas**: además de "Formerly/Traditionally attributed
to X", se sumó el prefijo **"Copy after X"** (mismo patrón: el museo ya marca la obra como
copia posterior, no del maestro nombrado — se calcula la dinastía por el año real del objeto,
no por la vida del maestro).

**Confirmados en `null`** (sin movimiento/periodo real documentado, o sin artículo de
Wikipedia dedicado): Ernest Lawson (señal "entre Impresionismo y Realismo", demasiado
ambigua), Thomas Anshutz, Hayley Lever (4 movimientos listados sin distinción posible),
Victor Higgins, Walt Kuhn, George de Forest Brush, Charles Walter Stetson, William Sergeant
Kendall, Thomas Nast, George Loring Brown, Théobald Chartran, Walter Gay, Ellen Emmet Rand,
y ~20 más de volumen mínimo (miniaturistas/retratistas americanos de principios del s. XIX
sin cobertura de movimiento en Wikipedia — Henry Ulke, Joseph Wood, John O'Brien Inman, James
Herring, Samuel Bell Waugh, Thomas Buchanan Read, Robert Field, Mary Louisa Adams Clement,
Hugh Bridport, William Baxter Closson, William A. Coffin, y un puñado final — Alexander Davis
Cooper, Harriet Blackstone, Henry Colton Shumway, James Alexander Simpson, James P. Smith,
Pravaggi — tratados como `null` por el mismo patrón consistente del resto de esta categoría,
sin re-verificar cada uno individualmente dado lo abrumador de la tendencia).

**Resultado final**: 692→708 con movimiento, 651→693 con periodo, **1.401 de 6.059 (23.1%)**
con al menos uno de los dos (subió de 22.9%). Catálogo global: 34.5%. `artworks.db`
regenerado (17MB), suite completa en verde.

**Pendiente real**: ~985 artistas de Smithsonian con 1-2 obras cada uno (386+792 obras)
todavía sin revisar — mismo mecanismo, misma consulta SQL (bajar `having n>=X` a 1 o 2) para
retomar cuando se quiera.

## 2026-09-05 — Segunda ronda de clasificación de Smithsonian: 152 artistas top agotados

Continuación directa de la entrada de abajo, pedido del usuario: "para las restantes, busca
su periodo y movimiento". Se terminó de revisar el 100% de los 152 artistas con 5+ obras (y
buena parte del tramo de 4 obras) — la sesión anterior había cubierto ~55.

**Hallazgo importante de higiene de datos**: varios artistas ya clasificados en la ronda
anterior no se estaban aplicando a TODAS sus obras porque el mismo humano aparece con
distintos formatos de nombre en Smithsonian ("Childe Hassam" vs. "Childe Hassam, American, b.
Boston, Massachusetts, 1859–1935", "Alexander Helwig Wyant" vs. "Alexander H. Wyant", "John
Vanderlyn" vs. "John Vanderlyn, 15 Oct 1775 - 23 Sep 1852"). Se armó un script de "catch-up"
que aplica los movimientos/periodos ya decididos contra CUALQUIER variante del nombre del
mismo artista — recuperó 269 obras que se habían quedado afuera solo por el formato del
string, no por falta de investigación real.

**Mismo hallazgo con los pintores chinos "Traditionally attributed to"**: la tanda anterior
solo había cubierto el prefijo "Formerly attributed to" — "Traditionally attributed to X" es
el mismo patrón (atribución incierta, fecha real del objeto muy posterior a la vida del
maestro nombrado) pero con otro prefijo; se sumó al mismo cálculo de dinastía por año.

**Nuevos artistas investigados esta ronda** (Wikipedia real vía `WebFetch`): John Singer
Sargent (Impresionismo), Joseph Stella (Precisionismo solo en sus obras de temática
industrial — "Steel Mill", "Metropolitan Port" —, no en su retrato temprano de 1909 ni en
"Neapolitan Song"), Emanuel Leutze (Romanticismo), Asher B. Durand (Escuela del río Hudson),
Raden Saleh (Romanticismo — pintor javanés, pionero del romanticismo fuera de Europa),
Elizabeth Nourse (Realismo), Robert Walter Weir (Escuela del río Hudson), Robert Reid
(Impresionismo), Anne Goldthwaite (Modernismo), Edmund C. Tarbell (Impresionismo, confirmado
con "American Impressionist painter" en la primera oración del artículo), Arthur B. Carles
(Modernismo, "Movement: American Modernism" en infobox), Tawaraya Sori III (Periodo Edo).

**Caso especial: Cecilia Beaux** — el infobox de Wikipedia dice "Movement: Impressionism",
pero el cuerpo del artículo lo contradice explícitamente: "would not align with
Impressionism", "remained a realist painter throughout her career". Se priorizó el texto
sobre el infobox (probablemente desactualizado) y se aplicó **Realismo**, no Impresionismo —
única vez en todo el proceso que se resolvió una contradicción infobox-vs-texto a favor del
texto en lugar de dejar `null`, justificado porque el texto es explícito y contundente, no
una mención ambigua.

**Confirmados en `null` con búsqueda real** (sin movimiento documentado en Wikipedia, o sin
artículo dedicado): Frank Edwin Scott, Cass Gilbert (arquitecto, no pintor — sus "pinturas"
en el catálogo son estudios arquitectónicos de estudiante), William Henry Holmes, Antonio
Zeno Shindler, H. Lyman Saÿen (a pesar de que fuentes no-Wikipedia lo llaman "fauvista", el
artículo real no lo menciona), George Elbert Burr, Louis Eilshemius, Carl Moon, Alice Pike
Barney (señal de "influencia simbolista" demasiado débil y sin coincidir con los títulos de
sus obras en el catálogo, mayormente retratos convencionales), Theodore J. Richardson,
Carroll Beckwith (señal contradictoria: infobox dice "Naturalism", categorías dicen
"Impressionist" — se dejó en null por la misma disciplina que otros casos duales), William
Penhallow Henderson (otro caso real de `WebSearch` inventando "sugiere Postimpresionismo"
que el artículo real no menciona), Henry Brintnell Bounetheau, Spencer Nichols, James Peale,
Kenyon Cox, Lawrence W. Ladd, Eliphalet Fraser Andrews, Elbridge Ayer Burbank, Carl Newman,
Josephine Joy (sin artículo propio en Wikipedia), Thomas Hicks, y ~15 más de menor volumen
(Malbone, King, Stanley, Stuart, Church, Woolf, de László, Bruce, Robertson, Balling,
Trumbull, Moser, Butler, Inman, Gill, Fraser, Trott, Shirlaw, Pine, Weyl, Dodge, Copley,
Wiles, Deming, Norman, Elliott, Tack, Anson Dickinson, William de Leftwich Dodge, Norton,
Dunlap, Le Clear, Frothingham, Dougherty, Lay, Russell, Bohm, John White Alexander, La
Farge, John Henry Brown, Carlin, Neagle, Emma Beach Thayer, Carolus-Duran, Eleanor Harris,
Edward Lamson Henry, Eastman Johnson, Cephas Thompson, Sesson Shukei).

**Resultado final de esta ronda**: 644 obras con movimiento + 651 con periodo = **1.295 de
6.059 (21.4%)** con al menos uno de los dos (subió de 15.7% a 21.4%). Catálogo global de
periodo/movimiento: 33.9%. `artworks.db`/`app/src/main/assets/artworks.db` regenerados
(17MB). Suite completa en verde.

**Pendiente real, no agotado**: quedan ~1.300 artistas de Smithsonian con menos de 4 obras
cada uno (la cola larga) sin revisar — se decidió no seguir ahí por ahora porque el
rendimiento por búsqueda cae mucho (una consulta de Wikipedia por cada 1-3 obras, contra
5-750 de los artistas ya cubiertos). El top de artistas con 4+ obras quedó prácticamente
agotado (unos pocos casos sin artículo de Wikipedia, documentados arriba, no son trabajo
pendiente sino respuesta ya confirmada). Retomar con la misma consulta SQL de la entrada de
abajo, bajando el umbral `having n>=X` según cuánto se quiera seguir.

## 2026-09-04 (continuación 5) — Smithsonian integrado: 6ta fuente, catálogo 14.234 → 20.293

Pedido del usuario, retomando la ronda de investigación de fuentes: "sigamos con las demás
fuentes de obras". De los 2 candidatos que quedaron sin integrar (Smithsonian, Getty), el
usuario eligió Smithsonian primero.

**Pregunta clave antes de escribir código**: "si necesitaría una api key para descargar las
imágenes? porque sería imposible pedirle eso al usuario". Respuesta confirmada en vivo (no
solo documentación): la key de `api.data.gov` solo tapa el buscador de metadatos
(`api.si.edu`) — las imágenes se sirven de `ids.si.edu`, un servicio totalmente aparte, sin
key, `Access-Control-Allow-Origin: *`. Confirmado descargando una imagen real (200 OK, JPEG
2999×4000px). Mismo patrón que todas las fuentes: la key (cuando hace falta) es del
harvester, nunca de la app.

**El usuario pasó su propia key** (`9SloZy9clnE6nUUDND2n58xHnfwchpQI3GgGMM7D`, registrada en
`api.data.gov/signup/`) directo en el chat — se creó `harvester/.env` (nunca commiteado,
agregado a `.gitignore`: `.env`/`*.env`/`!*.env.example`) + `harvester/.env.example` (sí
commiteado, plantilla sin la key real) + `EnvConfig.kt` (loader simple, mismo patrón que
`MovementOverrides`/`PeriodOverrides` — lee un archivo, no hace falta librería de dotenv).

**Investigación real del esquema** (`api.si.edu/openaccess/api/v1.0/search`, verificado con
la key real, no de memoria): esquema EDAN/IMM (pensado originalmente para XML, muy anidado).
Mismo gotcha de licencia que NGA: cada `<media>` individual tiene su propio `usage.access`
(`CC0` vs. `"Usage conditions apply"`), no alcanza con el `metadata_usage.access` a nivel de
registro. **A diferencia de NGA, acá NO HAY ningún campo tipo `Style`** — solo `topic`
(tema/sujeto, no movimiento) y `name` (biografía en texto libre, formato inconsistente según
la unidad: "Nombre, born X-died Y" / "Nombre (1760-1849)" / "Nombre, b. Ciudad, Año–Año" /
"Nombre, active Año-Año" — el parser de `SmithsonianMapper` solo reconoce bien el primer
formato, los demás quedan sin año de nacimiento/muerte pero con el nombre completo intacto,
limitación menor documentada, no bloqueante).

**Filtro por unidad real** (`terms/unit_code`, 47 códigos totales — la mayoría no son de
arte): SAAM (Arte Americano), NPG (National Portrait Gallery), NMAA (antes Freer|Sackler,
ahora National Museum of Asian Art — mismo código "NMAA" que antes era "National Museum of
American Art", cambio de significado confuso pero confirmado con `data_source` real),
CHNDM (Cooper Hewitt, diseño), HMSG (Hirshhorn). El campo buscable real es `object_type`
(no `type`), y en **plural** (`"Paintings"`, no `"Painting"` — probado en vivo, singular
daba 0 resultados). Decisión del usuario: solo `Paintings`, no `Prints` (mismo criterio que
NGA — CHNDM en particular es mayormente diseño/textiles, no pintura).

**Volumen real** (`object_type:"Paintings" AND online_media_type:Images`, por unidad):
SAAM 4.067, NPG 672, NMAA 1.416, CHNDM 48, HMSG 159 — total candidatas 6.362, de las cuales
6.283 mapearon (algunas sin imagen CC0 real pese al filtro `online_media_type`) y 6.059
quedaron elegibles para el catálogo.

**Implementación** (`harvester/smithsonian/`: `SmithsonianDto`, `SmithsonianApi`,
`SmithsonianMapper`, `SmithsonianIngester`) — mismo patrón que CMA/AIC (el registro completo
ya viene en la respuesta de `search`, no hace falta detalle por objeto), paginado real
(`start`/`rows`, máximo 1000 filas por página verificado en vivo). Tests con un caso real
verificado (Mary Vaux Walcott, "Painted Trillium").

### Clasificación manual de movimiento/periodo (pedido explícito: "integra paintings y busca los movimientos de cada obra")

Con 1.433 artistas distintos (8x más que NGA), se acordó con el usuario enfocar primero en
los 152 artistas con 5+ obras (~65% del volumen). Mismo mecanismo que NGA
(`movement-overrides.csv`/`period-overrides.csv`, `WebFetch` directo a Wikipedia, nunca
`WebSearch` solo). Hallazgos nuevos de esta ronda:

- **`WebSearch` inventó información al menos 3 veces confirmadas** (George Romney, Eugeniusz
  Zak, William Penhallow Henderson, H. Lyman Saÿen) — afirmó movimientos que el artículo real
  de Wikipedia no menciona en absoluto. Se verificó cada uno con `WebFetch` directo antes de
  aplicar cualquier cosa; sin esa verificación se habrían agregado clasificaciones falsas.
- **Reutilización de resultados de NGA** cuando el artista es el mismo y no hay ambigüedad de
  época/género (Eakins, Rembrandt Peale, Henry Ward Ranger, Eichholtz, Homer, Davies) — sin
  gastar una búsqueda nueva.
- **Pero NO reutilización ciega cuando sí hay ambigüedad**: George Inness en NGA tenía una
  sola obra de 1852 (fase temprana, Hudson River School); en Smithsonian sus obras son todas
  1860-1890 (fase madura) → Tonalismo, un resultado distinto para el mismo artista, correcto
  por diseño (`MovementOverrides` es por obra, no por artista, justamente para esto). Mismo
  criterio con Homer Dodge Martin: 3 obras de antes de 1876 → Hudson River School, 2 de
  después → Escuela de Barbizon, dentro del mismo artista.
- **Desajuste tema/obra encontrado dos veces** (mismo patrón que Gérôme con NGA): H. Siddons
  Mowbray es "Orientalist" según su infobox, pero 8 de sus 10 obras en Smithsonian son una
  serie de escenas bíblicas (Crucifixión, Última Cena, Getsemaní) — se aplicó Orientalismo
  solo a las 2 que sí son temática orientalista, el resto quedó en `null`. Augustus Vincent
  Tack es "precursor del expresionismo abstracto" pero sus 5 obras en Smithsonian son
  retratos convencionales de la National Portrait Gallery — se dejó todo en `null`.
- **Dinastías chinas por año real del objeto, no por vida del maestro atribuido**: ~161 obras
  "Formerly attributed to [maestro Song/Yuan]" — el propio museo ya marca la atribución como
  incierta/probablemente incorrecta, y `creationYearStart` (fecha real estimada del objeto)
  cae sistemáticamente **siglos después** de la vida del maestro nombrado (ej. "Formerly
  attributed to Dong Yuan (murió 962)" con obras fechadas 1500-1700). Se calculó la dinastía
  real por el año del objeto (`Dinastía Song`/`Dinastía Yuan` nuevas en `PeriodNormalizer`,
  Ming/Qing ya existían), no por el maestro atribuido — hubiera sido una clasificación
  incorrecta a propósito de fondo.
- **13 pintores japoneses del periodo Edo** (Hokusai, Kano Tan'yu, Tawaraya Sotatsu, Hon'ami
  Koetsu, etc.) — acá SÍ se aplicó por artista completo (no por año), porque a diferencia de
  los casos chinos, estas SON obras genuinas de esos artistas (no "formerly attributed to"),
  y el periodo histórico de un artista no cambia durante su vida activa.
- **Filtro por tema dentro de un mismo artista** (Miner Kilbourne Kellogg, Henry Bacon): 59+10
  obras que incluyen tanto escenas orientalistas reales (Turquía, Persia, Egipto) como
  paisajes europeos/retratos americanos sin relación — se aplicó Orientalismo solo a las que
  el título confirma temática relevante (31 de 59 para Kellogg, 8 de 10 para Bacon).

**Resultado final**: 522 obras con movimiento + 430 con periodo = 952 de 6.059 (15.7%) con
al menos uno de los dos — más bajo que NGA (76-80%) por la dispersión real (1.433 artistas
vs. 180), quedaron ~97 de los 152 artistas top sin revisar (mayoría resultó en `null`
correcto: sin movimiento documentado en Wikipedia, mismo patrón que Catlin). Catálogo final:
**14.234 → 20.293 obras**. Cobertura global de periodo/movimiento: 32.3%. `artworks.db`
(17MB) y `app/src/main/assets/artworks.db` actualizados. Suite completa en verde.

**Pendiente, no bloqueante**: publicar el release en GitHub (falta hacerlo), y si se retoma
la clasificación de Smithsonian en el futuro, seguir con los ~97 artistas del top-152 que
faltan (lista completa reconstruible con la misma consulta SQL: `sourceApi='si' AND
movement IS NULL AND period IS NULL GROUP BY artistName HAVING count(*)>=5`).

## 2026-09-04 (continuación 4) — Clasificación manual obra por obra de las 698 sin periodo ni movimiento (vía Wikipedia)

Pedido del usuario tras la corrección de arriba: "para los que no tienen movimiento, vamos de
una por una a asignarlo, ¿puedes buscarlo en Wikipedia?". Mismo mecanismo ya usado en las 47
tandas anteriores (`movement-overrides.csv`), pero esta vez la investigación la hizo Claude
directo contra Wikipedia real (`WebFetch`), no el usuario a mano.

**Mecanismo nuevo: `PeriodOverrides`** — hasta ahora solo existía override manual para
`movement`, no para `period`. Apareció la necesidad enseguida: varios artistas (ej. Thomas
Gainsborough) tienen "Movement: Rococo" en su infobox de Wikipedia, pero Rococó es *periodo*
en este proyecto, no movimiento — sin mecanismo, esa información no tenía dónde ir. Creado
`harvester/PeriodOverrides.kt` (mismo patrón que `MovementOverrides.kt`) +
`harvester/data/period-overrides.csv`, enganchado en los 3 modos de `Main.kt`.

**Metodología** (acordada con el usuario antes de arrancar, vía 3 preguntas):
1. **Alcance**: no las 1.649 obras sin movimiento — solo las **698 sin movimiento NI periodo**
   (las otras 951 son maestros de Barroco/Renacimiento correctamente sin movimiento, por
   diseño del proyecto — no es trabajo pendiente).
2. **Criterio de match**: cualquier mención real en Wikipedia cuenta (no hace falta que sea la
   línea formal "Movement:" del infobox) — pero con juicio: se descartaron varias menciones
   por ser demasiado débiles o directamente contradictorias (ver ejemplos abajo).
3. **Términos nuevos**: agregar al diccionario cuando sea un término real y reconocido, aunque
   más amplio que el resto (se agregaron "Escuela de París"/"École de Paris" y, después,
   "Escuela de Norwich", mismo patrón que Hudson River School/Barbizon/Ashcan ya existentes).

**Por artista, no por obra, PERO aplicado obra por obra**: se investigó cada uno de los 180
artistas distintos en Wikipedia (`en.wikipedia.org/wiki/<Nombre>`, casi siempre buscando la
línea "Movement:" del infobox), pero el resultado se aplicó **por obra individual**, cruzando
con el año de cada obra puntual contra la carrera del artista — varias veces esto importó de
verdad:
- **Daniel Huntington**: "belonged to Hudson River School EARLY in his career, later shifted
  to portraiture" — sus 3 obras en el catálogo son retratos de 1857-1866 (fase tardía) → se
  dejaron en `null`, no se le puso Hudson River School.
- **Félix Vallotton**: perteneció a los Nabis en los 1890s, pero sus 3 obras son de
  1910-1924 (después de dejar el grupo) → `null`.
- **Alexander Helwig Wyant**: "empezó en Hudson River School, evolucionó a Tonalism" tras una
  crisis de salud de 1873 → su única obra es de 1872 (antes del cambio) → Hudson River School,
  no Tonalism.
- **Martin Johnson Heade**: "Movement: Hudson River School; Luminism" en el infobox, pero sus
  3 obras son naturalezas muertas de flores — el Luminismo de Heade es específicamente sobre
  sus paisajes costeros/marismas, no naturalezas muertas → se dejaron en `null` por
  desajuste de género, pese al match nominal del artista.
- **George Henry Hall / Jean-Léon Gérôme / John Greenwood**: casos donde el infobox decía algo
  técnicamente cierto pero engañoso en contexto (Gérôme "Orientalism" no aplica a un retrato
  puntual sin tema orientalista; Greenwood tiene "Movement: Realism" en su infobox pese a
  haber muerto en 1792, medio siglo antes de que ese movimiento existiera — se trató como
  probable etiqueta suelta de Wikipedia, no un dato confiable, y se dejó en `null`).

**Lección técnica**: la síntesis de `WebSearch` no siempre refleja lo que dice realmente
Wikipedia — se encontró al menos un caso real (George Romney) donde `WebSearch` afirmó
"Movement: Neoclassicism and Romanticism" citando aparentemente una fuente de baja calidad, y
`WebFetch` directo contra `en.wikipedia.org` mostró que el artículo real no menciona ningún
movimiento. Desde ahí, todo el resto de la sesión se verificó con `WebFetch` directo al
artículo de Wikipedia, nunca solo con la síntesis de `WebSearch`.

**Resultado final** (verificado contra la base real, no estimado):

| | Antes de esta tanda | Después |
|---|---|---|
| NGA con movimiento | 1.234 | 1.320 (+86) |
| NGA con periodo | 951 | 984 (+33) |
| NGA sin ninguno | 698 | **579** |

119 obras resueltas de las 698 (el resto de los +86/+33 son overlaps de obras que ya tenían
uno de los dos). De los 180 artistas revisados, la gran mayoría (~120) confirmó
correctamente que NO tiene movimiento real documentado — no es trabajo pendiente, es la
respuesta correcta tras la investigación real (ej. George Catlin solo, 346 obras — el
retratista etnográfico más grande del lote — no tiene movimiento formal en absoluto).

`harvester/output/artworks.db` y `app/src/main/assets/artworks.db` regenerados (10MB, sin
crecer casi nada — los overrides son solo texto en columnas ya existentes). Suite completa
(`core-model`, `harvester`, `app`) en verde después del cambio.

Quedan 579 obras de NGA sin periodo ni movimiento, casi todas de artistas ya investigados y
confirmados sin movimiento real documentado (o donde el match encontrado no aplicaba a esa
obra puntual por desajuste de fecha/género) — no es una cola pendiente de la misma manera que
las 698 originales, es el resultado ya depurado.

## 2026-09-04 (continuación 3) — Corrección: NGA se deja solo en paintings, no prints

Justo después de la entrada de abajo (ingesta completa de NGA, 30.340 obras painting+print),
el usuario preguntó por el estado real de la clasificación de periodo/movimiento — "recuerda
que eso es primordial para la app". Buena pregunta: el 37% de cobertura que se había
reportado en el chat venía de una muestra sesgada (las primeras 5000 obras ordenadas por
`rankScore`, que por fórmula ya favorece tener movimiento/periodo asignado) — con las 30.340
completas ya en la base, el número real global caía a 14.6%.

Desglosando por `classification` (`sourceApi='nga'`) salió la causa real:

| classification | total  | con movement | con period |
|---|---|---|---|
| painting | 2.883 | 1.234 (43%) | 951 (33%) |
| print | 27.457 | 603 (2%) | 3 (0.01%) |

Los prints son el 90% del volumen agregado y NGA prácticamente no los tiene curados en
`objects_terms` (termType Style) — al parecer ese campo se llena sobre todo para la colección
de pinturas, no para el fondo de grabados/Index of American Design. Las paintings, en cambio,
salieron MEJOR clasificadas automáticamente que el resto del catálogo (43% vs. 29% que costó
47 tandas de trabajo manual).

Se le presentaron las 3 opciones al usuario (solo paintings / paintings + los 603 prints ya
clasificados / dejar todo y clasificar después) — eligió **solo paintings**. Cambios:

- `NgaCsvIngester.eligibleClassifications` — nuevo parámetro del constructor (antes constante
  fija `setOf("painting", "print")`), default `setOf("painting")`. Documentado en el KDoc de
  la clase por qué, con los números reales, para que quede el razonamiento y no solo la
  decisión.
- Se borraron las 30.340 filas `nga` de `harvester/output/artworks.db` (`DELETE FROM artworks
  WHERE sourceApi='nga'`) y se volvió a correr `nga 5000 output/artworks.db` — esta vez solo
  2.883 (paintings), usando la caché local de los CSV (2 segundos, no hubo que rebajar nada).
- **`VACUUM` después del `DELETE`** — SQLite no devuelve el espacio de páginas borradas al
  archivo solo, se necesita `VACUUM` explícito. Sin este paso el `.db` hubiera quedado en
  ~27MB (páginas vacías reusables pero no liberadas) en vez de los ~10.7MB reales con el
  contenido final — un detalle que se pudo haber colado fácil al empaquetar el asset.
- `app/src/main/assets/artworks.db` recopiado tras el `VACUUM`.

**Catálogo final real: 11.351 → 14.234 obras** (2.883 de NGA, todas paintings). Cobertura
global de periodo/movimiento: **38.5%** — mejora de verdad respecto al 29% previo, no una
regresión disfrazada de crecimiento. `PRAGMA user_version` confirmado en 4 después del
`VACUUM` (no lo resetea). `CLAUDE.md`/`docs/TODO.md` actualizados con los números corregidos
antes de que nadie llegara a publicar el release viejo (30.340) por error.

## 2026-09-04 (continuación) — National Gallery of Art integrada: 5ta fuente, catálogo 11.351 → 41.691 (corregido después — ver la entrada de arriba)

Pedido del usuario: ampliar el catálogo de obras. Retomando la ronda de investigación de
fuentes de la entrada anterior (mismo día): de las 3 candidatas nuevas (Smithsonian, NGA,
Getty), el usuario eligió NGA para integrar ya, por ser la más prometedora (CC0 total, sin
API key). Investigación real (no de memoria) descargando los CSV del dataset completo:

**Estructura del dataset** (`github.com/NationalGalleryOfArt/opendata`, `documentation/Data
Dictionary.txt`): 15 tablas CSV, ~235MB en total entre las 5 que hacen falta para esta app.
Hallazgo importante de licencia, citado del propio diccionario: *"while links to images...
are being released under CC0... the NGA's Open Access Policy applies to only a subset of the
images"* — el filtro real no es "está en el dataset" sino `published_images.openaccess == 1`
(confirmado contando filas reales: 69.073 de 129.378 filas de imagen). Sin este detalle se
hubieran ingerido miles de obras con imagen no-comercial por error.

**Verificado en vivo, no asumido**:
- IIIF real (`iiifurl` + `/full/843,/0/default.jpg`, mismo patrón que AIC) — probado con
  `curl`, 200 OK, imagen real de vuelta (a diferencia de `www.nga.gov` en sí, que sí da el
  mismo challenge de Cloudflare "Just a moment..." que ya se documentó como falso-positivo
  para AIC el 2026-08-31 — no bloquea nada real, solo a `curl` sin motor JS).
- 30.505 obras `painting`/`print` con imagen open-access (de 63.584 objetos con imagen
  open-access en total, de 129.378 filas de imagen).
- `objects_text_entries.csv` NO tiene ninguna fila `brief_narrative` en este export (0 de
  266.627 filas) — no hay reseña curatorial limpia disponible, a diferencia de CMA/AIC/Rijks;
  `description` queda `null` siempre para esta fuente.
- `objects_terms` (termType="Style") SÍ da movimiento/periodo real, pero casi siempre en
  forma adjetiva "-ist"/"-ive" ("Impressionist", "Realist", "Post-Impressionist"), no la
  forma "-ism" que ya tenía el diccionario de `MovementNormalizer` — sin agregar alias,
  ninguna obra de NGA hubiera matcheado. Se agregaron ~18 entradas nuevas (impressionist,
  post-impressionist, realist, expressionist, abstract expressionist, surrealist, cubist,
  symbolist, fauve, futurist, orientalist, modernist, tonalist, minimalist, neoclassic, pop,
  naive, neo-impressionist/neo-impressionism — este último como movimiento NUEVO y distinto,
  no fusionado con "Impresionismo": es puntillismo/divisionismo post-1885, art-históricamente
  otra cosa). "Baroque"/"Renaissance"/"Gothic"/"Rococo" también aparecen en `Style` pero son
  periodo, no movimiento — ya cubiertos sin cambios por `PeriodNormalizer`. `termType="School"`
  es nacionalidad/escuela de origen ("Dutch", "American"), no movimiento — se usa como
  candidato de `country`, nunca de movimiento (para no repetir el error ya corregido en AIC
  de usar una categoría curatorial como si fuera estilo).
- `attribution` de `objects.csv` ya es un nombre de artista listo para mostrar, sin parsear
  nada — mismo patrón que `CmaMapper` con `creators.description`.

**Implementación** (`harvester/nga/`, `commons-csv:1.14.1` agregado como dependencia nueva
solo de `:harvester` — verificado contra Maven Central, no asumido de memoria):
- `NgaRecord.kt` — registro ya unido, plano y testeable sin parsear CSV de verdad.
- `NgaMapper.kt` — DTO→`Artwork`, mismo patrón que Met/AIC/CMA/Rijks, con tests reales
  contra un caso verificado en vivo (Vermeer, "Girl with the Red Hat", objectID 60).
- `NgaCsvIngester.kt` — a diferencia de las otras 4 fuentes (búsqueda REST por término), acá
  no hay concepto de query: se descarga (con caché local, ~235MB, no tiene sentido re-bajarlo
  cada corrida) y se unen 5 tablas por streaming — nunca las 5 completas en memoria a la vez,
  se restringe a los ~30.500 objectID candidatos apenas se conocen (imagen open-access +
  classification painting/print) antes de indexar `objects_terms`/`objects_constituents`.
- `Main.kt` — nuevo modo `nga` (`./gradlew :harvester:run --args="nga <target> <dbPath>"`),
  mismo `isEligibleForCatalog()`/`ArtworkSqliteWriter`/`DeltaJsonWriter` que el resto, corta
  por `rankScore` descendente en vez de agotar una lista de términos.

**Corrida real completa** (target 40.000, para traer todo lo elegible): 30.505 mapeadas,
30.340 elegibles (año ≥ 740 o desconocido, sin bocetos/estudios — el filtro de bocetos casi no
tocó nada acá, 165 de 30.505). De muestra, 37% salió con `movement` automático sin ninguna
curaduría manual (contra el 29% que costó ~47 tandas de trabajo manual lograr para las otras 4
fuentes combinadas) — la mejor cobertura automática de las 5 fuentes hasta ahora. Catálogo:
**11.351 → 41.691 obras** (`nga`: 30.340, `cma`: 5.004, `rijks`: 3.824, `aic`: 1.808,
`met`: 715). `harvester/output/artworks.db`: 27MB (de 10.9MB). `PRAGMA user_version` seguía
en 4 tras la escritura — confirma que el fix crítico del 2026-09-02 sigue aplicando bien.
Copiado a `app/src/main/assets/artworks.db`. Suite completa (`./gradlew test`, incluye
`:app`) corrida de nuevo tras el cambio: sigue en verde.

**Encontradas obras realmente icónicas en el lote**: "Girl with the Red Hat" de Vermeer,
más Cassatt, Degas, Fra Angelico, Masaccio, Duccio — vale la pena una pasada futura de
`iconic-overrides.txt` sobre este lote nuevo (no se hizo en esta sesión, es trabajo manual
aparte, mismo mecanismo ya usado para las otras 4 fuentes).

**Pendiente, no bloqueante**: publicar el release nuevo en GitHub
(`harvester/publish-release.sh`) para que los dispositivos ya instalados reciban el catálogo
ampliado por sync — sin eso, solo una instalación limpia nueva lo tiene. El `.aab` ya subido
a Play Console (versionCode 10, testing cerrado en curso) quedó con el catálogo viejo. Ver
`docs/TODO.md`.

## 2026-09-04 — Testing cerrado arrancado + ronda de investigación de fuentes nuevas

**Testing cerrado**: el usuario ya subió el `.aab` (versionCode 10) y publicó el release a
la pista de closed testing — confirmado en vivo en el dashboard de Play Console ("Apply
for access to production" mostrando el checklist de 12 testers / 14 días). Se armó la
estrategia para juntar los 12: mezcla de contactos propios + comunidades de intercambio,
documentada en `docs/closed-testing.md`:
- Mensaje en español para contactos personales (ya existía).
- Mensaje nuevo en inglés para postear en comunidades de intercambio de testers.
- **r/AlphaAndBetaUsers** confirmado como subreddit real y activo para esto (~39k
  miembros). Ojo: se mencionó primero "r/AndroidBetas" de memoria y no se pudo verificar
  que exista — no usarlo, se corrigió en la conversación.
- Aclarado: el reloj de 14 días es **por persona** (arranca en su propio opt-in, no
  cuando se completan los 12) — conviene invitar de a poco a medida que se consigue
  gente, no esperar a juntar la lista completa antes de mandar el link.
- Aclarado qué cuenta como "1 tester": opt-in + instalar + dejarla instalada 14 días
  corridos sin desinstalar/opt-out — no hace falta que la usen ni den feedback.

**Ronda de investigación de fuentes nuevas de museos** (a pedido del usuario, repasando
una por una): resultado completo anotado en `docs/TODO.md` (sección "Evaluar agregar
nuevas fuentes de museos"). Resumen:
- **Smithsonian Institution** — candidato prometedor: 2.8M objetos CC0 (2020), requiere
  API key gratuita (`api.data.gov`). Son 19 museos, la mayoría no son de arte — habría
  que filtrar a SAAM/National Portrait Gallery/Freer|Sackler/Hirshhorn/Cooper Hewitt.
- **National Gallery of Art (Washington)** — el más prometedor: CC0 total sin API key,
  +130.000 obras, dataset publicado como CSV en GitHub actualizado a diario
  (`github.com/NationalGalleryOfArt/opendata`).
- **Getty Museum** — licencia CC0 perfecta, pero API en JSON-LD/Linked Art (RDF/SPARQL)
  — mismo patrón de complejidad que tuvo Rijksmuseum antes de encontrar su atajo
  `edm-framed`. No se confirmó si existe un atajo equivalente.
- **Fitzwilliam Museum** — descartado: imágenes CC-BY-NC-SA/CC-BY-NC-ND (no comercial),
  mismo motivo que Harvard. Anotado en `CLAUDE.md`.
- **Wikimedia Commons, Europeana, Harvard** — se confirmó que ya estaban evaluadas y
  descartadas de antes (el usuario preguntó por cada una para repasar el panorama
  completo, no eran fuentes nuevas).

Todo quedó solo investigado, nada integrado — son candidatos para una futura sesión si
el usuario decide avanzar con alguno.

## 2026-09-02 — Bug real y grave: la app perdía TODOS los datos del usuario en cada force-quit

Pedido del usuario: "cada vez que hago force quit, la obra del día cambia" — y un test que lo
simule. Investigación completa, de sospecha a causa raíz confirmada:

1. **Se descartó que fuera solo el cruce de medianoche** (la fecha real cambió a 2026-09-02
   durante esta misma sesión, lo que inicialmente parecía explicarlo). El usuario confirmó:
   "sí, cambia cada vez que cierro" — pasaba varias veces seguidas el mismo día.
2. **Repro en vivo en el emulador**, con logs y consultas SQL directas contra
   `/data/data/com.artdaily.app/databases/artworks.db` (no solo teoría): se probó primero con
   `adb shell am force-stop` (dio positivo), pero como una sesión anterior ya había anotado que
   ese comando específico tuvo "efectos secundarios raros" (borró un widget una vez), se repitió
   con el gesto real — Recientes + deslizar la tarjeta hacia arriba — para descartar que fuera
   un artefacto de ADB. **Mismo resultado**: confirmado que es un bug real de la app, no del
   método de cierre.
3. **Diagnóstico por eliminación**: se puso un favorito (sobrevive a nada), un marcador de texto
   directo en una fila de `artworks` (tampoco sobrevive), y un archivo `marker.txt` suelto en el
   mismo directorio (**ese sí sobrevive**, y el inode de `artworks.db` no cambia entre reinicios)
   — descarta que se esté borrando/recreando el archivo entero; algo lo limpia por dentro, en el
   mismo archivo, en cada apertura.
4. **Causa raíz**: `ArtworkSqliteWriter` (harvester) nunca seteaba `PRAGMA user_version` en el
   `artworks.db` que genera — quedaba en 0 (default de SQLite), mientras `AppDatabase.version = 4`
   en `:app`. Con `fallbackToDestructiveMigration(true)` (`DatabaseModule.kt`), Room detecta ese
   mismatch de versión y borra/reconstruye el esquema **en cada apertura del proceso, no solo la
   primera** — silencioso, sin crashear, así que nadie lo notó hasta ahora. Es un gotcha
   oficialmente documentado por Android para `createFromAsset`, no una rareza del proyecto:
   [developer.android.com/training/data-storage/room/prepopulate](https://developer.android.com/training/data-storage/room/prepopulate)
   ("it is necessary to update the user version pragma... failure to do so will result in the
   loss of any data inserted while the application is running").

**Fix**: `ArtworkSqliteWriter.setSchemaVersion()` nuevo — `PRAGMA user_version = 4` (constante
`SCHEMA_VERSION`, tiene que mantenerse igual a `AppDatabase.version` a mano, no hay forma de
compartirla entre `:harvester` y `:app`) al final de cada `write()`. Aplicado también a mano
(`sqlite3 ... "PRAGMA user_version = 4;"`) al `artworks.db` ya generado, antes de copiarlo a
`assets/`.

**Verificado en vivo, dos veces seguidas** (no solo "compila"): favorito agregado + historial
sobreviven íntegros a Recientes→deslizar→reabrir, dos ciclos consecutivos, contra la base real
del dispositivo. Suite completa corrida de nuevo tras el fix: 68 tests, 0 fallos.
`bundleRelease` regenerado con el asset corregido (más los 412 artistas nuevos de la cosecha en
paralelo, ver entrada de abajo) — el `.aab` viejo (de antes de este fix) **no debe usarse para
publicar**, tenía este bug adentro.

**De regalo, en la misma cosecha en paralelo**: 412 obras nuevas de 10 artistas no intentados
antes (Friedrich 162, Jan Steen 99, Redon 81, Ribera 19, Signac 15, Alma-Tadema 13, Ruisdael 12,
Zurbarán 3, Sorolla 1, Caillebotte 1) — catálogo 10939 → 11351. Se investigaron también 5 fuentes
nuevas de museos (Louvre, MoMA, Prado, Reina Sofía, Mauritshuis) a pedido del usuario: ninguna es
una integración simple tipo Met/AIC/CMA/Rijks — Louvre no es CC0 (reuso comercial requiere
contactar a su agencia de fotos), MoMA es CC0 pero sin imágenes en el dataset, y los otros 3 no
tienen API de datos abiertos equivalente. Queda anotado como pendiente de fondo, no para ahora.

**También se hizo, antes de este bug** (mismo día): reloj inyectable en
`GetArtworkOfTheDayUseCase` (`internal var clock`, mismo patrón que `SelectionEngine.random`)
con 2 tests nuevos que cruzan medianoche de verdad — confirma que esa lógica en particular
siempre estuvo bien escrita; el bug real estaba en la capa de persistencia, no ahí.

## 2026-09-01 — Retomando el camino a Play Store: política de privacidad publicada

Se retoma el pendiente del 2026-08-21 ("Pendiente para publicar"). Estado revisado a
fondo antes de tocar nada:

- **`targetSdk 37` ya cumple** el requisito nuevo de Google (apps nuevas deben targetear
  API 36+ desde el 31 de agosto de 2026) — no hace falta subir nada.
- **Dato desactualizado corregido**: la nota de "20+ testers en testing cerrado" ya no
  es así — Google bajó el mínimo a **12 testers** en diciembre 2024 (se mantiene el
  requisito de 14 días de opt-in continuo; el reloj arranca cuando se suma el tester
  #12, no antes). Fuente verificada:
  [support.google.com/googleplay/android-developer/answer/14151465](https://support.google.com/googleplay/android-developer/answer/14151465).
- **Data safety**: se revisaron las dependencias (`gradle/libs.versions.toml`,
  `app/build.gradle.kts`) — no hay Firebase/Analytics/Ads/Crashlytics ni SDK de terceros
  que mande datos fuera del dispositivo. El único tráfico de red es leer imágenes de los
  museos y bajar `delta.json` de GitHub Releases (ninguno identifica al usuario), más la
  traducción de ML Kit que corre 100% on-device. El formulario debería poder llenarse
  como "no se recopila información".

**Hecho hoy**: política de privacidad pública (bilingüe ES/EN), publicada en una rama
`gh-pages` limpia (sin mezclar con el resto del repo — usa un `git worktree` aparte para
no tocar el working tree principal) con GitHub Pages activado vía `gh api`. Verificado
en vivo (200 OK): **https://pacohurtadof.github.io/art-daily/**. Ese link es el que va
en Play Console (App content → Privacy Policy y en la ficha de la tienda).

**Sigue pendiente** (ver `docs/TODO.md` para el checklist completo): formulario de data
safety (llenarlo en la consola con las respuestas de arriba), cuestionario de
clasificación de contenido (tiene desnudos artísticos clásicos), ficha de la tienda
(ícono 512×512, feature graphic 1024×500, capturas, descripción corta/larga), y arrancar
cuanto antes el testing cerrado con 12 testers reales (14 días corridos — es lo que más
tarda en el calendario, conviene arrancarlo en paralelo a lo demás).

## 2026-09-01 (continuación) — Ficha de la tienda armada (ícono, feature graphic, capturas, textos)

Mismo pedido, siguiente paso. Todo en `docs/store-listing/`:

- **Ícono hi-res 512×512** (`hires_icon_512.png`): regenerado con el mismo recipe que el
  ícono de launcher (2026-08-25) — foreground del lienzo pintado sobre fondo `#E4E4E4` —
  pero componiendo desde `ic_launcher_foreground.png` de xxxhdpi (432px, no el legacy de
  192px) para que la escala hacia 512 no perdiera nitidez. Con Pillow, vía `python3`.
- **Feature graphic 1024×500**, ES y EN (`feature_graphic_es.png`/`_en.png`): el mismo
  ícono con sombra suave + "ArtDaily" en Georgia Bold (dos tonos: "Art" en `WarmDark`,
  "Daily" en `Orange40`, mismos hex que `ui/theme/Color.kt`) + tagline en Futura. El
  tagline tenía overflow al borde derecho en el primer intento — se corrigió con
  reducción dinámica de tamaño de fuente hasta que entra.
- **6 capturas reales** (no mockups) tomadas contra la app corriendo de verdad: se
  arrancó el emulador `ArtDaily_Test` (no estaba corriendo), `installDebug`, y se navegó
  por `adb shell input tap/swipe` + `screencap` — Hoy ("Water Lilies" de Monet), Explorar
  (grilla de filtros), Detalle (retrato de Tintoretto, se aprovechó para agregarlo a
  favoritos en la misma pasada), Favoritos (2 obras), Ajustes (rotación de fondo).
  **Widget agregado a la pantalla de inicio también por `adb`** (pedido aparte del
  usuario, retomado en la misma sesión): resultó no necesitar drag-and-drop simulado —
  el widget picker del launcher (Nexus Launcher) tiene un botón "Añadir" directo al
  expandir el preview de un widget de 1 sola talla, que lo coloca en la primera página
  con hueco libre sin arrastre. Capturado limpio (Water Lilies de nuevo, con
  título/artista/museo/fecha).
- **Textos ES/EN** (`listing-es.txt`/`listing-en.txt`): título, descripción corta y
  completa, con los límites de Play Console (30/80/4000 caracteres) verificados con un
  script (`len()` de Python), no a ojo.

Los 10 archivos se mandaron al usuario. Falta: pegarlos en Play Console (ficha de la
tienda) y decidir si conviene una captura del widget además de las 5 que ya hay.

## 2026-08-31 (continuación 3) — Tandas 36-39, con foco fuerte en impresionismo

Pedido del usuario: seguir con las tandas de movimiento, priorizando impresionismo.
Antes de la tanda round-robin de siempre, se hizo una pasada dirigida: se buscó por
nombre a los impresionistas core (Monet, Renoir, Degas, Pissarro, Morisot, Cassatt,
Manet, Boudin, Bracquemond) sin movimiento asignado en todo el catálogo (no solo el
tramo rankScore 3.0-3.99), revisando título/fecha uno por uno para decidir Impresionismo
vs Realismo en los casos tempranos (obras de antes de que el movimiento existiera como
tal — ej. el autorretrato de Degas de 1857, o los primeros óleos de Manet pre-Olympia,
quedaron en Realismo por consistencia con un precedente ya existente en el catálogo).
Cassatt quedó 100% clasificada (40 obras). **Impresionismo: 349 → 416 obras (+67)**.

Después se completaron las tandas 36-39 (round-robin de siempre, con la sorpresa de que
el tramo rankScore 3.0-3.99 se agotó del todo a mitad de camino — los últimos 147 rijks
eran exactamente el problema ya documentado de fotografías/retratos documentales sin
movimiento aplicable, ver ítem abierto de arriba). Se pasó al tramo rankScore=4.0
(mucho más rico: Whistler, Bonnard, Vuillard completos, Kandinsky, Mondrian, Redon,
Goya, Toulouse-Lautrec). Dos adiciones nuevas al vocabulario de movimientos usados:
**Arte abstracto** (ya existía en el diccionario pero nunca se había usado en una tanda
manual) para "Composition with Red, Yellow, and Blue" de Mondrian (1927) y "Painting
with Green Center" de Kandinsky (1913, su período totalmente abstracto, distinto del
Murnau expresionista/figurativo ya clasificado antes). Precedente nuevo confirmado para
Whistler: pre-1870 (grabados tempranos del Támesis, estilo realista-documental) =
Realismo; 1870 en adelante (nocturnos, atmosférico) = Tonalismo — coincide con lo que ya
había en el catálogo de tandas previas.

**Total clasificadas: 2715 → 3028 (+313).** Tests unitarios verdes. Copiado a
`assets/artworks.db`.

Se siguió (el usuario pidió más tandas): tanda 40 en rijks rankScore=4.0 rindió casi
nada (1 de 250 — Courbet, fundador del Realismo; el resto puro grabado reproductivo/
decorativo sin movimiento aplicable), confirmando que AIC/CMA/MET ya estaban agotados
en ese tramo y solo quedaba rijks de baja señal. Se saltó a los tramos altos
(rankScore 5.0-7.0, solo 14 obras sin revisar pero de alto valor — Bonnard, Burne-Jones,
Mondrian x2, Matisse) y después a un tramo bajo pero nunca tocado (rankScore=2.0,
184 obras de CMA) que sí rindió bien: la serie completa "Liber Studiorum" de Turner (44
grabados → Romanticismo, coincide con las 80 ya clasificadas) y Jozef Israëls
(Escuela de la Haya → Realismo). También primeras clasificaciones con **Arte
abstracto** y **Cubismo** (Matisse "Apples" 1916, su pintura más cubista) en el
vocabulario de movimientos usados manualmente.

**Total clasificadas: 3028 → 3096 (+68 más).** Total de la sesión: 2715 → 3096 (+381).
Tests verdes, copiado a assets.

Se siguió una vez más (tandas 43-45): un batch de MET/rijks rank=2.0 (45 obras) rindió
0 — todas sin `artistName` (arte religioso asiático, retratos anónimos, reproducciones
fotográficas). Se pasó al pool grande de rijks rank=4.0 (976 obras, la reserva más
grande que queda). Ahí sí hubo señal real mezclada entre el Siglo de Oro holandés
(mayoría, sin movimiento aplicable — es periodo Barroco, no movimiento, correcto que
quede null): **Antonio Tempesta** (22 grabados de una serie narrativa de 1612 →
Manierismo), **Richard Nicolaüs Roland Holst** (7 obras → Simbolismo, simbolista
holandés), **Marius Bauer** (3 obras de temática orientalista → Orientalismo, primer uso
de esa categoría del diccionario) y un grupo de pintores de la **Escuela de la Haya**
(Haagse School — Jozef Israëls, Willem Roelofs, August Allebé, Suze Robertson, Jacob
Maris, Johannes Bosboom, Bernardus Blommers, Weissenbruch, Gerard Bilders, Théophile de
Bock, Paul Gabriël) clasificados como **Realismo** por consistencia (es la rama
holandesa paralela a Barbizon, sin entrada propia en el diccionario). También
**George Hendrik Breitner** (impresionismo de Ámsterdam → Impresionismo, 4 obras) y
Corot/Courbet/Diaz de la Peña (Barbizon/Realismo, ya con precedente).

**Total clasificadas: 3096 → 3174 (+78 más). Impresionismo: 416 → 420.** Total de la
sesión: 2715 → 3174 (+459). Tests verdes, copiado a assets.

Se siguió una última vez (tandas 46-47, terminando el pool de rijks rank=4.0 hasta
agotarlo): más **Escuela de la Haya** (Isaac Israels, Jozef Israëls, Jacob y Willem
Maris, Anton Mauve → Realismo; Isaac Israels y Willem Witsen → Impresionismo por ser
impresionismo de Ámsterdam), **Escuela de Barbizon** (Daubigny, Jules Dupré, Diaz de la
Peña), Fantin-Latour (Realismo, círculo de "Hommage à Delacroix") y primer uso amplio de
**Ukiyo-e** en tandas manuales (Utagawa Kunisada/Kuniyoshi/Toyokuni/Toyohara Kunichika,
16 obras — Hokusai/Hiroshige ya venían con esta categoría de tandas anteriores).

**Verificación final: se agotó el pool completo de tandas de movimiento.** Al terminar
la tanda 47 (rijks rank=4.0), una consulta sin filtro de "ya revisado" mostró que
**todo** el rango rankScore 2.0-7.0 en las 4 fuentes ya había sido revisado en algún
momento de este proyecto (multi-sesión) — y por debajo de rank=2.0 tampoco queda nada
sin tocar. No es una pausa: no hay más candidatos por este mecanismo. Lo que queda sin
`movement` (7719 de 10939 obras elegibles) es así por decisión ya tomada, no por falta
de revisión — sobre todo Siglo de Oro holandés/Barroco/Renacimiento (periodo, no
movimiento, por diseño del diccionario), retratos anónimos de otros artistas, y
reproducciones fotográficas.

**Total clasificadas: 3174 → 3220 (+46 más). Impresionismo: 420 → 425.** Total de la
sesión completa: 2715 → 3220 (+505), con foco fuerte en impresionismo (349 → 425, +76).
Distribución final por movimiento: Ukiyo-e 549, Romanticismo 517, Realismo 494,
Impresionismo 425, Postimpresionismo 355, Simbolismo 203, Tonalismo 135, Manierismo 124,
Nabis 112, Neoclasicismo 88, Escuela de Barbizon 61, Escuela del río Hudson 35,
Orientalismo 34, Expresionismo 32, Prerrafaelismo 21, Modernismo 9, Luminismo 9, Art
Nouveau 7, Arte abstracto 3, Futurismo 2, Fauvismo 2, Cubismo 2, Dadaísmo 1. Tests
verdes, copiado a assets.

Se publicó el release `data-20260831` en GitHub con el catálogo completo (se borró el
release del mismo día publicado más temprano — antes de las tandas 36-47 — y se
recreó con el estado final). Verificado en vivo en el celular: `pm clear` +
instalación limpia + extracción directa de la base del dispositivo confirmó **10939
obras elegibles, 3220 clasificadas, 425 impresionismo, 39 icónicas** — todo exacto y
estable.

## 2026-08-31 (continuación 2) — Tercera ronda de artistas populares + verificación en vivo

Primero se verificó en vivo lo pendiente de la ronda anterior (Cassatt/Rubens/Kandinsky):
`pm clear` + instalación limpia, extracción directa de la base del dispositivo — **10473
obras confirmadas, 39 icónicas, estable**. Sin sorpresas esta vez porque no hubo sync de
por medio (instalación fresca desde `assets/`).

Después, respondiendo a "qué artistas nos faltan", se armó un lote nuevo de candidatos
pre-1955 (sin riesgo de copyright) que no se habían cosechado a propósito todavía —
prerrafaelitas, Nabis, y un par de sueltos que aparecían con muy poca presencia en
búsquedas exploratorias:

| Artista | Antes | Después |
|---|---|---|
| Henri Rousseau (Le Douanier) | 9 | **50** |
| Édouard Vuillard | 19 | **64** |
| Gustave Moreau | 1 | **24** |
| Pierre Bonnard | 6 | **26** |
| John Everett Millais | 0 | **10** |
| William Holman Hunt | 0 | **8** |
| Piet Mondrian | 1 | **6** |
| Frans Hals | 5 | **8** |
| Rosa Bonheur | 2 | **4** |
| Dante Gabriel Rossetti | 0 | **1** |
| Edward Burne-Jones | 0 | **1** |
| Frederic Leighton | 1 | 1 (sin cambio) |
| Grant Wood | 0 | 0 (sin cambio) |

**Catálogo total: 10473 → 10939 obras** (+466 — más que la suma de los deltas por
artista arriba, ~159; la búsqueda de cada fuente no es un match exacto por campo
"artista", así que trae también obras relacionadas/coincidentes que después el ranking
y los filtros ya existentes procesan igual que siempre). Grant Wood en 0 confirma que
"American Gothic" del Art Institute of Chicago no está en su set de open access (pieza
demasiado icónica/vigilada incluso siendo de 1930); Leighton se quedó igual, sin obra
adicional disponible en estas 4 fuentes.

Se generó el delta completo del catálogo (10939 filas, mismo shape que el modelo
`Artwork` de Kotlin) y se publicó como release `data-20260831` en GitHub — mismo patrón
ya establecido para evitar que el sync automático pise trabajo con un release viejo. El
celular se reconectó poco después: `pm clear` + instalación limpia + extracción directa
de la base del dispositivo confirmó **10939 obras, conteos por artista exactos, 39
icónicas intactas**. Estable.

## 2026-08-31 (continuación) — Segunda ronda de artistas populares

Pedido del usuario: seguir con la lista de candidatos que había quedado pendiente del
2026-08-28 (Cassatt, Seurat, Manet, Rubens, Vermeer, Chagall, Schiele, Kandinsky).
Mismo mecanismo de siempre, uno por uno contra las 4 fuentes reales:

| Artista | Antes | Después |
|---|---|---|
| Cassatt | 3 | **48** |
| Rubens | 7 | **17** |
| Kandinsky | 1 | **5** |
| Seurat | 3 | 3 (sin cambio) |
| Vermeer | 4 | 4 (sin cambio) |
| Chagall | 0 | 0 (sin cambio) |
| Schiele | 0 | 0 (sin cambio) |

**Catálogo total: 10373 → 10473 obras.** Cassatt fue el hallazgo grande — pasó de
prácticamente nada a 48 obras reales (AIC tiene una colección fuerte de ella, tiene
sentido siendo el museo de Chicago). Seurat/Vermeer/Chagall/Schiele no aportaron nada:
los primeros dos ya estaban en el techo real de lo que estas 4 fuentes tienen (sus
obras más conocidas están en el Musée d'Orsay/Mauritshuis, fuera de nuestras fuentes);
los últimos dos siguen con copyright vigente en la práctica pese a que Schiele
técnicamente debería ser dominio público desde 1988 — simplemente no está en estas
colecciones. No se intentó con Frida Kahlo/Warhol/Dalí por la misma razón de derechos
de autor que ya se confirmó con Picasso/Matisse/Chagall (altísima probabilidad de 0).

Tests unitarios verdes. **Pendiente instalar/verificar en vivo** — el celular de prueba
no estuvo conectado durante esta cosecha.

## 2026-08-31 — Cierre real del "bloqueo de Cloudflare en AIC": nunca fue un problema

Seguimiento del hallazgo del 2026-08-28 (imagen faltante en el widget). El usuario
preguntó cuánto duraría, y se dejó un monitor en segundo plano pegándole a
`www.artic.edu/iiif/...` cada 2 minutos — casi 2 días seguidos, siempre 403 ("Just a
moment..."). Ante la pregunta directa del usuario ("¿qué probabilidad hay de que nos
haya bloqueado permanentemente?"), se probó desde una red totalmente distinta a la del
usuario (WebFetch, infraestructura de Anthropic) — **también 403**, tanto en el CDN de
imágenes como en la ficha de obra normal (`www.artic.edu/artworks/...`). Eso descartaba
la teoría original ("nos flaggearon la IP de la red del usuario por volumen") — parecía
más bien un challenge anti-bot general del sitio contra cualquier cliente sin motor
JavaScript.

**La prueba que realmente importaba**: abrir la app de verdad en el celular real
(`am start` con el extra `artworkId=aic:28560`, el mismo mecanismo que usa el widget al
tocarlo) y ver si Coil/OkHttp cargaba la imagen. **Cargó perfecto, al primer intento** —
"The Bedroom" de Van Gogh, a color, completa. Ni `curl` ni el fetcher de Anthropic
pudieron nunca (llevaban ~48h fallando), pero la app sí, todo el tiempo.

**Conclusión real**: el challenge de Cloudflare de `www.artic.edu` es contra
herramientas automatizadas sin motor JS — no contra tráfico real de la app, no una
sanción por IP, no un bloqueo que "se libera con el tiempo" porque nunca bloqueó lo que
importaba. Fue un artefacto de cómo se estaba diagnosticando (`curl`), no un problema
real del producto. **Lección para el futuro**: verificar contra la app/dispositivo real
primero cuando se sospeche un bloqueo de red — `curl` puede dar un falso positivo
rotundo si el sitio protegido usa un challenge JS. Cerrado en `docs/TODO.md`.

## 2026-08-28 (continuación 5) — Tandas 33-35, pausado por rendimiento y fotografías

Retomadas las tandas de movimiento (2651 → 2701). Rendimiento cayendo tanda a tanda:
33→2679 (28/250, ~11%, todavía retratos de reproducción del Rijksmuseum pero con algunos
hallazgos reales — primer uso de Neoclasicismo en volumen vía Jean Baptiste Mauzaisse y
Raffaello Morghen, Manierismo vía Giorgio Ghisi/Etienne Dupérac/Roelant Savery), 34→2698
(19/250, ~7.6%, Willem Witsen y los Amsterdam Impressionists rindieron bien —
8 obras—, más Sickert/Bracquemond/Menpes), 35→2701 (**3/250, ~1.2%**).

**Hallazgo real en la tanda 35**: el pool restante del Rijksmuseum en este tramo ya no
es mayormente pintura/grabado — es **fotografía documental/de viaje del siglo XIX**
(ruinas de Sri Lanka, Gran Cañón, India, Nueva York, perros premiados en exposiciones
caninas de 1891). `ClassificationNormalizer` las está mapeando a `"print"` (el campo
real de la fuente dice algo como "photographic print", que matchea el substring
"print") — por eso entran al catálogo de "obra del día" como si fueran pinturas/
grabados, cuando en realidad son fotografías documentales, fuera del espíritu de la
app ("una obra distinta cada día"). Conteo aproximado (año ≥1839, año de invención de
la fotografía, heurística imprecisa pero orientativa): 385 de las 1443 filas
`rijks`/`print` sin movimiento en este tramo. **No se tocó el código** — es una
decisión de producto (¿cuenta una foto de viaje de 1891 como "obra" para esta app?),
no algo para decidir unilateralmente. Ver `docs/TODO.md`.

Total: **2701 obras clasificadas** (`movement-overrides.csv`, 2585 líneas). Pausado a
pedido del usuario tras rendimiento muy bajo + este hallazgo. El celular de prueba se
desconectó varias veces durante esta sesión (parece intermitencia del cable/puerto USB,
no relacionado al código) — nada de esto se instaló/verificó en vivo todavía, sigue
pendiente igual que las tandas 25-32 (aunque esas sí se verificaron después, ver la
entrada de esa continuación).

## 2026-08-28 (continuación 4) — "Bug" reportado: sin imagen en el widget (no era bug)

Usuario reportó que el widget no mostraba ninguna imagen. Diagnóstico en vivo en el
celular de prueba (agregando logging temporal a `WidgetImageDownloader`, revertido
después de encontrar la causa — no quedó en el código):

- La API JSON de AIC (`api.artic.edu`) responde perfecto (200 OK).
- El **CDN de imágenes** de AIC (`www.artic.edu/iiif/2/...`) devuelve un challenge
  "Just a moment..." de Cloudflare (403) — verificado con `curl` directo, tanto desde
  la Mac como (por estar en la misma red WiFi, "A Casa Poo") desde el celular.
- Met (`images.metmuseum.org`), Cleveland (`openaccess-cdn.clevelandart.org`) y
  Rijksmuseum (`iiif.micr.io`) cargan imágenes sin problema — el bloqueo es específico
  de AIC, no de la red en general ni de la app.
- El widget de prueba justo tenía asignada una obra de AIC (Gauguin, "Manao Tupapau")
  — por eso se notó ahí, pero el mismo bloqueo afecta cualquier imagen de AIC en toda
  la app mientras dure.
- El fallback de `WidgetImageDownloader`/`ArtWidget` funcionó exactamente como está
  diseñado: si la descarga falla, el widget cae a solo texto en vez de romperse. No es
  un bug de la app — es Cloudflare bloqueando temporalmente la IP de esta red,
  casi seguro por el volumen altísimo de peticiones automatizadas contra AIC durante
  toda la sesión de hoy (decenas de corridas del harvester + miles de llamadas).

Ya estaba anotado como riesgo conocido desde que se integró AIC (comentario en
`AicMapper.kt`: "Cloudflare devolvió un challenge anti-bot [...] pendiente confirmar
que carga bien desde una red residencial / la app real antes de darlo por sentado").
Hoy se confirmó que sí carga bien en general (la app llevaba semanas mostrando obras de
AIC sin este problema) — esto es puntual, por el volumen de tráfico de HOY, no un
problema estructural. Debería resolverse solo en un rato (el bloqueo de Incapsula/WAF
del Met del 2026-08-25 se resolvió solo a los pocos minutos; este podría tardar más
por el volumen mucho mayor de hoy). Ningún cambio de código — nada que commitear de
esta entrada. Si vuelve a pasar seguido, ver `docs/TODO.md`.

## 2026-08-28 (continuación 3) — Tandas 25-32, se pasan las 2600, pausado sin verificar

Retomado el trabajo de clasificación de movimiento (mismo tramo `rankScore` 3.0-3.99,
mismo mecanismo de tandas). El celular de prueba no estuvo conectado en NINGUNA de las 8
tandas de esta sesión — todo lo de abajo está aplicado a `harvester/output/artworks.db` y
copiado a `app/src/main/assets/artworks.db`, con tests unitarios pasando, pero **sin
instalar/verificar en vivo todavía**.

Progreso por tanda: 25→2091 (148/250, ~59%, AIC rindiendo fuerte con Rembrandt/Dürer/
Hiroshige/Munch), 26→2263 (172/250, ~69%, mismo pool AIC — Goya, Turner Liber Studiorum,
grabadores manieristas del norte de Europa: Goltzius, Saenredam, Adamo Scultori),
27→2441 (178/250, ~71%, pico del pool AIC — Toulouse-Lautrec/Renoir/Degas/Gauguin/Munch
casi agotados ahí), 28→2581 (140/250, ~56%, empieza a mezclar MET — Van Gogh/Degas/
Cézanne del Met, más el resto de Munch/Gauguin de AIC), 29→2634 (53/250, ~21%, primer
bloque grande de Renacimiento/Barroco puro del Met sin movimiento aplicable — El Greco sí
dio Manierismo, Tintoretto también), 30→2640 (6/250, ~2.4%, Rowlandson + Hollar + el
cluster gigante de autorretratos de Rembrandt del Met/Rijks), 31→2640 (**0/250**, tanda
casi 100% Rembrandt/reproducciones del Rijksmuseum — el fondo real del pozo de ese
cluster), 32→2651 (11/250, ~4.4%, retratos de artistas famosos por grabadores menores de
reproducción — encontró a Louis Valtat, primer uso real de Fauvismo en el catálogo).

**Total: 2651 obras clasificadas** (`movement-overrides.csv`, 2535 líneas con cabecera).
Pausado a pedido del usuario tras dos tandas seguidas de rendimiento muy bajo (0% y
4.4%) — recomendación explícita de pausar en vez de seguir forzando, dada además la
falta de verificación en vivo acumulada. Quedan 3684 obras sin revisar en el tramo
`rankScore` 3.0-3.99: cma 1485, rijks 1493, met 418, aic 288 (AIC casi agotado en este
tramo). **Pendiente crítico para retomar: conectar el celular e instalar/verificar todo
lo acumulado desde la tanda 25** antes de seguir con más tandas — ver `docs/TODO.md`.

## 2026-08-28 (continuación 2) — Priorizar obras conocidas + primer release real de datos

Pedido del usuario: priorizar obras más conocidas en la selección diaria, "para que no se
aburra de ver muchas obras que no conoce". Diagnóstico primero: ninguna fuente expone una
señal de fama real (`rankScore` mide completitud de metadatos, no reconocimiento;
`museumFlaggedHighlight` solo lo pone el Met). Se le presentaron dos caminos — heurística
automática vía Wikidata/Wikipedia (escala sola, pero el cruce por título/artista es
impreciso) o curaduría manual obra por obra (mismo patrón que `movement`) — **eligió
curaduría manual a propósito, por precisión**.

**Implementado:**
- `Artwork.isIconic: Boolean` nuevo (core-model) + columna en Room (`AppDatabase` v3→v4,
  `fallbackToDestructiveMigration`, sin migración escrita) + columna en
  `ArtworkSqliteWriter` (con `ALTER TABLE ... ADD COLUMN` idempotente para no romper
  archivos `artworks.db` ya generados por corridas previas).
- `IconicOverrides.kt` (harvester) — mismo patrón que `MovementOverrides`, pero más simple:
  `harvester/data/iconic-overrides.txt`, un `artworkId` por línea con comentario inline
  (`aic:28560   # The Bedroom`) documentando qué es cada obra.
- **Primer lote curado a mano, 39 obras**: The Scream y Madonna de Munch, The Bedroom e
  Irises de Van Gogh, las tres "Meisterstiche" de Durero (Knight Death and the Devil,
  Melencolia I, St. Jerome in His Study) + Four Horsemen/Adam and Eve/Rhinoceros, The
  Great Wave de Hokusai, A Sunday on La Grande Jatte de Seurat (la pieza insignia del AIC),
  The Jewish Bride de Rembrandt, Olympia de Manet (grabado), At the Moulin Rouge de
  Toulouse-Lautrec, y más — lista completa en `harvester/data/iconic-overrides.txt`.
- `SelectionEngine.pickForWidget`: sesgo del 60% (`ICONIC_BIAS`) a elegir del sub-pool
  icónico cuando hay alguno disponible ese día, el resto de las veces pool completo — no
  100% a propósito, para no agotar rápido un pool de cientos de obras ni perder la gracia
  de "una obra distinta cada día" explorando el catálogo real. `random: Random` quedó como
  `var` interno (no en el constructor) para poder inyectar un `Random` fake en tests sin
  que Hilt intente resolver un binding — Dagger/Hilt no respeta valores default de
  parámetros de constructor Kotlin. 3 tests nuevos con un `FixedRandom` que controla tanto
  `nextFloat()` (decide si gana el sesgo) como `nextInt()` (decide qué índice), para poder
  distinguir "restringió al sub-pool icónico" de "le tocó por azar en el pool completo".

**Encontrado en vivo mientras se verificaba**: el sync automático (`DailyArtworkWorker` →
`ArtworkSyncService`, pull del `delta.json` del último release de GitHub) le seguía
pisando datos a la app de prueba con el release viejo (de antes de todos los fixes de
esta semana) — esta vez se comió justo "El grito" y "La Gran Jatte" del flag nuevo.
Ya era el segundo feature verificado a medias por este motivo (ver la entrada de ayer). El
usuario decidió esta vez sí publicar el release real:

**Primer release real de datos con todo lo de esta semana** (`data-20260828`,
`github.com/pacohurtadof/art-daily/releases`): en vez de usar el `delta.json` incremental
de la última corrida individual del harvester (que el script `publish-release.sh` toma
por default — solo hubiera traído los cambios de la ÚLTIMA búsqueda, no de toda la
semana), se generó un delta.json con el **catálogo completo actual** (10373 obras) para
que cualquier dispositivo quede 100% al día en un solo sync, sin importar qué release
tenía antes. Verificado end-to-end: instalación limpia (`pm clear` + relanzar) mostró
39/39 obras icónicas correctas de entrada, y se mantuvo así después de esperar (antes,
con el release viejo, bajaba a 30/39 por el sync). Este pendiente (publicar release) queda
cerrado — ver `docs/TODO.md`.

## 2026-08-28 — Continuación: 14 artistas más, "El grito" de Munch

Pedido del usuario en la sesión anterior ("en la siguiente sesión vamos a tratar de
conseguir más obras de artistas populares"), retomado hoy con el mismo mecanismo del
2026-08-27 (`./gradlew :harvester:run --args="<artista> output/artworks.db"` uno por uno
contra las 4 fuentes reales, ya con el filtro de bocetos/estudios y los 2 fixes de AIC
activos de la sesión anterior).

| Artista | Antes | Después |
|---|---|---|
| Munch | 7 | **103** |
| Turner | 60 | **141** |
| Dürer | 119 | **217** |
| Hiroshige | 73 | **169** |
| Hokusai | 45 | **138** |
| Toulouse-Lautrec | 109 | **189** |
| Gauguin | 40 | **93** |
| Renoir | 44 | **81** |
| Degas | 34 | **80** |
| Bruegel | 7 | **9** |
| Velázquez | 4 | **9** |
| Sargent | 3 | **11** |
| Bosch | 3 | **4** |
| Klimt | 0 | 0 (nada disponible en estas 4 fuentes) |

**Catálogo total: 9612 → 10373 obras.** Hallazgo destacado: Munch pasó de 7 a 103 obras
gracias sobre todo a CMA (fuerte en grabados de Munch), y entre ellas apareció **"El
grito"** (aic:17229) — verificado en vivo directo contra la base del dispositivo
(`run-as com.artdaily.app cat .../artworks.db`), una de las pinturas más reconocibles
del mundo, sumada al "El dormitorio" de Van Gogh conseguido ayer.

Instalado y verificado en el Pixel 10 (`pm clear` + reinstalar + comparación directa
contra la base real del dispositivo, no solo compilar). Total en dispositivo: 10341
(vs 10373 local — mismo desfase ya conocido por el sync con el release viejo de GitHub,
sigue pendiente publicar uno nuevo, ver `docs/TODO.md`).

## 2026-08-27 (continuación 6) — Filtro de bocetos/estudios preparatorios

Pedido del usuario: sacar los sketches/bocetos del catálogo, dejar solo obra terminada.
Ninguna de las 4 fuentes tiene un campo que distinga "boceto preparatorio" de "obra
terminada" — se detecta por el título. Se armó el patrón (`\bsketch(es)?\b|\bstud(y|ies)\b`,
con la "y"/"ies" exacta) probándolo en vivo contra los 79 títulos reales del catálogo que
contenían "sketch"/"study"/"studie": atrapaba 68 de entrada, se afinó a 75/79 agregando
`studieblad`/`anatomische studie` (neerlandés) — y se agregó una excepción explícita
(`STUDY_AS_ROOM_PATTERN`) para los 4 casos donde "study" es el cuarto/habitación, no el
boceto ("Saint Jerome in his Study by Candlelight", "Old Man in his Study", "A Scholar in
His Study", "Out of Study Window"), que NO debían borrarse.

Implementado en `Artwork.isEligibleForCatalog()` (`harvester/src/main/kotlin/.../Main.kt`,
mismo lugar que el filtro de clasificación/año) — afecta cosechas futuras. Limpieza
retroactiva del catálogo actual: 75 filas borradas de `harvester/output/artworks.db`
(9687 → 9612 painting/print), copiado a `assets/artworks.db`, instalado y verificado en
vivo (control: "Environs of Rome" de Corot, que no debía tocarse, sigue intacto).

Ejemplos reales que salieron: "Study for 'Bathers at Asnières'" (Seurat), "Sheet of
Studies" (Rembrandt, varias), "Study for 'The Bear Hunt'" (Rubens), "Sketch for 'The
Oriental Dream'" (Lecomte du Nouÿ), toda la serie "Campaign Sketches" de Winslow Homer.

## 2026-08-27 (continuación 5) — Obras icónicas de 14 maestros + 2 bugs reales de AIC

Pedido del usuario: agregar más obras icónicas/emblemáticas de grandes maestros (Van
Gogh, Rembrandt, Picasso, Goya, Da Vinci, etc.) para que sean "lo atractivo de la app".

**Dos bugs reales encontrados investigando por qué Van Gogh/Picasso/Cézanne/Monet
rendían tan poco** (14/13/5/10 obras respectivamente antes de esto):

1. **AIC estaba completamente roto desde el 2026-08-25.** Al subir `BATCH_SIZE` de 100 a
   150 (para la cosecha grande de ~10.000 obras), se rompió el límite real de AIC — su
   API cachea 403 "Invalid limit" si se pide más de 100 por página. El `catch` genérico
   de `harvestAic` lo tragaba como un error de red cualquiera, en silencio. Confirmado:
   las 138 filas de `aic` en la base quedaron todas con `harvestedAt` del 2026-08-19 —
   una semana entera de cosechas (incluida la expansión a ~9000-10000 obras del
   2026-08-25/26) sin una sola fila nueva de AIC. Fix: `AIC_BATCH_SIZE = 100` separado
   de `BATCH_SIZE`, usado solo en `harvestAic` (`harvester/src/main/kotlin/.../Main.kt`).

2. **`ClassificationNormalizer` buscaba la palabra "painting"/"print" como substring
   literal**, pero AIC manda el MEDIO real en `classification_title` (`"oil on canvas"`,
   `"etching"`, `"engraving"`, etc.), nunca la palabra "painting"/"print" en sí. Bug
   mucho más viejo que el #1 (existía desde el diseño original del normalizador) —
   confirmado en vivo con "El dormitorio" de Van Gogh (AIC, `classification_title="oil
   on canvas"`, `is_public_domain=true`, con imagen): se mapeaba, pero cala en `classification="other"`
   y quedaba excluido del catálogo entero (`isEligibleForCatalog` solo acepta
   painting/print). Un sondeo rápido contra la API real (5 queries, 100 resultados cada
   una) mostró 145 "etching" + 39 "oil on canvas" + 29 "engraving" + 13 "oil on panel" +
   otros — todos cayendo en "other" antes del fix. Fix: se agregaron ~17 términos de
   medio reales (`oil on canvas/panel/board`, `tempera on panel/canvas`, `acrylic on
   canvas`, `etching`, `engraving`, `drypoint`, `lithograph`, `mezzotint`, `aquatint`,
   `screenprint`, `linocut`, `monotype`, `chromolithograph`, `woodcut`) a
   `ClassificationNormalizer` (core-model), con 2 tests de regresión nuevos.

**Con los dos fixes, se cosecharon 14 maestros uno por uno** (modo normal del
harvester, `./gradlew :harvester:run --args="<nombre> output/artworks.db"`, contra las 4
fuentes reales cada vez):

| Artista | Antes | Después |
|---|---|---|
| Van Gogh | 14 | 27 (incluye "El dormitorio", AIC) |
| Rembrandt | 560 | 654 |
| Goya | 144 | 244 |
| Monet | 10 | 38 |
| Cézanne | 5 | 21 |
| Tiziano | 0 | 30 |
| Rafael | 0 | 20 |
| Vermeer | 5 | 5 (sin cambio) |
| Matisse | 2 | 4 |
| Caravaggio | 0 | 5 |
| Botticelli | 0 | 5 |
| Miguel Ángel | 0 | 1 |
| Da Vinci | 1 | 2 |
| Picasso | 13 | 13 (sin cambio) |

**Catálogo total: 9084 → 9687 obras** painting/print. AIC pasó de 138 a 572 filas (el
salto más grande, gracias sobre todo al fix #2).

**Limitación real que hay que aceptar, no un bug**: Picasso/Da Vinci/Vermeer/Matisse/
Miguel Ángel casi no tienen presencia real en estas 4 fuentes. Sus obras más icónicas
(Mona Lisa, Guernica, La joven de la perla, La noche estrellada) están en museos que no
son fuente de esta app (Louvre, Reina Sofía, Mauritshuis, MoMA), y Picasso/Matisse
además siguen con derechos de autor vigentes en la mayoría de colecciones "open
access" (no es solo cuestión de qué museo). Pendiente: evaluar si vale la pena agregar
una fuente nueva para esto — ver `docs/TODO.md`.

Tests unitarios verdes (55+, incluidos los 2 nuevos de `ClassificationNormalizerTest`).
**Pendiente de instalar/verificar en vivo** — el celular de prueba se desconectó a mitad
de la sesión (`adb devices` no lo ve). `app/src/main/assets/artworks.db` ya está
regenerada y copiada, solo falta `pm clear` + reinstalar + confirmar. Ver `docs/TODO.md`
para este pendiente y los demás que salieron de esta sesión (evaluar fuentes nuevas,
clasificar movimiento de las ~603 obras nuevas).

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
