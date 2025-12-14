package com.kidslearning.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProgressEntity::class,
        GameScoreEntity::class,
        SettingsEntity::class,
        KidProfile::class,
        Achievement::class,
        DailyStreak::class,
        ParentReport::class,
        Reward::class,
        StoryChapter::class
    ],
    version = 2,
    exportSchema = false
)
abstract class KidsLearningDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao
    abstract fun gameScoreDao(): GameScoreDao
    abstract fun settingsDao(): SettingsDao
    abstract fun kidProfileDao(): KidProfileDao
    abstract fun achievementDao(): AchievementDao
    abstract fun dailyStreakDao(): DailyStreakDao
    abstract fun parentReportDao(): ParentReportDao
    abstract fun rewardDao(): RewardDao
    abstract fun storyChapterDao(): StoryChapterDao

    companion object {
        @Volatile
        private var INSTANCE: KidsLearningDatabase? = null

        fun getDatabase(context: Context): KidsLearningDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KidsLearningDatabase::class.java,
                    "kids_learning_database"
                )
                    .addMigrations(DatabaseMigrations.MIGRATION_1_2)
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(database: KidsLearningDatabase) {
            // Insert default rewards
            val rewardDao = database.rewardDao()

            val defaultRewards = listOf(
                Reward(name = "Superhero Avatar", emoji = "🦸", cost = 50, category = "avatar", description = "Unlock superhero avatar"),
                Reward(name = "Princess Avatar", emoji = "👸", cost = 50, category = "avatar", description = "Unlock princess avatar"),
                Reward(name = "Astronaut Avatar", emoji = "🚀", cost = 100, category = "avatar", description = "Unlock astronaut avatar"),
                Reward(name = "Rainbow Theme", emoji = "🌈", cost = 150, category = "theme", description = "Unlock rainbow theme"),
                Reward(name = "Ocean Theme", emoji = "🌊", cost = 150, category = "theme", description = "Unlock ocean theme"),
                Reward(name = "Gold Star Pack", emoji = "⭐", cost = 25, category = "sticker", description = "Get star stickers"),
                Reward(name = "Trophy Pack", emoji = "🏆", cost = 25, category = "sticker", description = "Get trophy stickers"),
                Reward(name = "Heart Pack", emoji = "💖", cost = 25, category = "sticker", description = "Get heart stickers")
            )

            defaultRewards.forEach { rewardDao.insertReward(it) }

            // Insert story chapters
            val storyDao = database.storyChapterDao()

            val arabicStories = listOf(
                StoryChapter(
                    chapterNumber = 1,
                    title = "مغامرة الحروف",
                    storyText = "في يوم جميل، قررت الحروف العربية أن تذهب في مغامرة رائعة عبر الصحراء...",
                    language = "arabic",
                    requiredLevel = 1,
                    emoji = "📖"
                ),
                StoryChapter(
                    chapterNumber = 2,
                    title = "حديقة الكلمات",
                    storyText = "وجدت الحروف حديقة سحرية مليئة بالكلمات الجميلة والأزهار الملونة...",
                    language = "arabic",
                    requiredLevel = 3,
                    emoji = "🌺"
                )
            )

            val frenchStories = listOf(
                StoryChapter(
                    chapterNumber = 1,
                    title = "L'Aventure des Lettres",
                    storyText = "Un beau jour, les lettres françaises ont décidé de partir à l'aventure dans la forêt magique...",
                    language = "french",
                    requiredLevel = 1,
                    emoji = "📚"
                ),
                StoryChapter(
                    chapterNumber = 2,
                    title = "Le Jardin des Mots",
                    storyText = "Les lettres ont trouvé un magnifique jardin rempli de beaux mots et de fleurs colorées...",
                    language = "french",
                    requiredLevel = 3,
                    emoji = "🌸"
                )
            )

            (arabicStories + frenchStories).forEach { storyDao.insertChapter(it) }
        }
    }
}