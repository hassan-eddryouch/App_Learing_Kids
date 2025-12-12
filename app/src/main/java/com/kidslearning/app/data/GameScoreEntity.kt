package com.kidslearning.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_scores")
data class GameScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val gameType: String, // "memory", "quiz", "coloring"
    val score: Int,
    val totalScore: Int,
    val completionTime: Long, // in milliseconds
    val playedDate: Long = System.currentTimeMillis()
)