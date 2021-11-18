package com.example.starclicker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_table")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val points: Int,

    val time: Long
)

@Entity(tableName = "owned_boosters_table")
data class OwnedBooster(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val boosterId: Int,

    val isUsable: Boolean
)