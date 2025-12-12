package com.kidslearning.app.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.kidslearning.app.data.KidsLearningDatabase
import com.kidslearning.app.data.KidsLearningRepository

class QuizViewModel : ViewModel() {

    private val _currentQuestion = MutableStateFlow(0)
    val currentQuestion: StateFlow<Int> = _currentQuestion.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _totalQuestions = MutableStateFlow(0)
    val totalQuestions: StateFlow<Int> = _totalQuestions.asStateFlow()

    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer: StateFlow<String?> = _selectedAnswer.asStateFlow()

    private val _showResult = MutableStateFlow(false)
    val showResult: StateFlow<Boolean> = _showResult.asStateFlow()

    private val _correctAnswers = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswers.asStateFlow()

    private val _wrongAnswers = MutableStateFlow(0)
    val wrongAnswers: StateFlow<Int> = _wrongAnswers.asStateFlow()

    private val _quizStartTime = MutableStateFlow(System.currentTimeMillis())
    val quizStartTime: StateFlow<Long> = _quizStartTime.asStateFlow()

    fun setTotalQuestions(total: Int) {
        _totalQuestions.value = total
    }

    fun selectAnswer(answer: String, isCorrect: Boolean) {
        _selectedAnswer.value = answer
        if (isCorrect) {
            _correctAnswers.value++
            _score.value++
        } else {
            _wrongAnswers.value++
        }
    }

    fun incrementScore() {
        _score.value++
    }

    fun nextQuestion() {
        _currentQuestion.value++
        _selectedAnswer.value = null
        _showResult.value = false
    }

    fun showResult() {
        _showResult.value = true
    }

    fun resetQuiz() {
        _currentQuestion.value = 0
        _score.value = 0
        _selectedAnswer.value = null
        _showResult.value = false
        _correctAnswers.value = 0
        _wrongAnswers.value = 0
        _quizStartTime.value = System.currentTimeMillis()
    }

    fun isQuizComplete(): Boolean {
        return _currentQuestion.value >= _totalQuestions.value
    }

    fun getQuizCompletionTime(): Long {
        return System.currentTimeMillis() - _quizStartTime.value
    }

    fun getAccuracyPercentage(): Float {
        val total = _correctAnswers.value + _wrongAnswers.value
        if (total == 0) return 0f
        return (_correctAnswers.value.toFloat() / total.toFloat()) * 100f
    }
}