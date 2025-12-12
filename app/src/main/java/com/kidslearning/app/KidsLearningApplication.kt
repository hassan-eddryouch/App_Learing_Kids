//package com.kidslearning.app

//import android.app.Application
//import com.kidslearning.app.data.KidsLearningDatabase
//import com.kidslearning.app.data.KidsLearningRepository
//import com.kidslearning.app.utils.Logger
//import com.kidslearning.app.utils.PreferencesManager
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.launch
//
//class KidsLearningApplication : Application() {
//
//    // Application scope for coroutines
//    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
//
//    // Lazy initialization of database
//    val database by lazy { KidsLearningDatabase.getDatabase(this) }
//
//    // Lazy initialization of repository
//    val repository by lazy { KidsLearningRepository(database) }
//
//    // Preferences manager
//    val preferencesManager by lazy { PreferencesManager(this) }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        // Set debug mode based on build type
//        Logger.setDebugMode(BuildConfig.DEBUG)
//
//        Logger.i("Application started")
//
//        // Initialize settings on first launch
//        applicationScope.launch {
//            try {
//                repository.initializeSettings()
//                Logger.d("Settings initialized")
//            } catch (e: Exception) {
//                Logger.e("Error initializing settings", e)
//            }
//        }
//
//        // Pre-load alphabet data
//        applicationScope.launch {
//            try {
//                preloadAlphabetData()
//                Logger.d("Alphabet data preloaded")
//            } catch (e: Exception) {
//                Logger.e("Error preloading alphabet data", e)
//            }
//        }
//    }
//
//    /**
//     * Preload alphabet data to ensure quick access
//     */
//    private suspend fun preloadAlphabetData() {
//        // This ensures the data classes are loaded into memory
//        // You can add additional initialization logic here
//    }
//
//    override fun onTerminate() {
//        super.onTerminate()
//        Logger.i("Application terminated")
//    }
//}

package com.kidslearning.app

import android.app.Application
import com.kidslearning.app.data.KidsLearningDatabase
import com.kidslearning.app.data.KidsLearningRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class KidsLearningApplication : Application() {

    // Application scope for coroutines
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Lazy initialization of database
    val database by lazy { KidsLearningDatabase.getDatabase(this) }

    // Lazy initialization of repository
    val repository by lazy { KidsLearningRepository(database) }

    override fun onCreate() {
        super.onCreate()

        // Initialize settings on first launch
        applicationScope.launch {
            try {
                repository.initializeSettings()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}