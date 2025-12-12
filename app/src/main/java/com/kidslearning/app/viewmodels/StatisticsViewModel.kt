package com.kidslearning.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kidslearning.app.data.AppStatistics
import com.kidslearning.app.data.KidsLearningDatabase
import com.kidslearning.app.data.KidsLearningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KidsLearningRepository

    private val _statistics = MutableStateFlow<AppStatistics?>(null)
    val statistics: StateFlow<AppStatistics?> = _statistics.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        val database = KidsLearningDatabase.getDatabase(application)
        repository = KidsLearningRepository(database)

        loadStatistics()
    }

    fun loadStatistics() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val stats = repository.getStatistics()
                _statistics.value = stats
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshStatistics() {
        loadStatistics()
    }
}