package com.kidslearning.app.utils

object Constants {
    // Game Types
    const val GAME_TYPE_MEMORY = "memory"
    const val GAME_TYPE_QUIZ = "quiz"
    const val GAME_TYPE_COLORING = "coloring"

    // Languages
    const val LANGUAGE_ARABIC = "arabic"
    const val LANGUAGE_FRENCH = "french"

    // Difficulty Levels
    const val DIFFICULTY_EASY = "easy"
    const val DIFFICULTY_MEDIUM = "medium"
    const val DIFFICULTY_HARD = "hard"

    // Animation Durations
    const val ANIMATION_DURATION_SHORT = 300
    const val ANIMATION_DURATION_MEDIUM = 500
    const val ANIMATION_DURATION_LONG = 1000

    // Audio
    const val VOLUME_LEVEL_DEFAULT = 0.7f
    const val VIBRATION_DURATION_SHORT = 50L
    const val VIBRATION_DURATION_LONG = 100L

    // UI
    const val GRID_COLUMNS_ALPHABET = 4
    const val GRID_COLUMNS_GAMES = 2
    const val CARD_ELEVATION = 8
    const val CORNER_RADIUS = 16

    // Progress
    const val TOTAL_ARABIC_LETTERS = 28
    const val TOTAL_FRENCH_LETTERS = 26

    // Game Configuration
    const val MEMORY_GAME_PAIRS = 8
    const val QUIZ_QUESTIONS_COUNT = 5
    const val COLORING_SECTIONS = 8

    // Rewards
    const val STARS_FOR_PERFECT = 3
    const val STARS_FOR_GOOD = 2
    const val STARS_FOR_COMPLETE = 1

    // Database
    const val DATABASE_NAME = "kids_learning_database"
    const val DATABASE_VERSION = 1
}
