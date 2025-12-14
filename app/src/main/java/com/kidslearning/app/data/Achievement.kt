package com.kidslearning.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val kidProfileId: Int,
    val achievementType: String, // "first_letter", "10_letters", "perfect_game", etc.
    val title: String,
    val description: String,
    val emoji: String,
    val stars: Int,
    val coins: Int,
    val unlockedDate: Long = System.currentTimeMillis(),
    val isUnlocked: Boolean = false
)