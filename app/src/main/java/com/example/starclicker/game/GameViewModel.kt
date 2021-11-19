package com.example.starclicker.game

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starclicker.database.DatabaseDao
import com.example.starclicker.database.Score
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class GameViewModel(val database: DatabaseDao) : ViewModel() {

    private var _starDelay = 1000L

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _progressBar = MutableLiveData(100)
    val progressBar: LiveData<Int>
        get() = _progressBar

    fun addPoints(points: Int) {
        val newScore = _score.value!! + points
        _score.value = newScore
    }

    fun randomValues() {
        viewModelScope.launch {
            while (true) {
                delay(_starDelay)
                addPoints((Math.random() * 100).toInt())
                _progressBar.value = (Math.random() * 100).toInt()
            }
        }
    }


}