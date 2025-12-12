package com.kidslearning.app.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository class that acts as a single source of truth for data operations
 */
class KidsLearningRepository(private val database: KidsLearningDatabase) {

    private val progressDao = database.progressDao()
    private val gameScoreDao = database.gameScoreDao()
    private val settingsDao = database.settingsDao()

    // ==================== PROGRESS OPERATIONS ====================

    /**
     * Get all progress for a specific language
     */
    fun getProgressByLanguage(language: String): Flow<List<ProgressEntity>> {
        return progressDao.getProgressByLanguage(language)
    }

    /**
     * Get progress for a specific letter
     */
    suspend fun getLetterProgress(letter: String, language: String): ProgressEntity? {
        return progressDao.getProgress(letter, language)
    }

    /**
     * Save or update progress for a letter
     */
    suspend fun saveProgress(letter: String, language: String, completed: Boolean) {
        val existing = progressDao.getProgress(letter, language)
        if (existing != null) {
            progressDao.updateProgress(
                existing.copy(
                    attempts = existing.attempts + 1,
                    completed = completed || existing.completed,
                    lastAttemptDate = System.currentTimeMillis()
                )
            )
        } else {
            progressDao.insertProgress(
                ProgressEntity(
                    letter = letter,
                    language = language,
                    attempts = 1,
                    completed = completed,
                    lastAttemptDate = System.currentTimeMillis()
                )
            )
        }
    }

    /**
     * Get count of completed letters for a language
     */
    fun getCompletedCount(language: String): Flow<Int> {
        return progressDao.getCompletedCount(language)
    }

    /**
     * Get recent progress entries
     */
    fun getRecentProgress(): Flow<List<ProgressEntity>> {
        return progressDao.getRecentProgress()
    }

    /**
     * Get total number of letters for a language
     */
    suspend fun getTotalLetterCount(language: String): Int {
        return progressDao.getTotalLetterCount(language)
    }

    /**
     * Delete all progress
     */
    suspend fun deleteAllProgress() {
        progressDao.deleteAllProgress()
    }

    // ==================== GAME SCORE OPERATIONS ====================

    /**
     * Get scores for a specific game type
     */
    fun getScoresByGameType(gameType: String): Flow<List<GameScoreEntity>> {
        return gameScoreDao.getScoresByGameType(gameType)
    }

    /**
     * Get all game scores
     */
    fun getAllScores(): Flow<List<GameScoreEntity>> {
        return gameScoreDao.getAllScores()
    }

    /**
     * Get the best score for a game type
     */
    suspend fun getBestScore(gameType: String): GameScoreEntity? {
        return gameScoreDao.getBestScore(gameType)
    }

    /**
     * Save a game score
     */
    suspend fun saveGameScore(
        gameType: String,
        score: Int,
        totalScore: Int,
        completionTime: Long
    ) {
        gameScoreDao.insertScore(
            GameScoreEntity(
                gameType = gameType,
                score = score,
                totalScore = totalScore,
                completionTime = completionTime,
                playedDate = System.currentTimeMillis()
            )
        )
    }

    /**
     * Save multiple game scores at once
     */
    suspend fun saveGameScores(scores: List<GameScoreEntity>) {
        gameScoreDao.insertScores(scores)
    }

    /**
     * Get the highest score for a game type
     */
    suspend fun getHighScore(gameType: String): Int {
        return gameScoreDao.getHighScore(gameType) ?: 0
    }

    /**
     * Get average score for a game type
     */
    suspend fun getAverageScore(gameType: String): Float {
        return gameScoreDao.getAverageScore(gameType) ?: 0f
    }

    /**
     * Get count of games played for a specific type
     */
    suspend fun getGamesPlayedCount(gameType: String): Int {
        return gameScoreDao.getGamesPlayedCount(gameType)
    }

    /**
     * Delete all scores for a game type
     */
    suspend fun deleteScoresByGameType(gameType: String) {
        gameScoreDao.deleteScoresByGameType(gameType)
    }

    /**
     * Delete all game scores
     */
    suspend fun deleteAllScores() {
        gameScoreDao.deleteAllScores()
    }

    // ==================== SETTINGS OPERATIONS ====================

    /**
     * Get settings as Flow
     */
    fun getSettings(): Flow<SettingsEntity?> {
        return settingsDao.getSettings()
    }

    /**
     * Get settings once (not reactive)
     */
    suspend fun getSettingsOnce(): SettingsEntity? {
        return settingsDao.getSettingsOnce()
    }

    /**
     * Update settings
     */
    suspend fun updateSettings(settings: SettingsEntity) {
        settingsDao.updateSettings(settings)
    }

    /**
     * Initialize default settings if not exists
     */
    suspend fun initializeSettings() {
        val existing = settingsDao.getSettingsOnce()
        if (existing == null) {
            settingsDao.insertSettings(SettingsEntity())
        }
    }

    /**
     * Toggle sound setting
     */
    suspend fun toggleSound(enabled: Boolean) {
        settingsDao.updateSoundEnabled(enabled)
    }

    /**
     * Toggle music setting
     */
    suspend fun toggleMusic(enabled: Boolean) {
        settingsDao.updateMusicEnabled(enabled)
    }

    /**
     * Toggle vibration setting
     */
    suspend fun toggleVibration(enabled: Boolean) {
        settingsDao.updateVibrationEnabled(enabled)
    }

    /**
     * Update difficulty level
     */
    suspend fun updateDifficulty(difficulty: String) {
        settingsDao.updateDifficulty(difficulty)
    }

    // ==================== STATISTICS OPERATIONS ====================

    /**
     * Get comprehensive statistics
     */
    suspend fun getStatistics(): AppStatistics {
        val arabicCompleted = progressDao.getTotalLetterCount("arabic")
        val frenchCompleted = progressDao.getTotalLetterCount("french")

        val memoryGamesPlayed = gameScoreDao.getGamesPlayedCount("memory")
        val quizGamesPlayed = gameScoreDao.getGamesPlayedCount("quiz")
        val coloringGamesPlayed = gameScoreDao.getGamesPlayedCount("coloring")

        val memoryHighScore = gameScoreDao.getHighScore("memory") ?: 0
        val quizHighScore = gameScoreDao.getHighScore("quiz") ?: 0

        return AppStatistics(
            arabicLettersCompleted = arabicCompleted,
            frenchLettersCompleted = frenchCompleted,
            totalGamesPlayed = memoryGamesPlayed + quizGamesPlayed + coloringGamesPlayed,
            memoryGameHighScore = memoryHighScore,
            quizGameHighScore = quizHighScore
        )
    }
}

/**
 * Data class for app statistics
 */
data class AppStatistics(
    val arabicLettersCompleted: Int,
    val frenchLettersCompleted: Int,
    val totalGamesPlayed: Int,
    val memoryGameHighScore: Int,
    val quizGameHighScore: Int
)