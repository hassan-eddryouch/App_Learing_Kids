package com.kidslearning.app.utils

object ValidationUtils {

    /**
     * Validate if letter is valid Arabic letter
     */
    fun isValidArabicLetter(letter: String): Boolean {
        val arabicLetters = listOf(
            "ا", "ب", "ت", "ث", "ج", "ح", "خ", "د", "ذ", "ر", "ز", "س",
            "ش", "ص", "ض", "ط", "ظ", "ع", "غ", "ف", "ق", "ك", "ل", "م",
            "ن", "ه", "و", "ي"
        )
        return arabicLetters.contains(letter)
    }

    /**
     * Validate if letter is valid French letter
     */
    fun isValidFrenchLetter(letter: String): Boolean {
        return letter.length == 1 && letter[0] in 'A'..'Z'
    }

    /**
     * Validate difficulty level
     */
    fun isValidDifficulty(difficulty: String): Boolean {
        return difficulty in listOf(
            Constants.DIFFICULTY_EASY,
            Constants.DIFFICULTY_MEDIUM,
            Constants.DIFFICULTY_HARD
        )
    }

    /**
     * Validate game type
     */
    fun isValidGameType(gameType: String): Boolean {
        return gameType in listOf(
            Constants.GAME_TYPE_MEMORY,
            Constants.GAME_TYPE_QUIZ,
            Constants.GAME_TYPE_COLORING
        )
    }

    /**
     * Validate language
     */
    fun isValidLanguage(language: String): Boolean {
        return language in listOf(
            Constants.LANGUAGE_ARABIC,
            Constants.LANGUAGE_FRENCH
        )
    }
}
