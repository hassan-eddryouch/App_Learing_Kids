package com.kidslearning.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM progress WHERE language = :language ORDER BY letter ASC")
    fun getProgressByLanguage(language: String): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM progress WHERE letter = :letter AND language = :language LIMIT 1")
    suspend fun getProgress(letter: String, language: String): ProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: ProgressEntity)

    @Update
    suspend fun updateProgress(progress: ProgressEntity)

    @Delete
    suspend fun deleteProgress(progress: ProgressEntity)

    @Query("DELETE FROM progress")
    suspend fun deleteAllProgress()

    @Query("SELECT COUNT(*) FROM progress WHERE completed = 1 AND language = :language")
    fun getCompletedCount(language: String): Flow<Int>

    @Query("SELECT * FROM progress ORDER BY lastAttemptDate DESC LIMIT 5")
    fun getRecentProgress(): Flow<List<ProgressEntity>>

    @Query("SELECT COUNT(*) FROM progress WHERE language = :language")
    suspend fun getTotalLetterCount(language: String): Int
}