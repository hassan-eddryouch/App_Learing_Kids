package com.kidslearning.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kidslearning.app.data.KidProfile
import com.kidslearning.app.data.KidsLearningDatabase
import com.kidslearning.app.data.KidsLearningRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KidsLearningRepository

    private val _profiles = MutableStateFlow<List<KidProfile>>(emptyList())
    val profiles: StateFlow<List<KidProfile>> = _profiles.asStateFlow()

    private val _currentProfile = MutableStateFlow<KidProfile?>(null)
    val currentProfile: StateFlow<KidProfile?> = _currentProfile.asStateFlow()

    init {
        val database = KidsLearningDatabase.getDatabase(application)
        repository = KidsLearningRepository(database)

        viewModelScope.launch {
            repository.getAllProfiles().collect { profileList ->
                _profiles.value = profileList
            }
        }
    }

    fun selectProfile(profile: KidProfile) {
        _currentProfile.value = profile
    }

    fun createProfile(name: String, age: Int, avatarEmoji: String) {
        viewModelScope.launch {
            val newProfile = KidProfile(
                name = name,
                age = age,
                avatarEmoji = avatarEmoji,
                level = 1,
                totalStars = 0,
                totalCoins = 0
            )
            val profileId = repository.createProfile(newProfile)

            // Initialize achievements for new profile
            repository.initializeAchievements(profileId.toInt())
        }
    }

    fun updateProfile(profile: KidProfile) {
        viewModelScope.launch {
            repository.updateProfile(profile)
        }
    }

    fun deleteProfile(profile: KidProfile) {
        viewModelScope.launch {
            repository.deleteProfile(profile)
        }
    }

    fun addStars(profileId: Int, stars: Int) {
        viewModelScope.launch {
            repository.addStars(profileId, stars)
        }
    }

    fun addCoins(profileId: Int, coins: Int) {
        viewModelScope.launch {
            repository.addCoins(profileId, coins)
        }
    }
}
