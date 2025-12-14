package com.kidslearning.app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// ==================== STORY MODE SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryModeScreen(navController: NavController) {
    val stories = remember {
        listOf(
            StoryItem(
                1,
                "مغامرة الحروف",
                "The Adventure of Letters",
                "في يوم مشمس جميل، قررت الحروف العربية أن تذهب في مغامرة رائعة...",
                "arabic",
                "📖",
                true,
                1
            ),
            StoryItem(
                2,
                "حديقة الكلمات",
                "The Garden of Words",
                "وجدت الحروف حديقة سحرية مليئة بالكلمات الجميلة...",
                "arabic",
                "🌺",
                false,
                3
            ),
            StoryItem(
                3,
                "L'Aventure des Lettres",
                "The Adventure of Letters",
                "Un beau jour ensoleillé, les lettres françaises ont décidé de partir à l'aventure...",
                "french",
                "📚",
                true,
                1
            ),
            StoryItem(
                4,
                "Le Jardin Magique",
                "The Magic Garden",
                "Les lettres ont découvert un jardin magique rempli de beaux mots...",
                "french",
                "🌸",
                false,
                3
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFCE7F3), Color(0xFFFDF2F8))
                )
            )
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        "Story Time",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Learn through stories 📚",
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

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(stories) { story ->
                StoryCard(story) {
                    navController.navigate("story_reader/${story.id}")
                }
            }
        }
    }
}

data class StoryItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val preview: String,
    val language: String,
    val emoji: String,
    val unlocked: Boolean,
    val requiredLevel: Int
)

@Composable
fun StoryCard(story: StoryItem, onClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (story.unlocked) onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (story.unlocked) Color.White else Color.Gray.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(if (story.unlocked) 8.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(story.emoji, fontSize = 48.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            story.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (story.unlocked) Color(0xFF2D3748) else Color.Gray
                        )
                        Text(
                            story.subtitle,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                if (!story.unlocked) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color.Gray
                        )
                        Text(
                            "Level ${story.requiredLevel}",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            if (story.unlocked) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    story.preview,
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 20.sp,
                    maxLines = if (expanded) Int.MAX_VALUE else 2
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { expanded = !expanded }) {
                        Text(if (expanded) "Show less" else "Read more")
                    }

                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEC4899)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Start Reading")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "Read",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// ==================== STORY READER SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun StoryReaderScreen(storyId: Int, navController: NavController) {
    var currentPage by remember { mutableStateOf(0) }
    val totalPages = 5

    val storyPages = remember {
        listOf(
            StoryPage(
                "في يوم جميل مشمس",
                "On a beautiful sunny day",
                "☀️",
                "The sun was shining brightly in the sky..."
            ),
            StoryPage(
                "خرجت الحروف للعب",
                "The letters went out to play",
                "🎈",
                "All the Arabic letters decided to play together..."
            ),
            StoryPage(
                "وجدوا حديقة جميلة",
                "They found a beautiful garden",
                "🌺",
                "The garden was full of colorful flowers..."
            ),
            StoryPage(
                "تعلموا أشياء جديدة",
                "They learned new things",
                "📚",
                "Each letter learned something special..."
            ),
            StoryPage(
                "وعادوا سعداء",
                "And they returned happy",
                "😊",
                "They all went home with smiles on their faces!"
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFF7ED), Color(0xFFFEF3C7))
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            TopAppBar(
                title = {
                    Text(
                        "Page ${currentPage + 1} of $totalPages",
                        fontWeight = FontWeight.Bold
                    )
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

            // Story Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = currentPage,
                    transitionSpec = {
                        slideInHorizontally { it } + fadeIn() togetherWith
                                slideOutHorizontally { -it } + fadeOut()
                    },
                    label = "story_page_transition"
                ) { page ->
                    StoryPageContent(storyPages[page])
                }
            }

            // Navigation Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { if (currentPage > 0) currentPage-- },
                    enabled = currentPage > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B5CF6)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.KeyboardArrowLeft, "Previous")
                    Text("Previous")
                }

                Button(
                    onClick = {
                        if (currentPage < totalPages - 1) {
                            currentPage++
                        } else {
                            navController.navigateUp()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF10B981)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(if (currentPage < totalPages - 1) "Next" else "Finish")
                    Icon(
                        if (currentPage < totalPages - 1) Icons.Default.KeyboardArrowRight
                        else Icons.Default.Check,
                        "Next"
                    )
                }
            }
        }
    }
}

data class StoryPage(
    val arabicText: String,
    val englishText: String,
    val emoji: String,
    val narration: String
)

@Composable
fun StoryPageContent(page: StoryPage) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                page.emoji,
                fontSize = 100.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                page.arabicText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B5CF6),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                page.englishText,
                fontSize = 18.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                page.narration,
                fontSize = 16.sp,
                color = Color(0xFF4B5563),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}