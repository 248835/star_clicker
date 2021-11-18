package com.example.starclicker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DatabaseDao {
    @Insert
    suspend fun insertScore(score: Score)

    @Query("SELECT * FROM score_table ORDER BY points DESC LIMIT 1")
    suspend fun getBestScoreByPoints(): Score

    @Query("SELECT * FROM score_table ORDER BY time LIMIT 1")
    suspend fun getBestScoreByTime(): Score

    @Query("SELECT SUM(points) FROM score_table")
    suspend fun getPointsSum(): Long

    @Insert
    suspend fun insertOwnedBoosters(ownedBooster: OwnedBooster)

    @Update
    suspend fun updateOwnedBoosters(ownedBooster: OwnedBooster)

    @Query("SELECT * FROM owned_boosters_table WHERE isUsable=1")
    fun getUsableOwnedBoosters(): LiveData<List<OwnedBooster>>

    @Query("SELECT * FROM owned_boosters_table")
    fun getAllOwnedBoosters(): LiveData<List<OwnedBooster>>
}

// using LiveData to wait on result: https://proandroiddev.com/livedata-transformations-4f120ac046fc
// using coroutines https://stackoverflow.com/questions/59491707/how-to-wait-for-end-of-a-coroutine
