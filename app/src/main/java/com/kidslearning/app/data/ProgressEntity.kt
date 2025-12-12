package com.kidslearning.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress")
data class ProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val letter: String,
    val language: String, // "arabic" or "french"
    val attempts: Int = 0,
    val completed: Boolean = false,
    val lastAttemptDate: Long = System.currentTimeMillis()
)