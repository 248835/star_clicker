package com.example.starclicker.dialogs.boosters.info

import androidx.lifecycle.ViewModel
import com.example.starclicker.database.DatabaseDao

class BoostersInfoViewModel(val database: DatabaseDao) : ViewModel(){
    val boosters = database.getAllBoosters()
}