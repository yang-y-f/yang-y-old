package com.example.cafeteria.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val STORE_NAME = "cafeteria_prefs"

private val Context.dataStore by preferencesDataStore(name = STORE_NAME)

object AppPreferences {
	private lateinit var appContext: Context
	private val KEY_SERVER_URL = stringPreferencesKey("server_url")

	fun init(context: Context) {
		appContext = context.applicationContext
	}

	fun serverUrlFlow(): Flow<String> {
		return appContext.dataStore.data.map { prefs ->
			prefs[KEY_SERVER_URL] ?: "http://10.0.2.2:8000"
		}
	}

	suspend fun setServerUrl(url: String) {
		appContext.dataStore.edit { prefs ->
			prefs[KEY_SERVER_URL] = url
		}
	}
}