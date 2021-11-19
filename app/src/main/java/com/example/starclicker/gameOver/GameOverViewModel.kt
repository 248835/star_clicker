package com.example.starclicker.gameOver

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starclicker.database.DatabaseDao
import com.example.starclicker.database.Score
import kotlinx.coroutines.*
import timber.log.Timber

class GameOverViewModel(private val database: DatabaseDao) : ViewModel() {
    private val bestScore = database.getBestScore()

    fun checkDatabase(owner: LifecycleOwner){
        bestScore.observe(owner, {
            // this may fire up twice. Make sure it's not a problem
            Timber.e("$it")
        })
    }

    fun insertScore(score: Score) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.insertScore(score)
            }
        }
    }
}