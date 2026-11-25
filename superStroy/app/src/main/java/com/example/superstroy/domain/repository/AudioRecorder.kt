package com.example.superstroy.domain.repository

interface AudioRecorder {
    fun startRecording()
    fun stopRecording(): String
}
