package com.kidslearning.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kidslearning.app.data.StoryChapter
import com.kidslearning.app.data.KidsLearningDatabase
import com.kidslearning.app.data.KidsLearningRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KidsLearningRepository

    private val _stories = MutableStateFlow<List<StoryChapter>>(emptyList())
    val stories: StateFlow<List<StoryChapter>> = _stories.asStateFlow()

    private val _currentStory = MutableStateFlow<StoryChapter?>(null)
    val currentStory: StateFlow<StoryChapter?> = _currentStory.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    init {
        val database = KidsLearningDatabase.getDatabase(application)
        repository = KidsLearningRepository(database)

        viewModelScope.launch {
            repository.getAllChapters().collect { storyList ->
                _stories.value = storyList
            }
        }
    }

    fun loadStory(storyId: Int) {
        val story = _stories.value.find { it.id == storyId }
        _currentStory.value = story
        _currentPage.value = 0
    }

    fun nextPage() {
        _currentPage.value++
    }

    fun previousPage() {
        if (_currentPage.value > 0) {
            _currentPage.value--
        }
    }

    fun completeStory(storyId: Int, profileId: Int) {
        viewModelScope.launch {
            repository.completeChapter(storyId)
            // Award stars and coins
            repository.addStars(profileId, 10)
            repository.addCoins(profileId, 5)
        }
    }

    fun getStoriesByLanguage(language: String) {
        viewModelScope.launch {
            repository.getChaptersByLanguage(language).collect { storyList ->
                _stories.value = storyList
            }
        }
    }
}