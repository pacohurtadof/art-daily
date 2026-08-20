package com.artdaily.app.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artdaily.app.data.local.FavoriteDao
import com.artdaily.app.data.local.toArtwork
import com.artdaily.core.model.Artwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    favoriteDao: FavoriteDao
) : ViewModel() {

    /** `observeAll()` ya viene ordenado por fecha de guardado (más reciente primero) —
     * ver el `ORDER BY` en `FavoriteDao`. */
    val favorites: StateFlow<List<Artwork>> = favoriteDao.observeAll()
        .map { entities -> entities.map { it.toArtwork() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
