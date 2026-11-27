package com.example.superstroy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superstroy.data.hardware.AudioRecorder
import com.example.superstroy.data.remote.DefectRemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoiceInputViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val remoteDataSource: DefectRemoteDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow<VoiceState>(VoiceState.Idle)
    val uiState: StateFlow<VoiceState> = _uiState.asStateFlow()

    sealed class VoiceState {
        object Idle : VoiceState()
        object Recording : VoiceState()
        object Processing : VoiceState()
        data class Success(val text: String) : VoiceState()
        data class Error(val message: String) : VoiceState()
    }

    fun startRecording() {
        try {
            audioRecorder.startRecording()
            _uiState.value = VoiceState.Recording
        } catch (e: Exception) {
            _uiState.value = VoiceState.Error("Ошибка начала записи: ${e.message}")
        }
    }

    fun stopRecordingAndProcess() {
        val file = audioRecorder.stopRecording()
        if (file == null || file.length() == 0L) {
            _uiState.value = VoiceState.Error("Запись не удалась или пуста")
            return
        }

        _uiState.value = VoiceState.Processing

        viewModelScope.launch {
            try {
                val recognizedText = remoteDataSource.recognizeSpeech(file)
                if (recognizedText.isBlank()) {
                    _uiState.value = VoiceState.Error("Речь не распознана. Попробуйте снова.")
                } else {
                    _uiState.value = VoiceState.Success(recognizedText)
                }
            } catch (e: Exception) {
                _uiState.value = VoiceState.Error("Ошибка сети: ${e.message}")
            } finally {
                file.delete() // Удаляем временный файл после отправки
            }
        }
    }

    fun resetState() {
        _uiState.value = VoiceState.Idle
    }
}
