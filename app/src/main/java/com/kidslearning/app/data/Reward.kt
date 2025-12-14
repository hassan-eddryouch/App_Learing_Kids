package com.kidslearning.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val emoji: String,
    val cost: Int, // coins needed
    val category: String, // "avatar", "theme", "sticker"
    val isUnlocked: Boolean = false
)