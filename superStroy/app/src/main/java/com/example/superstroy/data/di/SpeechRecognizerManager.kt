package com.example.superstroy.data.di

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class SpeechRecognizerManager(private val context: Context) {

    private val _speechState = MutableStateFlow<SpeechState>(SpeechState.Idle)
    val speechState: StateFlow<SpeechState> = _speechState

    private var speechRecognizer: SpeechRecognizer? = null

    // Состояния для UI
    sealed class SpeechState {
        object Idle : SpeechState()
        object Listening : SpeechState()
        data class Success(val text: String) : SpeechState()
        data class Error(val message: String) : SpeechState()
    }

    fun startListening() {
        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()) // Русский язык
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _speechState.value = SpeechState.Listening
            }

            override fun onBeginningOfSpeech() {
                /* Не используется */
            }

            override fun onRmsChanged(rmsdB: Float) {
                /* Не используется */
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                /* Не используется */
            }

            override fun onEndOfSpeech() {
                /* Не используется */
            }

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Ошибка записи аудио"
                    SpeechRecognizer.ERROR_CLIENT -> "Ошибка клиента"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Нет разрешения на микрофон"
                    SpeechRecognizer.ERROR_NETWORK -> "Ошибка сети"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Таймаут сети"
                    SpeechRecognizer.ERROR_NO_MATCH -> "Речь не распознана. Попробуйте еще раз."
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Сервис распознавания занят"
                    SpeechRecognizer.ERROR_SERVER -> "Ошибка сервера"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Вы не начали говорить"
                    else -> "Неизвестная ошибка распознавания"
                }
                _speechState.value = SpeechState.Error(errorMessage)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val recognizedText = matches?.firstOrNull() ?: ""
                _speechState.value = SpeechState.Success(recognizedText)
            }

            override fun onPartialResults(partialResults: Bundle?) {
                /* Не используется */
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                /* Не используется */
            }
        })

        speechRecognizer?.startListening(intent)
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
    }

    fun destroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }
}
