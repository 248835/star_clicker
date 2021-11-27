package com.example.starclicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.starclicker.database.Booster
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

        setBoosters()

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        navController = findNavController(R.id.myNavHostFragment)

        Timber.plant(Timber.DebugTree())
    }

    private fun setBoosters() {
        lifecycleScope.launchWhenCreated {
            listOf(
                Booster(
                    id = 1,
                    name = "Booster 1",
                    description = "Zajebisty booster 1 xd",
                    price = 123
                ),
                Booster(
                    id = 2,
                    name = "Booster 2 z dluga nazwa",
                    description = "Zajebisty booster 2 xd z opisem, który na potrzeby tego widoku nazywamy długim",
                    price = 234
                ),
                Booster(
                    id = 3,
                    name = "Booster 3 z dluga nazwa",
                    description = "Zajebisty booster 2 xd z opisem, który na potrzeby tego widoku nazywamy długim",
                    price = 9999
                ),
                Booster(
                    id = 4,
                    name = "Booster 4 z dluga nazwa",
                    description = "Zajebisty booster 2 xd z opisem, który na potrzeby tego widoku nazywamy długim",
                    price = 0
                )
            ).forEach {
                database.insertBooster(it)
            }
        }
    }
}