package com.example.starclicker.dialogs.boost

import androidx.lifecycle.ViewModel
import com.example.starclicker.database.DatabaseDao

class BoostViewModel(val database: DatabaseDao) : ViewModel()  {
    val boosters = database.getAllBoosters()

}