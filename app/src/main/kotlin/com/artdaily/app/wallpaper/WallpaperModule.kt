package com.artdaily.app.wallpaper

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** `@Binds` (no `@Provides`) porque solo mapea interfaz -> implementación, sin lógica extra
 * — mismo patrón que `RepositoryModule`/`SettingsModule`. */
@Module
@InstallIn(SingletonComponent::class)
abstract class WallpaperModule {
    @Binds
    abstract fun bindWallpaperApplier(impl: WallpaperApplierImpl): WallpaperApplier
}
