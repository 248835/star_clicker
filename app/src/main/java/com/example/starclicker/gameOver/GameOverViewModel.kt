package com.example.starclicker.gameOver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starclicker.database.DatabaseDao
import com.example.starclicker.database.Score
import kotlinx.coroutines.*
import timber.log.Timber

class GameOverViewModel(private val database: DatabaseDao) : ViewModel() {
    fun checkDatabase(){
        // using coroutines https://stackoverflow.com/questions/59491707/how-to-wait-for-end-of-a-coroutine
        viewModelScope.launch {
            Timber.e("NonBlocking ${getBestScoreByPoints()} ${getBestScoreByTime()}")
        }
    }

    private suspend fun getBestScoreByPoints(): Score {
        return withContext(Dispatchers.IO){
            database.getBestScoreByPoints()
        }
    }

    private suspend fun getBestScoreByTime(): Score {
        return withContext(Dispatchers.IO){
            database.getBestScoreByTime()
        }
    }

    fun insertScore(score: Score) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.insertScore(score)
            }
        }
    }
}