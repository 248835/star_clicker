package com.example.starclicker.boosters

import androidx.lifecycle.MutableLiveData

object Boosters {
    val boosters = listOf(
        Booster(
            name = "Booster 1",
            description = "Zajebisty booster 1 xd",
            price = 123
        ),
        Booster(
            name = "Booster 2 z dluga nazwa",
            description = "Zajebisty booster 2 xd z opisem, który na potrzeby tego widoku nazywamy długim",
            price = 234
        ),
        Booster(
            name = "Booster 3 z dluga nazwa",
            description = "Zajebisty booster 2 xd z opisem, który na potrzeby tego widoku nazywamy długim",
            price = 9999
        ),
        Booster(
            name = "Booster 4 z dluga nazwa",
            description = "Zajebisty booster 2 xd z opisem, który na potrzeby tego widoku nazywamy długim",
            price = 0
        ),
    )

    fun clearBoosters() {
        boosters.forEach { it.active.value = false }
    }
}

data class Booster(
    val name: String,

    val description: String,

    val price: Int,

    val active: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
)