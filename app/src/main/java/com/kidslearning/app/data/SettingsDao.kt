package com.kidslearning.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<SettingsEntity?>

    @Query("SELECT * FROM settings WHERE id = 1 LIMIT 1")
    suspend fun getSettingsOnce(): SettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: SettingsEntity)

    @Update
    suspend fun updateSettings(settings: SettingsEntity)

    @Query("UPDATE settings SET soundEnabled = :enabled WHERE id = 1")
    suspend fun updateSoundEnabled(enabled: Boolean)

    @Query("UPDATE settings SET musicEnabled = :enabled WHERE id = 1")
    suspend fun updateMusicEnabled(enabled: Boolean)

    @Query("UPDATE settings SET vibrationEnabled = :enabled WHERE id = 1")
    suspend fun updateVibrationEnabled(enabled: Boolean)

    @Query("UPDATE settings SET difficulty = :difficulty WHERE id = 1")
    suspend fun updateDifficulty(difficulty: String)
}