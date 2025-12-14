package com.kidslearning.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryChapterDao {
    @Query("SELECT * FROM story_chapters ORDER BY chapterNumber ASC")
    fun getAllChapters(): Flow<List<StoryChapter>>

    @Query("SELECT * FROM story_chapters WHERE language = :language ORDER BY chapterNumber ASC")
    fun getChaptersByLanguage(language: String): Flow<List<StoryChapter>>

    @Insert
    suspend fun insertChapter(chapter: StoryChapter)

    @Query("UPDATE story_chapters SET isCompleted = 1 WHERE id = :chapterId")
    suspend fun completeChapter(chapterId: Int)
}