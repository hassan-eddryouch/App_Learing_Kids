package com.kidslearning.app.utils

import java.util.concurrent.TimeUnit

object FormatUtils {

    /**
     * Format time in milliseconds to readable string
     */
    fun formatTime(millis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(minutes)

        return if (minutes > 0) {
            String.format("%d:%02d", minutes, seconds)
        } else {
            String.format("%d sec", seconds)
        }
    }

    /**
     * Format percentage
     */
    fun formatPercentage(value: Float): String {
        return String.format("%.1f%%", value)
    }

    /**
     * Format score
     */
    fun formatScore(score: Int, total: Int): String {
        return "$score/$total"
    }

    /**
     * Get performance message based on score
     */
    fun getPerformanceMessage(score: Int, total: Int): String {
        val percentage = (score.toFloat() / total.toFloat()) * 100

        return when {
            percentage >= 90 -> "🌟 Excellent! You're a star!"
            percentage >= 75 -> "🎉 Great job! Keep it up!"
            percentage >= 60 -> "😊 Good work! Practice more!"
            percentage >= 40 -> "💪 Nice try! You can do better!"
            else -> "🌈 Keep practicing! You'll get there!"
        }
    }

    /**
     * Get difficulty emoji
     */
    fun getDifficultyEmoji(difficulty: String): String {
        return when (difficulty) {
            Constants.DIFFICULTY_EASY -> "😊"
            Constants.DIFFICULTY_MEDIUM -> "🤔"
            Constants.DIFFICULTY_HARD -> "🔥"
            else -> "😊"
        }
    }

    /**
     * Get star rating based on performance
     */
    fun getStarRating(score: Int, total: Int): Int {
        val percentage = (score.toFloat() / total.toFloat()) * 100

        return when {
            percentage >= 90 -> Constants.STARS_FOR_PERFECT
            percentage >= 70 -> Constants.STARS_FOR_GOOD
            percentage >= 50 -> Constants.STARS_FOR_COMPLETE
            else -> 0
        }
    }
}