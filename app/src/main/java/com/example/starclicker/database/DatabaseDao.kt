package com.example.starclicker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DatabaseDao {
    // Score
    @Insert
    suspend fun insertScore(score: Score)

    @Query("SELECT * FROM score_table ORDER BY points DESC LIMIT 1")
    fun getBestScore(): LiveData<Score>

    @Query("SELECT SUM(points) FROM score_table")
    fun getPointsSum(): LiveData<Long>
}

// using LiveData to wait on result: https://proandroiddev.com/livedata-transformations-4f120ac046fc
// using coroutines https://stackoverflow.com/questions/59491707/how-to-wait-for-end-of-a-coroutine
