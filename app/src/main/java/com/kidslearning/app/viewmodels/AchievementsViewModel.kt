package com.kidslearning.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kidslearning.app.data.Achievement
import com.kidslearning.app.data.KidsLearningDatabase
import com.kidslearning.app.data.KidsLearningRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AchievementsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KidsLearningRepository

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _unlockedCount = MutableStateFlow(0)
    val unlockedCount: StateFlow<Int> = _unlockedCount.asStateFlow()

    init {
        val database = KidsLearningDatabase.getDatabase(application)
        repository = KidsLearningRepository(database)
    }

    fun loadAchievements(profileId: Int) {
        viewModelScope.launch {
            repository.getAchievements(profileId).collect { achievementList ->
                _achievements.value = achievementList
                _unlockedCount.value = achievementList.count { it.isUnlocked }
            }
        }
    }

    fun unlockAchievement(achievementId: Int) {
        viewModelScope.launch {
            repository.unlockAchievement(achievementId)
        }
    }

    fun checkAndUnlockAchievements(profileId: Int, achievementType: String) {
        viewModelScope.launch {
            val achievements = _achievements.value
            val achievement = achievements.find {
                it.achievementType == achievementType && !it.isUnlocked
            }

            achievement?.let {
                repository.unlockAchievement(it.id)
                // Award stars and coins
                repository.addStars(profileId, it.stars)
                repository.addCoins(profileId, it.coins)
            }
        }
    }
}