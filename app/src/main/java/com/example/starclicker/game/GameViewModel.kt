package com.example.starclicker.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starclicker.boosters.Booster
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class GameViewModel : ViewModel() {

    //Shaking mode variables
    private enum class AccDirection {TOP, BOTTOM}
    private var previousAccDirection = AccDirection.TOP
    private var shakingModeEnabled = false

    fun startShakingMode(onFinished : () -> Unit){
        viewModelScope.launch {
            shakingModeEnabled = true
            delay(SHAKING_MODE_DURATION)
            shakingModeEnabled = false
            onFinished()
        }
    }

    fun handleSensorData(verticalAcc : Float){
        if(!shakingModeEnabled) return

        if(previousAccDirection == AccDirection.TOP){
            if(verticalAcc >= ACC_THRESHOLD_TO_GET_POINTS){
                addPoints(POINTS_PER_SHAKE)
                previousAccDirection = AccDirection.BOTTOM
            }
        }else if(previousAccDirection == AccDirection.BOTTOM){
            if(verticalAcc <= -ACC_THRESHOLD_TO_GET_POINTS){
                addPoints(POINTS_PER_SHAKE)
                previousAccDirection = AccDirection.TOP
            }
        }
    }

    fun startCountdown(onCountdownChange : (countdown : Int) -> Unit){
        viewModelScope.launch {
            var countdown = 3

            onCountdownChange(countdown)
            while(countdown > 0) {
                delay(COUNTDOWN_PERIOD)
                countdown--
                onCountdownChange(countdown)
            }
        }
    }

    private val _score = MutableLiveData(100)
    val score: LiveData<Int>
        get() = _score

    private val _progressBar = MutableLiveData(100)
    val progressBar: LiveData<Int>
        get() = _progressBar

    fun addPoints(points: Int) {
        val newScore = _score.value!! + points
        _score.value = newScore
        if (_progressBar.value!! < 100)
            _progressBar.value = _progressBar.value?.plus(points)
    }

    fun decreasePointsOverTime(){
        viewModelScope.launch {
            while (true) {
                delay(100L)
                _progressBar.value = _progressBar.value?.dec()
            }
        }
    }

    fun deactivateAfterDelay(booster : Booster){
        viewModelScope.launch {
            delay(BOOSTER_LIFE_TIME)
            booster.active.value = false
        }
    }

    companion object {
        private const val COUNTDOWN_PERIOD = 1000L

        private const val BOOSTER_LIFE_TIME = 8000L

        //Shaking mode constants
        private const val ACC_THRESHOLD_TO_GET_POINTS = 1f
        private const val POINTS_PER_SHAKE = 40
        private const val SHAKING_MODE_DURATION = 8000L
    }
}