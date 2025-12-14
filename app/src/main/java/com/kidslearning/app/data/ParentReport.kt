package com.kidslearning.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parent_reports")
data class ParentReport(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val kidProfileId: Int,
    val weekStart: String, // "YYYY-MM-DD"
    val totalTimeMinutes: Int,
    val lettersLearned: Int,
    val gamesPlayed: Int,
    val accuracy: Float,
    val improvementAreas: String, // JSON string
    val strengths: String, // JSON string
    val generatedDate: Long = System.currentTimeMillis()
)