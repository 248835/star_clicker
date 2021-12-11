package com.example.starclicker.boosters

import androidx.lifecycle.MutableLiveData

object Boosters {
    val boosters = listOf(
        Booster(
            name = "Centrum",
            description = "Gwiazdki spajają tylko blisko środka ekranu",
            price = 100
        ),
        Booster(
            name = "Powiększenie",
            description = "Zwiększa rozmiar gwiazdek",
            price = 200
        ),
        Booster(
            name = "Spowolnienie",
            description = "Zmniejsza prędkość gwiazdek",
            price = 300
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