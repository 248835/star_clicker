package com.example.starclicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.starclicker.database.DatabaseDao
import com.example.starclicker.database.StarClickerDatabase
import com.example.starclicker.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var database: DatabaseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = StarClickerDatabase.getInstance(this).databaseDao

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        navController = findNavController(R.id.myNavHostFragment)

        Timber.plant(Timber.DebugTree())
    }
}