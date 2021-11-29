package com.example.starclicker.dialogs.boosters

import androidx.lifecycle.ViewModel
import com.example.starclicker.database.DatabaseDao

class BoostersViewModel(val database: DatabaseDao) : ViewModel()  {
    val boosters = database.getAllBoosters()

}