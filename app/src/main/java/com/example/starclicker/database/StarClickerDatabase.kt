package com.example.starclicker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Score::class, OwnedBooster::class, Booster::class],
    version = 3,
    exportSchema = false
)
abstract class StarClickerDatabase: RoomDatabase() {
    abstract val databaseDao: DatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: StarClickerDatabase? = null

        fun getInstance(context: Context): StarClickerDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StarClickerDatabase::class.java,
                        "star_clicker_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}