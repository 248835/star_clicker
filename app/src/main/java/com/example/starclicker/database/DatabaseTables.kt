package com.example.starclicker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_table")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val points: Int
)