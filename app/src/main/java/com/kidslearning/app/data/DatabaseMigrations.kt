package com.kidslearning.app.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Database Migration from Version 1 to Version 2
 * Adds new tables for enhanced features
 */
object DatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create kid_profiles table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS kid_profiles (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    age INTEGER NOT NULL,
                    avatarEmoji TEXT NOT NULL,
                    level INTEGER NOT NULL,
                    totalStars INTEGER NOT NULL,
                    totalCoins INTEGER NOT NULL,
                    createdDate INTEGER NOT NULL,
                    lastActiveDate INTEGER NOT NULL,
                    parentPinCode TEXT NOT NULL
                )
            """.trimIndent())

            // Create achievements table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS achievements (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    kidProfileId INTEGER NOT NULL,
                    achievementType TEXT NOT NULL,
                    title TEXT NOT NULL,
                    description TEXT NOT NULL,
                    emoji TEXT NOT NULL,
                    stars INTEGER NOT NULL,
                    coins INTEGER NOT NULL,
                    unlockedDate INTEGER NOT NULL,
                    isUnlocked INTEGER NOT NULL,
                    FOREIGN KEY(kidProfileId) REFERENCES kid_profiles(id) ON DELETE CASCADE
                )
            """.trimIndent())

            // Create daily_streaks table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS daily_streaks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    kidProfileId INTEGER NOT NULL,
                    date TEXT NOT NULL,
                    activitiesCompleted INTEGER NOT NULL,
                    minutesSpent INTEGER NOT NULL,
                    starsEarned INTEGER NOT NULL,
                    FOREIGN KEY(kidProfileId) REFERENCES kid_profiles(id) ON DELETE CASCADE
                )
            """.trimIndent())

            // Create parent_reports table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS parent_reports (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    kidProfileId INTEGER NOT NULL,
                    weekStart TEXT NOT NULL,
                    totalTimeMinutes INTEGER NOT NULL,
                    lettersLearned INTEGER NOT NULL,
                    gamesPlayed INTEGER NOT NULL,
                    accuracy REAL NOT NULL,
                    improvementAreas TEXT NOT NULL,
                    strengths TEXT NOT NULL,
                    generatedDate INTEGER NOT NULL,
                    FOREIGN KEY(kidProfileId) REFERENCES kid_profiles(id) ON DELETE CASCADE
                )
            """.trimIndent())

            // Create rewards table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS rewards (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT NOT NULL,
                    emoji TEXT NOT NULL,
                    cost INTEGER NOT NULL,
                    category TEXT NOT NULL,
                    isUnlocked INTEGER NOT NULL
                )
            """.trimIndent())

            // Create story_chapters table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS story_chapters (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    chapterNumber INTEGER NOT NULL,
                    title TEXT NOT NULL,
                    storyText TEXT NOT NULL,
                    language TEXT NOT NULL,
                    requiredLevel INTEGER NOT NULL,
                    isCompleted INTEGER NOT NULL,
                    emoji TEXT NOT NULL
                )
            """.trimIndent())

            // Create indices for better performance
            database.execSQL("CREATE INDEX IF NOT EXISTS index_achievements_kidProfileId ON achievements(kidProfileId)")
            database.execSQL("CREATE INDEX IF NOT EXISTS index_daily_streaks_kidProfileId ON daily_streaks(kidProfileId)")
            database.execSQL("CREATE INDEX IF NOT EXISTS index_daily_streaks_date ON daily_streaks(date)")
            database.execSQL("CREATE INDEX IF NOT EXISTS index_parent_reports_kidProfileId ON parent_reports(kidProfileId)")
            database.execSQL("CREATE INDEX IF NOT EXISTS index_story_chapters_language ON story_chapters(language)")
        }
    }
}