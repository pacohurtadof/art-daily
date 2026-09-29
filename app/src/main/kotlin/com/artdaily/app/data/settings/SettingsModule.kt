package com.artdaily.app.data.settings

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** `@Binds` (no `@Provides`) porque solo mapea interfaz -> implementación, sin lógica extra
 * — mismo patrón que `RepositoryModule`. */
@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    abstract fun bindWallpaperPreferences(impl: WallpaperPreferencesImpl): WallpaperPreferences
}
