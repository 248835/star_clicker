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

    // OwnedBooster
    @Insert
    suspend fun insertOwnedBoosters(ownedBooster: OwnedBooster)

    @Update
    suspend fun updateOwnedBoosters(ownedBooster: OwnedBooster)

    @Query("SELECT * FROM owned_boosters_table WHERE isUsable=1")
    fun getUsableOwnedBoosters(): LiveData<List<OwnedBooster>>

    @Query("SELECT * FROM owned_boosters_table")
    fun getAllOwnedBoosters(): LiveData<List<OwnedBooster>>

    // Booster
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooster(booster: Booster)

    @Query("SELECT * FROM booster_table")
    fun getAllBoosters(): LiveData<List<Booster>>

    @Query("SELECT * FROM booster_table WHERE id=:id")
    fun getBooster(id: Int): LiveData<Booster>

    @Query("SELECT * FROM booster_table WHERE name=:name")
    fun getBooster(name: String): LiveData<Booster>

    @Query("DELETE FROM booster_table")
    suspend fun clearBoosters()
}

// using LiveData to wait on result: https://proandroiddev.com/livedata-transformations-4f120ac046fc
// using coroutines https://stackoverflow.com/questions/59491707/how-to-wait-for-end-of-a-coroutine
