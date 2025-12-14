package com.kidslearning.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {
    @Query("SELECT * FROM rewards ORDER BY cost ASC")
    fun getAllRewards(): Flow<List<Reward>>

    @Query("SELECT * FROM rewards WHERE isUnlocked = 1")
    fun getUnlockedRewards(): Flow<List<Reward>>

    @Insert
    suspend fun insertReward(reward: Reward)

    @Query("UPDATE rewards SET isUnlocked = 1 WHERE id = :rewardId")
    suspend fun unlockReward(rewardId: Int)
}