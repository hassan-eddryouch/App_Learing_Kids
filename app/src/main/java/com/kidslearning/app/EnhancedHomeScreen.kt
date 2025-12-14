package com.kidslearning.app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EnhancedHomeScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val currentProfile = remember { mutableStateOf(ProfileItem("Emma", 6, "👧", 5, 150)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF8F9FF), Color(0xFFFFFFFF))
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Enhanced Header with Profile
            EnhancedHeader(currentProfile.value, navController)

            // Main Content
            Box(modifier = Modifier.weight(1f)) {
                Column {
                    // Quick Stats Bar
                    QuickStatsBar(currentProfile.value)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Main Menu Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(getEnhancedMenuItems()) { item ->
                            EnhancedMenuCard(item) {
                                navController.navigate(item.route)
                            }
                        }
                    }
                }
            }
        }

        // Floating Achievement Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingAchievementButton {
                navController.navigate("achievements")
            }
        }
    }
}

@Composable
fun EnhancedHeader(profile: ProfileItem, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Animated Avatar
                AnimatedAvatar(profile.emoji)

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        "Hello, ${profile.name}! 👋",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Level ${profile.level}",
                            fontSize = 14.sp,
                            color = Color(0xFF8B5CF6),
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        LinearProgressIndicator(
                            progress = 0.65f,
                            modifier = Modifier
                                .width(60.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Color(0xFF8B5CF6)
                        )
                    }
                }
            }

            // Menu Button
            IconButton(
                onClick = { navController.navigate("parent_dashboard") },
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6))
                        ),
                        CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun AnimatedAvatar(emoji: String) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(64.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFDDD6FE), Color(0xFFFAE8FF))
                )
            )
            .border(3.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = 32.sp)
    }
}

@Composable
fun QuickStatsBar(profile: ProfileItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickStatCard(
            icon = "⭐",
            value = "${profile.stars}",
            label = "Stars",
            color = Color(0xFFFBBF24),
            modifier = Modifier.weight(1f)
        )
        QuickStatCard(
            icon = "🪙",
            value = "150",
            label = "Coins",
            color = Color(0xFFF59E0B),
            modifier = Modifier.weight(1f)
        )
        QuickStatCard(
            icon = "🔥",
            value = "7",
            label = "Streak",
            color = Color(0xFFEF4444),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun QuickStatCard(
    icon: String,
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 24.sp)
            Text(
                value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                label,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun EnhancedMenuCard(item: EnhancedMenuItem, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    val infiniteTransition = rememberInfiniteTransition()
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing)
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Gradient Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = item.colors
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Icon with rotation animation
                    Text(
                        text = item.icon,
                        fontSize = 56.sp,
                        modifier = Modifier.rotate(if (item.animate) shimmer else 0f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    if (item.badge != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.3f)
                        ) {
                            Text(
                                item.badge,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
        }
    }
}

data class EnhancedMenuItem(
    val icon: String,
    val title: String,
    val colors: List<Color>,
    val route: String,
    val badge: String? = null,
    val animate: Boolean = false
)

fun getEnhancedMenuItems() = listOf(
    EnhancedMenuItem(
        "🔤",
        "Arabic\nAlphabet",
        listOf(Color(0xFF8B5CF6), Color(0xFFC084FC)),
        "arabic",
        "28 letters"
    ),
    EnhancedMenuItem(
        "🔡",
        "French\nAlphabet",
        listOf(Color(0xFF10B981), Color(0xFF34D399)),
        "french",
        "26 letters"
    ),
    EnhancedMenuItem(
        "🎮",
        "Memory\nGame",
        listOf(Color(0xFFFF6B9D), Color(0xFFFFB4D5)),
        "memory_game",
        "New!"
    ),
    EnhancedMenuItem(
        "❓",
        "Quiz\nTime",
        listOf(Color(0xFF3B82F6), Color(0xFF60A5FA)),
        "quiz"
    ),
    EnhancedMenuItem(
        "🎨",
        "Coloring\nBook",
        listOf(Color(0xFF9C27B0), Color(0xFFBA68C8)),
        "coloring"
    ),
    EnhancedMenuItem(
        "📖",
        "Story\nMode",
        listOf(Color(0xFFEC4899), Color(0xFFF472B6)),
        "story_mode",
        animate = true
    ),
    EnhancedMenuItem(
        "🏆",
        "Achievements",
        listOf(Color(0xFFFBBF24), Color(0xFFFCD34D)),
        "achievements",
        animate = true
    ),
    EnhancedMenuItem(
        "🎁",
        "Rewards\nStore",
        listOf(Color(0xFFF59E0B), Color(0xFFFBBF24)),
        "rewards_store"
    )
)

@Composable
fun FloatingAchievementButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    FloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFFFBBF24),
        modifier = Modifier
            .size(72.dp)
            .scale(scale)
    ) {
        Text("🏆", fontSize = 32.sp)
    }
}