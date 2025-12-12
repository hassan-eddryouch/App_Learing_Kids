package com.kidslearning.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kidslearning.app.data.GameScoreEntity
import com.kidslearning.app.data.KidsLearningDatabase
import com.kidslearning.app.data.KidsLearningRepository
import com.kidslearning.app.data.ProgressEntity
import com.kidslearning.app.data.SettingsEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
/**
 * Main ViewModel for the Kids Learning App
 * Manages progress, settings, and overall app state
 */
class KidsLearningViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KidsLearningRepository

    // UI State flows
    private val _arabicProgress = MutableStateFlow<List<ProgressEntity>>(emptyList())
    val arabicProgress: StateFlow<List<ProgressEntity>> = _arabicProgress.asStateFlow()

    private val _frenchProgress = MutableStateFlow<List<ProgressEntity>>(emptyList())
    val frenchProgress: StateFlow<List<ProgressEntity>> = _frenchProgress.asStateFlow()

    private val _gameScores = MutableStateFlow<List<GameScoreEntity>>(emptyList())
    val gameScores: StateFlow<List<GameScoreEntity>> = _gameScores.asStateFlow()

    private val _settings = MutableStateFlow(SettingsEntity())
    val settings: StateFlow<SettingsEntity> = _settings.asStateFlow()

    private val _arabicCompletedCount = MutableStateFlow(0)
    val arabicCompletedCount: StateFlow<Int> = _arabicCompletedCount.asStateFlow()

    private val _frenchCompletedCount = MutableStateFlow(0)
    val frenchCompletedCount: StateFlow<Int> = _frenchCompletedCount.asStateFlow()

    init {
        val database = KidsLearningDatabase.getDatabase(application)
        repository = KidsLearningRepository(database)

        // Initialize settings
        viewModelScope.launch {
            repository.initializeSettings()
        }

        // Collect flows
        viewModelScope.launch {
            repository.getProgressByLanguage("arabic").collect { progress ->
                _arabicProgress.value = progress
            }
        }

        viewModelScope.launch {
            repository.getProgressByLanguage("french").collect { progress ->
                _frenchProgress.value = progress
            }
        }

        viewModelScope.launch {
            repository.getCompletedCount("arabic").collect { count ->
                _arabicCompletedCount.value = count
            }
        }

        viewModelScope.launch {
            repository.getCompletedCount("french").collect { count ->
                _frenchCompletedCount.value = count
            }
        }

        viewModelScope.launch {
            repository.getSettings().collect { settings ->
                _settings.value = settings ?: SettingsEntity()
            }
        }
    }

    // Progress operations
    fun saveLetterProgress(letter: String, language: String, completed: Boolean) {
        viewModelScope.launch {
            repository.saveProgress(letter, language, completed)
        }
    }

    // Game score operations
    fun saveGameScore(gameType: String, score: Int, totalScore: Int, completionTime: Long) {
        viewModelScope.launch {
            repository.saveGameScore(gameType, score, totalScore, completionTime)
        }
    }

    fun loadGameScores(gameType: String) {
        viewModelScope.launch {
            repository.getScoresByGameType(gameType).collect { scores ->
                _gameScores.value = scores
            }
        }
    }

    suspend fun getHighScore(gameType: String): Int {
        return repository.getHighScore(gameType)
    }

    // Settings operations
    fun updateSettings(settings: SettingsEntity) {
        viewModelScope.launch {
            repository.updateSettings(settings)
        }
    }

    fun toggleSound() {
        val currentSettings = _settings.value
        updateSettings(currentSettings.copy(soundEnabled = !currentSettings.soundEnabled))
    }

    fun toggleMusic() {
        val currentSettings = _settings.value
        updateSettings(currentSettings.copy(musicEnabled = !currentSettings.musicEnabled))
    }

    fun toggleVibration() {
        val currentSettings = _settings.value
        updateSettings(currentSettings.copy(vibrationEnabled = !currentSettings.vibrationEnabled))
    }

    fun setDifficulty(difficulty: String) {
        val currentSettings = _settings.value
        updateSettings(currentSettings.copy(difficulty = difficulty))
    }
}

/**
 * ViewModelFactory for creating ViewModels with Application context
 */
class KidsLearningViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KidsLearningViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KidsLearningViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}