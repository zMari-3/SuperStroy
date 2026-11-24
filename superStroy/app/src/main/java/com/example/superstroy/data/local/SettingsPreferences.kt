package com.example.superstroy.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings_prefs")

class SettingsPreferences(private val context: Context) {

    companion object {
        val REMINDERS_KEY = booleanPreferencesKey("reminders_enabled")
        val STATUS_CHANGES_KEY = booleanPreferencesKey("status_changes_enabled")
        val SYNC_ENABLED_KEY = booleanPreferencesKey("sync_enabled")
        val OFFLINE_MODE_KEY = booleanPreferencesKey("offline_mode_enabled")
        val THEME_KEY = stringPreferencesKey("app_theme")
    }

    // Чтение настроек (Flow)
    val remindersEnabled: Flow<Boolean> = context.dataStore.data.map { it[REMINDERS_KEY] ?: true }
    val statusChangesEnabled: Flow<Boolean> = context.dataStore.data.map { it[STATUS_CHANGES_KEY] ?: true }
    val syncEnabled: Flow<Boolean> = context.dataStore.data.map { it[SYNC_ENABLED_KEY] ?: true }
    val offlineModeEnabled: Flow<Boolean> = context.dataStore.data.map { it[OFFLINE_MODE_KEY] ?: false }
    val theme: Flow<String> = context.dataStore.data.map { it[THEME_KEY] ?: "Системная" }

    // Сохранение настроек (suspend)
    suspend fun setRemindersEnabled(enabled: Boolean) {
        context.dataStore.edit { it[REMINDERS_KEY] = enabled }
    }
    suspend fun setStatusChangesEnabled(enabled: Boolean) {
        context.dataStore.edit { it[STATUS_CHANGES_KEY] = enabled }
    }
    suspend fun setSyncEnabled(enabled: Boolean) {
        context.dataStore.edit { it[SYNC_ENABLED_KEY] = enabled }
    }
    suspend fun setOfflineModeEnabled(enabled: Boolean) {
        context.dataStore.edit { it[OFFLINE_MODE_KEY] = enabled }
    }
    suspend fun setTheme(themeName: String) {
        context.dataStore.edit { it[THEME_KEY] = themeName }
    }
}
