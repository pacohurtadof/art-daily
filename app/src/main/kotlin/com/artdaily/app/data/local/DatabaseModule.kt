package com.artdaily.app.data.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * `createFromAsset` copia `assets/artworks.db` (generado por `:harvester`, ver
 * `docs/etapa2-diseno-arquitectura.md` sección 7) como base de datos inicial en el primer
 * arranque — así la app tiene obras para mostrar sin haber tocado la red todavía.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .createFromAsset(AppDatabase.DATABASE_NAME)
            // Sin usuarios reales todavía (pre-lanzamiento) — no hay migraciones escritas
            // a propósito. Si sube `AppDatabase.version`, Room recrea el archivo entero (y
            // vuelve a copiar el asset empaquetado) en vez de tronar al abrir.
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideArtworkDao(db: AppDatabase): ArtworkDao = db.artworkDao()

    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()

    @Provides
    fun provideHistoryDao(db: AppDatabase): HistoryDao = db.historyDao()

    @Provides
    fun provideWidgetConfigDao(db: AppDatabase): WidgetConfigDao = db.widgetConfigDao()
}
