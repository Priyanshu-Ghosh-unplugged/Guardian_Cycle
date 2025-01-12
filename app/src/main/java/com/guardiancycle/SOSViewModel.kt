package com.guardiancycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SOSViewModel : ViewModel() {
    private val _sosState = MutableStateFlow<SOSState>(SOSState.Idle)
    val sosState: StateFlow<SOSState> = _sosState

    fun triggerSOS() {
        viewModelScope.launch {
            _sosState.value = SOSState.Triggered
            // Implement SOS logic here
        }
    }
}

sealed class SOSState {
    object Idle : SOSState()
    object Triggered : SOSState()
    data class Error(val message: String) : SOSState()
}