package com.kidslearning.app.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SoundManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    private var textToSpeech: TextToSpeech? = null
    private var vibrator: Vibrator? = null
    
    private val soundMap = mutableMapOf<String, Int>()
    private var isTtsReady = false
    
    init {
        initializeSoundPool()
        initializeTextToSpeech()
        initializeVibrator()
    }
    
    private fun initializeSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
            
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()
    }
    
    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true
                textToSpeech?.language = Locale.getDefault()
            }
        }
    }
    
    private fun initializeVibrator() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
    
    fun playBackgroundMusic() {
        try {
            stopBackgroundMusic()
            mediaPlayer = MediaPlayer().apply {
                isLooping = true
                setVolume(0.3f, 0.3f)
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing background music", e)
        }
    }
    
    fun stopBackgroundMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }
    
    fun playSound(soundName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                soundMap[soundName]?.let { soundId ->
                    soundPool?.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            } catch (e: Exception) {
                Log.e("SoundManager", "Error playing sound: $soundName", e)
            }
        }
    }
    
    fun playSuccessSound() {
        playSound("success")
        vibrate(100)
    }
    
    fun playErrorSound() {
        playSound("error")
        vibrate(200)
    }
    
    fun playClickSound() {
        playSound("click")
        vibrate(50)
    }
    
    fun playLetterSound(letter: String, language: String = "en") {
        if (isTtsReady) {
            val locale = when (language) {
                "arabic" -> Locale("ar")
                "french" -> Locale.FRENCH
                else -> Locale.ENGLISH
            }
            
            textToSpeech?.let { tts ->
                tts.language = locale
                tts.speak(letter, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }
    
    fun speakText(text: String, language: String = "en") {
        if (isTtsReady) {
            val locale = when (language) {
                "arabic" -> Locale("ar")
                "french" -> Locale.FRENCH
                else -> Locale.ENGLISH
            }
            
            textToSpeech?.let { tts ->
                tts.language = locale
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }
    
    fun vibrate(duration: Long) {
        try {
            vibrator?.let { vib ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vib.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vib.vibrate(duration)
                }
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error vibrating", e)
        }
    }
    
    fun release() {
        stopBackgroundMusic()
        soundPool?.release()
        soundPool = null
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}