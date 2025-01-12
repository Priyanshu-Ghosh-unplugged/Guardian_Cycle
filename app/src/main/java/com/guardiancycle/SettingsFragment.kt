package com.guardiancycle

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.guardiancycle.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        setupShakeDetectionSettings()
        setupFakeCallSettings()
    }

    private fun setupShakeDetectionSettings() {
        // Shake detection switch
        binding.switchShakeDetection.apply {
            isChecked = sharedPreferences.getBoolean("shake_enabled", true)
            setOnCheckedChangeListener { _, isChecked ->
                sharedPreferences.edit().putBoolean("shake_enabled", isChecked).apply()
                updateShakeDetection(isChecked)
            }
        }

        // Shake sensitivity
        binding.editShakeSensitivity.apply {
            setText(sharedPreferences.getFloat("shake_sensitivity", 12.0f).toString())
            doAfterTextChanged { text ->
                text?.toString()?.toFloatOrNull()?.let { value ->
                    sharedPreferences.edit().putFloat("shake_sensitivity", value).apply()
                    updateShakeSensitivity(value)
                }
            }
        }
    }

    private fun setupFakeCallSettings() {
        // Default caller name
        binding.editDefaultCaller.apply {
            setText(sharedPreferences.getString("default_caller", "Mom"))
            doAfterTextChanged { text ->
                sharedPreferences.edit().putString("default_caller", text.toString()).apply()
            }
        }

        // Auto-answer switch
        binding.switchAutoAnswer.apply {
            isChecked = sharedPreferences.getBoolean("auto_answer", true)
            setOnCheckedChangeListener { _, isChecked ->
                sharedPreferences.edit().putBoolean("auto_answer", isChecked).apply()
            }
        }
    }

    private fun updateShakeDetection(enabled: Boolean) {
        // Update shake detection service
    }

    private fun updateShakeSensitivity(sensitivity: Float) {
        // Update shake detection threshold
    }
}