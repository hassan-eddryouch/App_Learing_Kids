package com.kidslearning.app.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.random.Random

/**
 * Extension functions for common operations
 */

// Toast extensions
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

// Color extensions
fun Color.toHexString(): String {
    return String.format("#%08X", this.toArgb())
}

fun Color.lighten(factor: Float = 0.2f): Color {
    return Color(
        red = (red + (1f - red) * factor).coerceIn(0f, 1f),
        green = (green + (1f - green) * factor).coerceIn(0f, 1f),
        blue = (blue + (1f - blue) * factor).coerceIn(0f, 1f),
        alpha = alpha
    )
}

fun Color.darken(factor: Float = 0.2f): Color {
    return Color(
        red = (red * (1f - factor)).coerceIn(0f, 1f),
        green = (green * (1f - factor)).coerceIn(0f, 1f),
        blue = (blue * (1f - factor)).coerceIn(0f, 1f),
        alpha = alpha
    )
}

// Random color generator
fun randomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f
    )
}

fun randomBrightColor(): Color {
    val colors = listOf(
        Color(0xFFFF6B9D),
        Color(0xFF8B5CF6),
        Color(0xFF3B82F6),
        Color(0xFF10B981),
        Color(0xFFFBBF24),
        Color(0xFFF97316),
        Color(0xFFEF4444),
        Color(0xFFA855F7)
    )
    return colors.random()
}

// String extensions
fun String.capitalizeFirstLetter(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

fun String.isArabicLetter(): Boolean {
    val arabicRange = '\u0600'..'\u06FF'
    return this.length == 1 && this[0] in arabicRange
}

fun String.isFrenchLetter(): Boolean {
    return this.length == 1 && this[0] in 'A'..'Z'
}

// List extensions
fun <T> List<T>.shuffleImmutable(): List<T> {
    return this.toMutableList().apply { shuffle() }
}

// Number extensions
fun Int.toTimeString(): String {
    val minutes = this / 60
    val seconds = this % 60
    return if (minutes > 0) {
        String.format("%d:%02d", minutes, seconds)
    } else {
        String.format("%d sec", seconds)
    }
}

fun Float.toPercentageString(): String {
    return String.format("%.1f%%", this)
}