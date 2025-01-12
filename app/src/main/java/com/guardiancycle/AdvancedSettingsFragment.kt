package com.guardiancycle

import android.R
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.preference.PreferenceManager
import com.guardiancycle.databinding.FragmentAdvancedSettingsBinding

class AdvancedSettingsFragment : Fragment() {

    private lateinit var binding: FragmentAdvancedSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val KEY_POWER_BUTTON_SOS = "power_button_sos"
        private const val KEY_LOCATION_FREQUENCY = "location_frequency"
        private const val KEY_AUDIO_RECORDING_ENABLED = "audio_recording_enabled"
        private const val DEFAULT_POWER_BUTTON_SOS = true
        private const val DEFAULT_LOCATION_FREQUENCY = 1
        private const val DEFAULT_AUDIO_RECORDING_ENABLED = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdvancedSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        setupAdvancedSettings()
    }

    private fun setupAdvancedSettings() {
        setupPanicButtonSwitch()
        setupLocationFrequencySpinner()
        setupAudioRecordingSwitch()
    }

    private fun setupPanicButtonSwitch() {
        binding.switchPowerButtonTrigger.apply {
            isChecked = sharedPreferences.getBoolean(KEY_POWER_BUTTON_SOS, DEFAULT_POWER_BUTTON_SOS)
            setOnCheckedChangeListener { _, isChecked ->
                updatePreference(KEY_POWER_BUTTON_SOS, isChecked)
            }
        }
    }

    private fun setupLocationFrequencySpinner() {
        val frequencies = arrayOf("5 seconds", "10 seconds", "30 seconds", "1 minute")
        binding.spinnerLocationFrequency.apply {
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_dropdown_item,
                frequencies
            )
            setSelection(
                sharedPreferences.getInt(KEY_LOCATION_FREQUENCY, DEFAULT_LOCATION_FREQUENCY)
            )
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    updatePreference(KEY_LOCATION_FREQUENCY, position)
                    updateLocationTrackingFrequency(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No-op
                }
            }
        }
    }

    private fun setupAudioRecordingSwitch() {
        binding.switchAudioRecording.apply {
            isChecked = sharedPreferences.getBoolean(
                KEY_AUDIO_RECORDING_ENABLED,
                DEFAULT_AUDIO_RECORDING_ENABLED
            )
            setOnCheckedChangeListener { _, isChecked ->
                updatePreference(KEY_AUDIO_RECORDING_ENABLED, isChecked)
            }
        }
    }

    private fun updateLocationTrackingFrequency(position: Int) {
        val frequencyInMillis = when (position) {
            0 -> 5000L
            1 -> 10000L
            2 -> 30000L
            3 -> 60000L
            else -> 10000L
        }
        // TODO: Update location service frequency with the `frequencyInMillis`
    }

    private fun updatePreference(key: String, value: Any) {
        with(sharedPreferences.edit()) {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("Unsupported preference value type")
            }
            apply()
        }
    }
}
