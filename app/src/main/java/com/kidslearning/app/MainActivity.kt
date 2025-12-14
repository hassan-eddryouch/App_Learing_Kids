package com.kidslearning.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KidsLearningTheme {
                val navController = rememberNavController()
                EnhancedNavigationGraph(navController)
            }
        }
    }
}

@Composable
fun KidsLearningTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF8B5CF6),
            secondary = Color(0xFF3B82F6),
            background = Color(0xFFF3F4F6)
        ),
        content = content
    )
}

@Composable
fun EnhancedNavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        // Splash Screen
        composable("splash") {
            EnhancedSplashScreen(navController)
        }

        // Profile Selection
        composable("profile_selection") {
            ProfileSelectionScreen(navController)
        }

        // Enhanced Home
        composable("home") {
            EnhancedHomeScreen(navController)
        }

        // Original Screens
        composable("arabic") {
            ArabicAlphabetScreen(navController)
        }
        composable("french") {
            FrenchAlphabetScreen(navController)
        }
        composable("draw/{letter}") { backStackEntry ->
            DrawLetterScreen(
                letter = backStackEntry.arguments?.getString("letter") ?: "A",
                navController = navController
            )
        }
        composable("memory_game") {
            MemoryGameScreen(navController)
        }
        composable("quiz") {
            QuizGameScreen(navController)
        }
        composable("coloring") {
            ColoringScreen(navController)
        }

        // New Enhanced Screens
        composable("achievements") {
            AchievementsScreen(navController)
        }
        composable("rewards_store") {
            RewardsStoreScreen(navController)
        }
        composable("story_mode") {
            StoryModeScreen(navController)
        }
        composable(
            "story_reader/{storyId}",
            arguments = listOf(navArgument("storyId") { type = NavType.IntType })
        ) { backStackEntry ->
            StoryReaderScreen(
                storyId = backStackEntry.arguments?.getInt("storyId") ?: 1,
                navController = navController
            )
        }
        composable("parent_dashboard") {
            ParentDashboardScreen(navController)
        }
    }
}

@Composable
fun EnhancedSplashScreen(navController: NavHostController) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Logo animation
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        // Fade in text
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(800)
        )

        // Wait and navigate
        kotlinx.coroutines.delay(2500)
        navController.navigate("profile_selection") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF667EEA),
                        Color(0xFF764BA2),
                        Color(0xFFF093FB)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            // Animated Logo
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .then(Modifier.scale(scale.value)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🎓",
                        fontSize = 120.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Title with fade
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(alpha.value)
            ) {
                Text(
                    text = "Kids Learning",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Learn • Play • Grow",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Loading indicator
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier
                    .size(40.dp)
                    .alpha(alpha.value)
            )
        }

        // Version at bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "Version 2.0",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}