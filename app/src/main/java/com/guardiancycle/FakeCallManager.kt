package com.guardiancycle

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.NotificationCompat
import com.google.common.collect.ComparisonChain.start


class FakeCallManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private var timer: CountDownTimer? = null

    fun initiateCall(callerName: String = "Mom") {
        // Show incoming call UI
        showIncomingCallNotification(callerName)

        // Start vibration
        startVibration()

        // Start ringtone
        playRingtone()

        // Auto-answer after 5 seconds
        startAutoAnswerTimer()
    }

    @SuppressLint("ServiceCast", "NotificationPermission")
    private fun showIncomingCallNotification(callerName: String) {
        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        val channelId = "fake_call_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Fake Calls",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                enableLights(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val fullScreenIntent = Intent(context, FakeCallActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0, fullScreenIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_phone)
            .setContentTitle("Incoming Call")
            .setContentText(callerName)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun startVibration() {
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(0, 1000, 1000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(pattern, 0)
        }
    }

    private fun playRingtone() {
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, ringtoneUri)
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            isLooping = true
            prepare()
            start()
        }
    }

    private fun startAutoAnswerTimer() {
        timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                answerCall()
            }
        }.start()
    }

    fun answerCall() {
        // Stop ringtone and vibration
        mediaPlayer?.stop()
        vibrator?.cancel()
        timer?.cancel()

        // Play pre-recorded conversation
        playFakeConversation()
    }

    private fun playFakeConversation() {
        mediaPlayer = MediaPlayer.create(context, R.raw.fake_conversation).apply {
            start()
            setOnCompletionListener {
                endCall()
            }
        }
    }

    fun endCall() {
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator?.cancel()
        timer?.cancel()
    }
}