package com.guardiancycle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.telephony.SmsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch



class SOSManager(

    private val context: Context,
    private val contactsRepository: EmergencyContactsRepository,
    private val locationRepository: LocationRepository
) {
    private var audioRecorder: MediaRecorder? = null

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun triggerSOS() {
        scope.launch {
            try {


                // 2. Send alerts to emergency contacts
                val contacts = contactsRepository.getEmergencyContacts()
                contacts.forEach { contact ->
                    val location = "I am in problem, need help"
                    sendEmergencyAlert(contact, location)
                }

                // 3. Start recording audio
                startAudioRecording()

                // 4. Activate siren
                playSiren()

                // 5. Start foreground location tracking
                startLocationTracking()

            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    @SuppressLint("NewApi")
    private fun sendEmergencyAlert(contact: EmergencyContact, location: String) {
        val message = """
            EMERGENCY ALERT
            
            Location: I am in problem help
            Time: ${System.currentTimeMillis()}
        """.trimIndent()

        SmsManager.getDefault().sendTextMessage(
            contact.phoneNumber,
            null,
            message,
            null,
            null
        )
    }

    private fun startAudioRecording() {
        audioRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(AudioRecorderManager.getRecordingFile((context),this))
            prepare()
            start()
        }
    }

    private fun playSiren() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.siren_sound)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    private fun startLocationTracking() {
        context.startService(Intent(context, LocationService::class.java))
    }

    companion object {
        val triggerSOS = mapOf(
            "TRIGGER_BUTTON" to "BUTTON",
            "TRIGGER_SHAKE" to "SHAKE"
        )
    }
}

