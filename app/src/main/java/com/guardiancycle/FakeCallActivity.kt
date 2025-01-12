package com.guardiancycle

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import androidx.preference.PreferenceManager
//import com.google.android.filament.View
import com.guardiancycle.TimeFormatter.formatDuration
import com.guardiancycle.databinding.ActivityFakeCallBinding
import android.view.View

class FakeCallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFakeCallBinding
    private lateinit var callTimer: CountUpTimer
    private var callDuration = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set full screen and show over lock screen
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        binding = ActivityFakeCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        startCallTimer()
    }

    private fun setupUI() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val callerName = prefs.getString("default_caller", "Mom")

        binding.apply {
            textCallerName.text = callerName
            textCallerNumber.text = "8989894566540"

            // Call actions
            buttonAnswer.setOnClickListener { answerCall() }
            buttonReject.setOnClickListener { endCall() }
            buttonEndCall.setOnClickListener { endCall() }
            buttonSpeaker.setOnClickListener { toggleSpeaker() }
            buttonMute.setOnClickListener { toggleMute() }
        }
    }

    private fun toggleMute() {
        TODO("Not yet implemented")
    }

    private fun toggleSpeaker() {
        TODO("Not yet implemented")
    }

    private fun startCallTimer() {
        callTimer = object : CountUpTimer(1000) {
            override fun onTick(millisElapsed: Long) {
                callDuration = millisElapsed
                binding.textCallDuration.text = formatDuration(millisElapsed)
            }
        }
    }

    private fun answerCall() {
        binding.apply {
            callAnswerLayout.visibility = View.GONE
            callOngoingLayout.visibility = View.VISIBLE
        }

        callTimer.start()
        startFakeConversation()
    }

    private fun startFakeConversation() {
        TODO("Not yet implemented")
    }

    private fun endCall() {
        callTimer.cancel()
        finish()
    }
}