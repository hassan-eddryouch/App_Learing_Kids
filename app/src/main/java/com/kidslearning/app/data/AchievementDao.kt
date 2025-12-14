package com.kidslearning.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements WHERE kidProfileId = :profileId ORDER BY unlockedDate DESC")
    fun getAchievements(profileId: Int): Flow<List<Achievement>>

    @Query("SELECT * FROM achievements WHERE kidProfileId = :profileId AND isUnlocked = 1")
    fun getUnlockedAchievements(profileId: Int): Flow<List<Achievement>>

    @Insert
    suspend fun insertAchievement(achievement: Achievement)

    @Update
    suspend fun updateAchievement(achievement: Achievement)

    @Query("UPDATE achievements SET isUnlocked = 1 WHERE id = :achievementId")
    suspend fun unlockAchievement(achievementId: Int)
}