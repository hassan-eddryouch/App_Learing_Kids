package com.kidslearning.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kid_profiles")
data class KidProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val age: Int,
    val avatarEmoji: String = "👦",
    val level: Int = 1,
    val totalStars: Int = 0,
    val totalCoins: Int = 0,
    val createdDate: Long = System.currentTimeMillis(),
    val lastActiveDate: Long = System.currentTimeMillis(),
    val parentPinCode: String = ""
)