package com.kidslearning.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_streaks")
data class DailyStreak(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val kidProfileId: Int,
    val date: String, // "YYYY-MM-DD"
    val activitiesCompleted: Int = 0,
    val minutesSpent: Int = 0,
    val starsEarned: Int = 0
)