# Testing cerrado — 12 testers, 14 días corridos

Requisito de Google para cuentas de Play Console personales creadas después del
13/11/2023 (la tuya) antes de habilitar producción: **12 testers reales, con opt-in
continuo durante 14 días corridos**, en una pista de testing cerrado. Verificado el
2026-09-01 (bajó de 20 a 12 en diciembre 2024) —
[fuente](https://support.google.com/googleplay/android-developer/answer/14151465).

Puntos que importan para no perder tiempo:

- **El reloj arranca cuando se suma el tester #12**, no antes — conviene juntar a los
  12 lo más rápido posible, no ir sumando de a uno espaciado en el tiempo.
- **Tienen que ser personas reales, en dispositivos reales, con cuentas de Google
  genuinas** — no emuladores, no cuentas duplicadas, no bots.
- **El opt-in tiene que ser continuo.** Si alguien opta y después se desinstala/opta
  out antes del día 14, esos días no cuentan para esa persona — tiene que volver a
  empezar sus 14 días. Por eso conviene pedirles explícitamente que la dejen instalada
  sin tocarla, no que la prueben y la borren.
- No hace falta que la usen activamente — con que quede instalada y con la cuenta
  optada in alcanza.

## Pasos en Play Console (los hacés vos, requiere tu login)

1. **Play Console → tu app → Testing → Closed testing** (o "Pruebas cerradas") →
   crear una pista nueva (ej. "Cerrado — inicial").
2. Subir el `.aab` firmado a esa pista (`./gradlew :app:bundleRelease`, ver
   `docs/TODO.md`).
3. Pestaña **Testers**: elegir "Lista de emails" (la opción más simple para 12
   personas conocidas) y pegar los 12 emails de la tabla de abajo.
4. Play Console genera un **link de opt-in** (algo como
   `play.google.com/apps/testing/com.artdaily.app`) — ese es el link que les mandás.
5. Cada tester: abre el link desde su cuenta de Google → "Become a tester" / "Pasar a
   ser probador" → instala desde el link de Play Store que aparece ahí mismo (todavía
   no está en la store pública, solo visible para quien optó in).

## Mensaje para mandarles (borrador, ajustalo a como les hables)

> Hola! Estoy por publicar en Play Store una app que hice (ArtDaily — muestra una
> obra de arte distinta cada día, con widget). Antes de poder publicarla necesito que
> 12 personas la instalen como "tester" durante 2 semanas — no hace falta que la usen,
> solo que la dejen instalada sin desinstalarla en ese tiempo. ¿Me ayudás?
>
> Son 2 pasos, 2 minutos:
> 1. Abrí este link desde tu celular y tocá "Become a tester": [LINK DE OPT-IN]
> 2. Instalá la app desde el link de Play Store que te va a aparecer ahí mismo.
>
> Eso es todo — gracias!

## 2026-09-04: estrategia elegida — contactos propios + comunidades de intercambio

El usuario no tiene 12 contactos personales a mano para pedirles esto. Google no
exige que sean conocidos, solo "personas reales, dispositivos reales, cuentas de
Google genuinas" que opten in y dejen la app instalada — por eso existe un
ecosistema activo de devs que se ayudan mutuamente a cumplir exactamente este
trámite (no es hacer trampa: son cuentas y dispositivos genuinos, cumplen la letra
del requisito). Decisión: mezcla de los dos caminos.

**Dónde postear** (en inglés, son comunidades angloparlantes):
- Reddit: **r/AndroidBetas** y **r/AlphaAndBetaUsers**
- Grupos de Telegram/Discord de "Play Store closed testing exchange" (buscar esos
  términos — rotan con frecuencia, no hay uno fijo para linkear acá)
- Subforo de beta testing de XDA Developers

### Borrador para esas comunidades (inglés)

> **[Testing] ArtDaily — a new artwork every day, with a home screen widget (Android)**
>
> Hey! Working on getting ArtDaily out of closed testing on Google Play and need to
> hit Google's 12-tester / 14-day requirement. The app shows a different artwork
> each day (Met, Art Institute of Chicago, Cleveland Museum, Rijksmuseum — all
> public domain), filterable by period/movement/artist, with a home screen widget.
> Fully offline-first, no ads, no data collection.
>
> Opt-in link: [LINK DE OPT-IN]
>
> Just need you to opt in and keep it installed for 14 days — no need to actually
> use it, though feedback is welcome. Happy to reciprocate on your listing too, just
> drop your link below or DM me.

Después de postear, conviene anotar acá abajo quién se apunta desde estas fuentes
(no solo los contactos personales) para no perder la cuenta de cuántos faltan.

## Planilla de seguimiento

Completar a medida que cada uno confirma. "Día 14" = fecha de opt-in + 14 días; ahí
ya cuenta para el requisito (siempre que no haya optado out en el medio).

| # | Nombre | Email (cuenta de Google) | Invitado (fecha) | Opt-in confirmado (fecha) | Día 14 |
|---|--------|---------------------------|-------------------|----------------------------|--------|
| 1 |        |                           |                   |                            |        |
| 2 |        |                           |                   |                            |        |
| 3 |        |                           |                   |                            |        |
| 4 |        |                           |                   |                            |        |
| 5 |        |                           |                   |                            |        |
| 6 |        |                           |                   |                            |        |
| 7 |        |                           |                   |                            |        |
| 8 |        |                           |                   |                            |        |
| 9 |        |                           |                   |                            |        |
| 10 |       |                           |                   |                            |        |
| 11 |       |                           |                   |                            |        |
| 12 |       |                           |                   |                            |        |

Link de opt-in (pegar acá una vez creada la pista): _pendiente_
