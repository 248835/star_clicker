package com.example.starclicker.ui.starView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class StarViewModel: ViewModel() {

    private var _isRunning = true
    val isRunning: Boolean
        get() = _isRunning

    private var _starDelay = 1000L

    fun setStarDelay(delay: Long) {
        _starDelay = delay
    }

    fun start() {
        _isRunning = true
    }

    fun stop() {
        _isRunning = false
    }

    fun shower(star: () -> Unit) {
        viewModelScope.launch {
            while (_isRunning) {
                star.invoke()
                delay(_starDelay)
            }
        }
    }
}