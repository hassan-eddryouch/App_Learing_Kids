package com.kidslearning.app

import android.media.MediaPlayer
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Letter(
    val char: String,
    val soundResource: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArabicAlphabetScreen(navController: NavController) {
    val arabicLetters = remember {
        listOf(
            "ا", "ب", "ت", "ث", "ج", "ح", "خ", "د", "ذ", "ر", "ز", "س",
            "ش", "ص", "ض", "ط", "ظ", "ع", "غ", "ف", "ق", "ك", "ل", "م",
            "ن", "ه", "و", "ي"
        ).mapIndexed { index, letter ->
            Letter(
                letter,
                "arabic_$letter",
                Color(0xFF8B5CF6).copy(alpha = 0.7f + (index % 3) * 0.1f)
            )
        }
    }

    AlphabetTemplate(
        title = "حروف عربية 🌙",
        subtitle = "Arabic Alphabet",
        letters = arabicLetters,
        navController = navController,
        backgroundColor = Brush.verticalGradient(
            colors = listOf(Color(0xFF8B5CF6), Color(0xFFC084FC))
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrenchAlphabetScreen(navController: NavController) {
    val frenchLetters = remember {
        ('A'..'Z').mapIndexed { index, letter ->
            Letter(
                letter.toString(),
                "french_$letter",
                Color(0xFF10B981).copy(alpha = 0.7f + (index % 3) * 0.1f)
            )
        }
    }

    AlphabetTemplate(
        title = "French Letters 🇫🇷",
        subtitle = "Lettres Françaises",
        letters = frenchLetters,
        navController = navController,
        backgroundColor = Brush.verticalGradient(
            colors = listOf(Color(0xFF10B981), Color(0xFF34D399))
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlphabetTemplate(
    title: String,
    subtitle: String,
    letters: List<Letter>,
    navController: NavController,
    backgroundColor: Brush
) {
    val context = LocalContext.current
    var selectedLetter by remember { mutableStateOf<Letter?>(null) }

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
                        text = title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.background(backgroundColor)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(letters) { letter ->
                LetterCard(letter = letter) {
                    selectedLetter = letter
                    // Play sound (simulation)
                    // MediaPlayer.create(context, soundId).start()
                    navController.navigate("draw/${letter.char}")
                }
            }
        }
    }
}

@Composable
fun LetterCard(letter: Letter, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            letter.color.copy(alpha = 0.3f),
                            letter.color.copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter.char,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                modifier = Modifier.scale(if (isPressed) pulseScale else 1f)
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(200)
            isPressed = false
        }
    }
}