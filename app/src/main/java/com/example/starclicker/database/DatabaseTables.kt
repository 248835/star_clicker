package com.example.starclicker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_table")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val points: Int
)

@Entity(tableName = "owned_boosters_table")
data class OwnedBooster(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val boosterId: Int,

    val isUsable: Boolean
)

@Entity(tableName = "booster_table")
data class Booster(
    // unique - on conflict gets replaced
    @PrimaryKey
    val id: Long,

    val name: String,

    val description: String,

    val price: Int
)