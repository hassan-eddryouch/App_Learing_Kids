package com.kidslearning.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyStreakDao {
    @Query("SELECT * FROM daily_streaks WHERE kidProfileId = :profileId ORDER BY date DESC LIMIT 30")
    fun getStreaks(profileId: Int): Flow<List<DailyStreak>>

    @Query("SELECT * FROM daily_streaks WHERE kidProfileId = :profileId AND date = :date")
    suspend fun getStreakForDate(profileId: Int, date: String): DailyStreak?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStreak(streak: DailyStreak)

    @Update
    suspend fun updateStreak(streak: DailyStreak)
}