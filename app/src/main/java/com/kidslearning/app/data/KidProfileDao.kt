package com.kidslearning.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface KidProfileDao {
    @Query("SELECT * FROM kid_profiles ORDER BY lastActiveDate DESC")
    fun getAllProfiles(): Flow<List<KidProfile>>

    @Query("SELECT * FROM kid_profiles WHERE id = :profileId")
    suspend fun getProfile(profileId: Int): KidProfile?

    @Insert
    suspend fun insertProfile(profile: KidProfile): Long

    @Update
    suspend fun updateProfile(profile: KidProfile)

    @Delete
    suspend fun deleteProfile(profile: KidProfile)

    @Query("UPDATE kid_profiles SET totalStars = totalStars + :stars WHERE id = :profileId")
    suspend fun addStars(profileId: Int, stars: Int)

    @Query("UPDATE kid_profiles SET totalCoins = totalCoins + :coins WHERE id = :profileId")
    suspend fun addCoins(profileId: Int, coins: Int)

    @Query("UPDATE kid_profiles SET level = :level WHERE id = :profileId")
    suspend fun updateLevel(profileId: Int, level: Int)
}
