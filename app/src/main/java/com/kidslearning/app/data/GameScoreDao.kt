package com.kidslearning.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GameScoreDao {
    @Query("SELECT * FROM game_scores WHERE gameType = :gameType ORDER BY playedDate DESC LIMIT 10")
    fun getScoresByGameType(gameType: String): Flow<List<GameScoreEntity>>

    @Query("SELECT * FROM game_scores ORDER BY playedDate DESC LIMIT 20")
    fun getAllScores(): Flow<List<GameScoreEntity>>

    @Query("SELECT * FROM game_scores WHERE gameType = :gameType ORDER BY score DESC LIMIT 1")
    suspend fun getBestScore(gameType: String): GameScoreEntity?

    @Insert
    suspend fun insertScore(score: GameScoreEntity)

    @Insert
    suspend fun insertScores(scores: List<GameScoreEntity>)

    @Query("SELECT MAX(score) FROM game_scores WHERE gameType = :gameType")
    suspend fun getHighScore(gameType: String): Int?

    @Query("SELECT AVG(score) FROM game_scores WHERE gameType = :gameType")
    suspend fun getAverageScore(gameType: String): Float?

    @Query("DELETE FROM game_scores WHERE gameType = :gameType")
    suspend fun deleteScoresByGameType(gameType: String)

    @Query("DELETE FROM game_scores")
    suspend fun deleteAllScores()

    @Query("SELECT COUNT(*) FROM game_scores WHERE gameType = :gameType")
    suspend fun getGamesPlayedCount(gameType: String): Int
}