package com.kidslearning.app.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import com.kidslearning.app.data.KidsLearningDatabase
import com.kidslearning.app.data.KidsLearningRepository

class ColoringViewModel : ViewModel() {

    private val _selectedColor = MutableStateFlow(Color(0xFFFF6B9D))
    val selectedColor: StateFlow<Color> = _selectedColor.asStateFlow()

    private val _coloredSections = MutableStateFlow<Set<Int>>(emptySet())
    val coloredSections: StateFlow<Set<Int>> = _coloredSections.asStateFlow()

    private val _totalSections = MutableStateFlow(0)
    val totalSections: StateFlow<Int> = _totalSections.asStateFlow()

    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> = _isComplete.asStateFlow()

    private val _startTime = MutableStateFlow(System.currentTimeMillis())
    val startTime: StateFlow<Long> = _startTime.asStateFlow()

    fun setSelectedColor(color: Color) {
        _selectedColor.value = color
    }

    fun setTotalSections(total: Int) {
        _totalSections.value = total
    }

    fun colorSection(sectionId: Int) {
        val newSet = _coloredSections.value.toMutableSet()
        newSet.add(sectionId)
        _coloredSections.value = newSet

        checkCompletion()
    }

    private fun checkCompletion() {
        if (_coloredSections.value.size == _totalSections.value && _totalSections.value > 0) {
            _isComplete.value = true
        }
    }

    fun getCompletionPercentage(): Float {
        if (_totalSections.value == 0) return 0f
        return (_coloredSections.value.size.toFloat() / _totalSections.value.toFloat()) * 100f
    }

    fun getCompletionTime(): Long {
        return System.currentTimeMillis() - _startTime.value
    }

    fun reset() {
        _coloredSections.value = emptySet()
        _isComplete.value = false
        _startTime.value = System.currentTimeMillis()
        _selectedColor.value = Color(0xFFFF6B9D)
    }
}