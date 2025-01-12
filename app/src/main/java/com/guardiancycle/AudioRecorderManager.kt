package com.guardiancycle
import android.content.Context
import android.media.MediaRecorder
import java.io.File
import java.io.IOException

class AudioRecorderManager(private val context: Context) {

    private var audioRecorder: MediaRecorder? = null

    // Function to start audio recording
    fun startAudioRecording() {
        audioRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(getRecordingFile())  // Specify output file
            try {
                prepare()
                start()  // Start recording
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle the exception (e.g., show an error message)
            }
        }
    }

    // Function to stop the audio recording
    fun stopAudioRecording() {
        try {
            audioRecorder?.apply {
                stop()  // Stop recording
                release()  // Release resources
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Function to generate the file where the audio recording will be saved
    private fun getRecordingFile(): String {
        // Create a file in the app's private storage
        val fileDir = context.filesDir  // Can use getExternalFilesDir() for external storage
        val recordingFile = File(fileDir, "audio_recording_${System.currentTimeMillis()}.mp4")

        return recordingFile.absolutePath
    }

    companion object {
        fun getRecordingFile(context: Context, mediaRecorder: MediaRecorder): String {
            val fileDir = context.filesDir  // Can use getExternalFilesDir() for external storage
            val recordingFile = File(fileDir, "audio_recording_${System.currentTimeMillis()}.mp4")

            return recordingFile.absolutePath

        }
    }
}
