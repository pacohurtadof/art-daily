package com.artdaily.app.widget

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.artdaily.app.MainActivity
import com.artdaily.app.R
import java.io.File
import kotlinx.serialization.json.Json

/**
 * `docs/etapa2-diseno-arquitectura.md`, sección 5: Jetpack Glance para la UI, sin lógica
 * pesada acá — el contenido real lo calcula `DailyArtworkWorker` y solo se lee del estado.
 *
 * Layout: imagen de fondo cubriendo todo el widget (`Box` + `Image.fillMaxSize()`), con el
 * texto superpuesto abajo sobre una franja semitransparente para que se lea encima de
 * cualquier imagen. Si no hay imagen (todavía cargando, o falló la descarga), se muestra
 * solo el texto sin la franja oscura.
 */
class ArtWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Glance NO corre dentro de un `ComposeView` de Android — su composición es propia
        // (se traduce a RemoteViews), así que `androidx.compose.ui.res.stringResource()`
        // (que depende de `LocalContext`/`LocalConfiguration` de ese mundo) no aplica acá.
        // Se usa el `context` ya capturado por este closure y `context.getString()` directo.
        provideContent {
            val prefs = currentState<androidx.datastore.preferences.core.Preferences>()
            val state = prefs[STATE_KEY]?.let {
                runCatching { Json.decodeFromString<WidgetArtworkState>(it) }.getOrNull()
            }

            // Tocar el widget abre la app directo en el detalle de ESTA obra — antes no
            // hacía nada al tocarlo.
            val boxModifier = if (state != null) {
                GlanceModifier.fillMaxSize().clickable(
                    actionStartActivity<MainActivity>(
                        actionParametersOf(ARTWORK_ID_PARAM to state.artworkId)
                    )
                )
            } else {
                GlanceModifier.fillMaxSize()
            }

            Box(modifier = boxModifier, contentAlignment = Alignment.BottomStart) {
                if (state == null) {
                    Text(context.getString(R.string.widget_loading), modifier = GlanceModifier.padding(12.dp))
                    return@Box
                }

                val bitmap = state.imageFilePath
                    ?.takeIf { File(it).exists() }
                    ?.let { BitmapFactory.decodeFile(it) }

                if (bitmap != null) {
                    Image(
                        provider = ImageProvider(bitmap),
                        contentDescription = state.title,
                        contentScale = ContentScale.Crop,
                        modifier = GlanceModifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .background(if (bitmap != null) Color(0xCC000000) else Color.Transparent)
                        .padding(8.dp)
                ) {
                    val textColor = if (bitmap != null) ColorProvider(Color.White) else ColorProvider(Color.Black)
                    Text(state.title, style = TextStyle(fontWeight = FontWeight.Bold, color = textColor))
                    Text(state.artistName ?: context.getString(R.string.unknown_artist), style = TextStyle(color = textColor))
                    Text(
                        state.museum + (state.dateText?.let { " · $it" } ?: ""),
                        style = TextStyle(color = textColor)
                    )
                }
            }
        }
    }

    companion object {
        val STATE_KEY = stringPreferencesKey("artwork_json")

        /** Mismo nombre de clave que lee `MainActivity` en el extra del Intent — Glance
         * pasa los `ActionParameters` como extras usando el nombre de la key. */
        val ARTWORK_ID_PARAM = ActionParameters.Key<String>("artworkId")

        /** Llamado por `DailyArtworkWorker` tras calcular la obra del día para este widget. */
        suspend fun updateState(context: Context, glanceId: GlanceId, state: WidgetArtworkState) {
            updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                prefs.toMutablePreferences().apply { this[STATE_KEY] = Json.encodeToString(state) }
            }
            ArtWidget().update(context, glanceId)
        }
    }
}
