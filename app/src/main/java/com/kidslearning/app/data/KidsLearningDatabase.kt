package com.kidslearning.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProgressEntity::class,
        GameScoreEntity::class,
        SettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KidsLearningDatabase : RoomDatabase() {
    
    abstract fun progressDao(): ProgressDao
    abstract fun gameScoreDao(): GameScoreDao
    abstract fun settingsDao(): SettingsDao
    
    companion object {
        @Volatile
        private var INSTANCE: KidsLearningDatabase? = null
        
        fun getDatabase(context: Context): KidsLearningDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KidsLearningDatabase::class.java,
                    "kids_learning_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}