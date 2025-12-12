package com.kidslearning.app.utils

import android.util.Log

object Logger {
    private const val TAG = "KidsLearning"
    private var isDebugMode = true // Set to false for release

    fun d(message: String) {
        if (isDebugMode) {
            Log.d(TAG, message)
        }
    }

    fun e(message: String, throwable: Throwable? = null) {
        if (isDebugMode) {
            if (throwable != null) {
                Log.e(TAG, message, throwable)
            } else {
                Log.e(TAG, message)
            }
        }
    }

    fun i(message: String) {
        if (isDebugMode) {
            Log.i(TAG, message)
        }
    }

    fun w(message: String) {
        if (isDebugMode) {
            Log.w(TAG, message)
        }
    }

    fun setDebugMode(enabled: Boolean) {
        isDebugMode = enabled
    }
}