package com.artdaily.app.data.repository

import com.artdaily.core.repository.ArtworkRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** `@Binds` (no `@Provides`) porque solo mapea interfaz -> implementación, sin lógica extra. */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindArtworkRepository(impl: ArtworkRepositoryImpl): ArtworkRepository
}
