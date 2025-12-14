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

// ==================== ACHIEVEMENTS SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(navController: NavController) {
    val achievements = remember {
        listOf(
            AchievementItem("🌟", "First Steps", "Complete your first letter", true, 10),
            AchievementItem("🏆", "Star Collector", "Earn 50 stars", true, 20),
            AchievementItem("🎯", "Perfect Game", "Get 100% in a quiz", false, 30),
            AchievementItem("📚", "Bookworm", "Complete 10 stories", false, 40),
            AchievementItem("🔥", "Hot Streak", "7 day learning streak", true, 50),
            AchievementItem("🎨", "Artistic", "Complete 5 coloring pages", false, 25),
            AchievementItem("✏️", "Master Writer", "Practice 50 letters", false, 35),
            AchievementItem("🧠", "Memory Master", "Win 10 memory games", true, 30)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFF7ED), Color(0xFFFEF3C7))
                )
            )
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        "Achievements",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Collect them all! 🏆",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Stats Banner
        AchievementsBanner(
            unlocked = achievements.count { it.unlocked },
            total = achievements.size
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Achievements Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(achievements) { achievement ->
                AchievementCard(achievement)
            }
        }
    }
}

data class AchievementItem(
    val emoji: String,
    val title: String,
    val description: String,
    val unlocked: Boolean,
    val stars: Int
)

@Composable
fun AchievementsBanner(unlocked: Int, total: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "$unlocked / $total",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF59E0B)
                )
                Text(
                    "Achievements Unlocked",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFFFED7AA), Color(0xFFFEF3C7))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🏆", fontSize = 40.sp)
            }
        }
    }
}

@Composable
fun AchievementCard(achievement: AchievementItem) {
    var showDialog by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (achievement.unlocked) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { showDialog = true },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.unlocked) Color.White else Color.Gray.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(if (achievement.unlocked) 8.dp else 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                // Emoji with animation
                Text(
                    text = achievement.emoji,
                    fontSize = 48.sp,
                    modifier = if (achievement.unlocked) {
                        Modifier.rotate(rotation)
                    } else {
                        Modifier
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = achievement.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (achievement.unlocked) Color(0xFF2D3748) else Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⭐", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${achievement.stars}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFFA500)
                    )
                }

                if (!achievement.unlocked) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    if (showDialog) {
        AchievementDialog(achievement) {
            showDialog = false
        }
    }
}

@Composable
fun AchievementDialog(achievement: AchievementItem, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(achievement.emoji, fontSize = 60.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(achievement.title, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    achievement.description,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (achievement.unlocked) {
                    Text(
                        "✨ Unlocked! ✨",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                } else {
                    Text(
                        "Keep going to unlock!",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Awesome!")
            }
        }
    )
}

// ==================== REWARDS STORE SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsStoreScreen(navController: NavController) {
    val coins = remember { mutableStateOf(150) }

    val rewards = listOf(
        RewardItem("🦸", "Superhero Avatar", 50, false),
        RewardItem("👸", "Princess Avatar", 50, false),
        RewardItem("🚀", "Astronaut Avatar", 100, false),
        RewardItem("🌈", "Rainbow Theme", 150, false),
        RewardItem("🌊", "Ocean Theme", 150, false),
        RewardItem("⭐", "Gold Star Pack", 25, false),
        RewardItem("🎨", "Art Supplies", 75, false),
        RewardItem("🎵", "Music Pack", 100, false)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEDE9FE), Color(0xFFFAF5FF))
                )
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    "Rewards Store",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            },
            actions = {
                // Coin Balance
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFBBF24)
                    ),
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🪙", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "${coins.value}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(rewards) { reward ->
                RewardCard(reward, coins.value) {
                    if (coins.value >= reward.cost) {
                        coins.value -= reward.cost
                    }
                }
            }
        }
    }
}

data class RewardItem(
    val emoji: String,
    val name: String,
    val cost: Int,
    val owned: Boolean
)

@Composable
fun RewardCard(reward: RewardItem, currentCoins: Int, onBuy: () -> Unit) {
    val canAfford = currentCoins >= reward.cost

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(reward.emoji, fontSize = 56.sp)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    reward.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onBuy,
                    enabled = canAfford && !reward.owned,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B5CF6),
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (reward.owned) {
                        Text("Owned ✓")
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🪙")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${reward.cost}")
                        }
                    }
                }
            }
        }
    }
}