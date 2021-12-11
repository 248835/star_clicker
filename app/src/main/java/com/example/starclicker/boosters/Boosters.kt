package com.example.starclicker.boosters

import androidx.lifecycle.MutableLiveData

object Boosters {
    val boosters = Booster.values()

    fun clearBoosters() {
        boosters.forEach { it.active.value = false }
    }
}

enum class Booster(
    val displayedName: String,
    val description: String,
    val price: Int,
    val active: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
){
    CENTER(
        displayedName = "Centrum",
        description = "Gwiazdki spajają tylko blisko środka ekranu",
        price = 100
    ),
    SLOW(
        displayedName = "Spowolnienie",
        description = "Zmniejsza prędkość gwiazdek",
        price = 300
    ),
    ENLARGE(
        displayedName = "Powiększenie",
        description = "Zwiększa rozmiar gwiazdek",
        price = 200
    )
}