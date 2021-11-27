package com.example.starclicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.starclicker.database.DatabaseDao
import com.example.starclicker.dialogs.boosters.info.BoostersInfoViewModel
import com.example.starclicker.dialogs.boosters.shop.BoostersViewModel
import com.example.starclicker.game.GameViewModel
import com.example.starclicker.gameOver.GameOverViewModel

class ViewModelFactory(private val database: DatabaseDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(database) as T
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(database) as T
            modelClass.isAssignableFrom(BoostersViewModel::class.java) -> BoostersViewModel(database) as T
            modelClass.isAssignableFrom(BoostersInfoViewModel::class.java) -> BoostersInfoViewModel(database) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}