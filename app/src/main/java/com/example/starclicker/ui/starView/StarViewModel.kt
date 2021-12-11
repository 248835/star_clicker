package com.example.starclicker.ui.starView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starclicker.boosters.Booster
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class StarViewModel: ViewModel() {

    private var _isRunning = true
    val isRunning: Boolean
        get() = _isRunning

    private var _starDelay = 1000L

    fun starSizeBoosterModifier(): Float {
        return if(Booster.ENLARGE.active.value!!) 2f else 1f
    }

    fun starSpawnWidthBoosterModifier() : Float {
        return if(Booster.CENTER.active.value!!) 0.5f else 1f
    }

    fun starSpeedBoosterModifier() : Float {
        return if(Booster.SLOW.active.value!!) 0.5f else 1f
    }


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