package com.example.superstroy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstroy.data.local.SettingsPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: SettingsPreferences
) : ViewModel() {

    // Превращаем Flow в StateFlow, чтобы Compose мог их читать
    val remindersEnabled = preferences.remindersEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val statusChangesEnabled = preferences.statusChangesEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val syncEnabled = preferences.syncEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val offlineModeEnabled = preferences.offlineModeEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    val theme = preferences.theme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "Светлая"
    )

    // Методы для обновления настроек
    fun toggleReminders(enabled: Boolean) {
        viewModelScope.launch { preferences.setRemindersEnabled(enabled) }
    }
    fun toggleStatusChanges(enabled: Boolean) {
        viewModelScope.launch { preferences.setStatusChangesEnabled(enabled) }
    }
    fun toggleSync(enabled: Boolean) {
        viewModelScope.launch { preferences.setSyncEnabled(enabled) }
    }
    fun toggleOfflineMode(enabled: Boolean) {
        viewModelScope.launch { preferences.setOfflineModeEnabled(enabled) }
    }
    fun changeTheme(newTheme: String) {
        viewModelScope.launch { preferences.setTheme(newTheme) }
    }
}