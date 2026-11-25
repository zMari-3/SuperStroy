package com.example.superstroy.domain.repository

class FakeAudioRecorder : AudioRecorder {
    private var isRecording = false
    override fun startRecording() {  isRecording = true }
    override fun stopRecording(): String = "fake_audio_path.mp3"
}
