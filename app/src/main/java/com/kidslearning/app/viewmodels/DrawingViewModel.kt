// ==================== DrawingViewModel.kt ====================
package com.kidslearning.app.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import com.kidslearning.app.data.KidsLearningDatabase
import com.kidslearning.app.data.KidsLearningRepository

class DrawingViewModel : ViewModel() {

    private val _currentColor = MutableStateFlow(Color(0xFF8B5CF6))
    val currentColor: StateFlow<Color> = _currentColor.asStateFlow()

    private val _brushSize = MutableStateFlow(20f)
    val brushSize: StateFlow<Float> = _brushSize.asStateFlow()

    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _canRedo = MutableStateFlow(false)
    val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()

    private val _isErasing = MutableStateFlow(false)
    val isErasing: StateFlow<Boolean> = _isErasing.asStateFlow()

    fun setColor(color: Color) {
        _currentColor.value = color
        _isErasing.value = false
    }

    fun setBrushSize(size: Float) {
        _brushSize.value = size.coerceIn(5f, 50f)
    }

    fun setCanUndo(canUndo: Boolean) {
        _canUndo.value = canUndo
    }

    fun setCanRedo(canRedo: Boolean) {
        _canRedo.value = canRedo
    }

    fun toggleEraser() {
        _isErasing.value = !_isErasing.value
        if (_isErasing.value) {
            _currentColor.value = Color.White
        }
    }

    fun reset() {
        _currentColor.value = Color(0xFF8B5CF6)
        _brushSize.value = 20f
        _canUndo.value = false
        _canRedo.value = false
        _isErasing.value = false
    }
}