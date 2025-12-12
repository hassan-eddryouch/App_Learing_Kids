package com.kidslearning.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.kidslearning.app.data.KidsLearningDatabase
import com.kidslearning.app.data.KidsLearningRepository

class MemoryGameViewModel : ViewModel() {

    private val _moves = MutableStateFlow(0)
    val moves: StateFlow<Int> = _moves.asStateFlow()

    private val _matchedPairs = MutableStateFlow(0)
    val matchedPairs: StateFlow<Int> = _matchedPairs.asStateFlow()

    private val _gameStartTime = MutableStateFlow(System.currentTimeMillis())
    val gameStartTime: StateFlow<Long> = _gameStartTime.asStateFlow()

    private val _isGameComplete = MutableStateFlow(false)
    val isGameComplete: StateFlow<Boolean> = _isGameComplete.asStateFlow()

    private val _bestMoves = MutableStateFlow<Int?>(null)
    val bestMoves: StateFlow<Int?> = _bestMoves.asStateFlow()

    private val _currentLevel = MutableStateFlow(1)
    val currentLevel: StateFlow<Int> = _currentLevel.asStateFlow()

    fun incrementMoves() {
        _moves.value++
    }

    fun incrementMatchedPairs() {
        _matchedPairs.value++
    }

    fun setGameComplete(complete: Boolean) {
        _isGameComplete.value = complete
        if (complete) {
            updateBestMoves()
        }
    }

    private fun updateBestMoves() {
        val currentMoves = _moves.value
        val best = _bestMoves.value
        if (best == null || currentMoves < best) {
            _bestMoves.value = currentMoves
        }
    }

    fun resetGame() {
        _moves.value = 0
        _matchedPairs.value = 0
        _gameStartTime.value = System.currentTimeMillis()
        _isGameComplete.value = false
    }

    fun nextLevel() {
        _currentLevel.value++
        resetGame()
    }

    fun getCompletionTime(): Long {
        return System.currentTimeMillis() - _gameStartTime.value
    }

    fun getCompletionTimeInSeconds(): Int {
        return (getCompletionTime() / 1000).toInt()
    }
}