package com.kidslearning.app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboardScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Progress", "Reports", "Settings")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Parent Dashboard",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Track your child's learning",
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
                containerColor = Color.White
            )
        )

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        // Content
        when (selectedTab) {
            0 -> OverviewTab()
            1 -> ProgressTab()
            2 -> ReportsTab()
            3 -> SettingsTab()
        }
    }
}

@Composable
fun OverviewTab() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Weekly Summary Card
            WeeklySummaryCard()
        }

        item {
            Text(
                "Today's Activity",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )
        }

        item {
            TodayActivityCard()
        }

        item {
            Text(
                "Learning Streak 🔥",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )
        }

        item {
            StreakCard()
        }

        item {
            Text(
                "Recent Achievements",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )
        }

        item {
            AchievementsRow()
        }
    }
}

@Composable
fun WeeklySummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "This Week's Summary",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("⏱️", "2h 45m", "Time Spent")
                StatItem("📚", "12", "Letters Learned")
                StatItem("🎮", "8", "Games Played")
                StatItem("⭐", "95", "Stars Earned")
            }
        }
    }
}

@Composable
fun StatItem(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 32.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B5CF6)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun TodayActivityCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F9FF)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "35 minutes",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0284C7)
                    )
                    Text(
                        "Learning time today",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                CircularProgressIndicator(
                    progress = 0.7f,
                    modifier = Modifier.size(60.dp),
                    color = Color(0xFF0284C7),
                    strokeWidth = 6.dp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = 0.7f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF0284C7)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Goal: 50 minutes per day",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun StreakCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF7ED)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "7 Days",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF97316)
                )
                Text(
                    "Current streak!",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFFFED7AA), Color(0xFFFEF3C7))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🔥", fontSize = 48.sp)
            }
        }
    }
}

@Composable
fun AchievementsRow() {
    val achievements = listOf(
        "🌟" to "First Letter",
        "🏆" to "10 Stars",
        "🎯" to "Perfect Game",
        "📚" to "Bookworm"
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(achievements) { (emoji, title) ->
            Card(
                modifier = Modifier
                    .width(100.dp)
                    .height(120.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = emoji, fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3748)
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressTab() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProgressCard(
                title = "Arabic Letters",
                progress = 0.65f,
                completed = 18,
                total = 28,
                color = Color(0xFF8B5CF6)
            )
        }

        item {
            ProgressCard(
                title = "French Letters",
                progress = 0.45f,
                completed = 12,
                total = 26,
                color = Color(0xFF10B981)
            )
        }

        item {
            Text(
                "Skills Development",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            SkillProgressBars()
        }
    }
}

@Composable
fun ProgressCard(
    title: String,
    progress: Float,
    completed: Int,
    total: Int,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = color
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "$completed of $total completed",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SkillProgressBars() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SkillBar("Reading", 0.75f, Color(0xFF3B82F6))
        SkillBar("Writing", 0.60f, Color(0xFFEC4899))
        SkillBar("Memory", 0.85f, Color(0xFFF59E0B))
        SkillBar("Pronunciation", 0.70f, Color(0xFF10B981))
    }
}

@Composable
fun SkillBar(name: String, progress: Float, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text("${(progress * 100).toInt()}%", fontSize = 14.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color
        )
    }
}

@Composable
fun ReportsTab() {
    // Reports content
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Weekly and monthly reports coming soon!")
    }
}

@Composable
fun SettingsTab() {
    // Settings content
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Parent settings coming soon!")
    }
}