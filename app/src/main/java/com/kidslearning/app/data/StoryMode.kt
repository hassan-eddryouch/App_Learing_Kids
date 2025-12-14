package com.kidslearning.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story_chapters")
data class StoryChapter(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val chapterNumber: Int,
    val title: String,
    val storyText: String,
    val language: String,
    val requiredLevel: Int,
    val isCompleted: Boolean = false,
    val emoji: String
)