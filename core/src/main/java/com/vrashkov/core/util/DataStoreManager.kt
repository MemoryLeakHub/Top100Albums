package com.vrashkov.core.util

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
private val Context.settingsDataStore by preferencesDataStore("settings")
class DataStoreManager @Inject constructor(@ApplicationContext private val appContext: Context) {

    private val settingsDataStore = appContext.settingsDataStore

    companion object {
        val albumId = stringPreferencesKey("album_id")
    }

    suspend fun setAlbumId(value: String) {
        settingsDataStore.edit { settings ->
            settings[albumId] = value
        }
    }

    val getAlbumId: Flow<String> = appContext.settingsDataStore.data.map { settings ->
        settings[albumId] ?: ""
    }

}