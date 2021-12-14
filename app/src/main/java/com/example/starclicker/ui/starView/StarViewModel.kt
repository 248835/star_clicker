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

    private var lastSpawnedSpecialTime = 0L

    private var specialSpawnTimePeriod = 10000L

    fun starSizeBoosterModifier(): Float {
        return if(Booster.ENLARGE.active.value!!) 2f else 1f
    }

    fun starSpawnWidthBoosterModifier() : Float {
        return if(Booster.CENTER.active.value!!) 0.25f + Math.random().toFloat() * 0.5f else Math.random().toFloat()
    }

    fun starSpeedBoosterModifier() : Float {
        return if(Booster.SLOW.active.value!!) 0.5f else 1f
    }

    fun starSpawnDelay() : Long
    {
        return (500L + Math.random() * 1000L).toLong();
    }

    fun start() {
        _isRunning = true
    }

    fun stop() {
        _isRunning = false
    }

    fun shower(star: () -> Unit, specialStar: () -> Unit) {
        viewModelScope.launch {
            while (_isRunning) {

                val delay = starSpawnDelay()

                lastSpawnedSpecialTime += delay

                if(lastSpawnedSpecialTime > specialSpawnTimePeriod)
                {
                    lastSpawnedSpecialTime -= specialSpawnTimePeriod;
                    specialStar.invoke()
                }
                else
                {
                    star()
                }

                delay(delay)
            }
        }
    }
}