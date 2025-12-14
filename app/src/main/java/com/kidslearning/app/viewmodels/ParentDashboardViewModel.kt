package com.kidslearning.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kidslearning.app.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ParentDashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KidsLearningRepository

    private val _weeklyStats = MutableStateFlow<WeeklyStats?>(null)
    val weeklyStats: StateFlow<WeeklyStats?> = _weeklyStats.asStateFlow()

    private val _todayActivity = MutableStateFlow<DailyActivity?>(null)
    val todayActivity: StateFlow<DailyActivity?> = _todayActivity.asStateFlow()

    private val _streakCount = MutableStateFlow(0)
    val streakCount: StateFlow<Int> = _streakCount.asStateFlow()

    private val _reports = MutableStateFlow<List<ParentReport>>(emptyList())
    val reports: StateFlow<List<ParentReport>> = _reports.asStateFlow()

    init {
        val database = KidsLearningDatabase.getDatabase(application)
        repository = KidsLearningRepository(database)
    }

    fun loadDashboardData(profileId: Int) {
        viewModelScope.launch {
            // Load weekly stats
            loadWeeklyStats(profileId)

            // Load today's activity
            loadTodayActivity(profileId)

            // Load streak
            loadStreak(profileId)

            // Load reports
            repository.getReports(profileId).collect { reportList ->
                _reports.value = reportList
            }
        }
    }

    private suspend fun loadWeeklyStats(profileId: Int) {
        val stats = repository.getStatistics()
        _weeklyStats.value = WeeklyStats(
            timeSpent = 165, // minutes
            lettersLearned = stats.arabicLettersCompleted + stats.frenchLettersCompleted,
            gamesPlayed = stats.totalGamesPlayed,
            starsEarned = 95
        )
    }

    private suspend fun loadTodayActivity(profileId: Int) {
        val today = getTodayDate()
        val streak = repository.getStreakForDate(profileId, today)

        _todayActivity.value = DailyActivity(
            minutesSpent = streak?.minutesSpent ?: 0,
            activitiesCompleted = streak?.activitiesCompleted ?: 0,
            goalMinutes = 50
        )
    }

    private suspend fun loadStreak(profileId: Int) {
        // Calculate consecutive days
        var count = 0
        val calendar = java.util.Calendar.getInstance()

        for (i in 0 until 30) {
            val date = getDateString(calendar)
            val streak = repository.getStreakForDate(profileId, date)

            if (streak != null && streak.activitiesCompleted > 0) {
                count++
            } else if (i > 0) {
                break
            }

            calendar.add(java.util.Calendar.DAY_OF_YEAR, -1)
        }

        _streakCount.value = count
    }

    private fun getTodayDate(): String {
        val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        return format.format(java.util.Date())
    }

    private fun getDateString(calendar: java.util.Calendar): String {
        val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        return format.format(calendar.time)
    }

    fun generateWeeklyReport(profileId: Int) {
        viewModelScope.launch {
            repository.generateWeeklyReport(profileId)
        }
    }
}

data class WeeklyStats(
    val timeSpent: Int,
    val lettersLearned: Int,
    val gamesPlayed: Int,
    val starsEarned: Int
)

data class DailyActivity(
    val minutesSpent: Int,
    val activitiesCompleted: Int,
    val goalMinutes: Int
)