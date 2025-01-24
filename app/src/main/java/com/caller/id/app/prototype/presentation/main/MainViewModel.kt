package com.caller.id.app.prototype.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _isPermissionTaskRunning = MutableStateFlow(true)
    val isPermissionTaskRunning = _isPermissionTaskRunning.asStateFlow()

    fun setPermissionTaskRunningFalse() {
        viewModelScope.launch{
            delay(1000)
            _isPermissionTaskRunning.value = false
        }
    }


}