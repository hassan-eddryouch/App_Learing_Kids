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
import kotlin.random.Random

// ==================== MEMORY GAME ====================
data class MemoryCard(
    val id: Int,
    val emoji: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryGameScreen(navController: NavController) {
    val emojis = listOf("🍎", "🍌", "🍇", "🍓", "🍊", "🍉", "🍒", "🥝")
    val cards = remember {
        mutableStateOf(
            (emojis + emojis).mapIndexed { index, emoji ->
                MemoryCard(index, emoji)
            }.shuffled().toMutableList()
        )
    }

    var flippedCards by remember { mutableStateOf(listOf<Int>()) }
    var moves by remember { mutableStateOf(0) }
    var matchedPairs by remember { mutableStateOf(0) }
    var showWinDialog by remember { mutableStateOf(false) }

    LaunchedEffect(flippedCards) {
        if (flippedCards.size == 2) {
            moves++
            kotlinx.coroutines.delay(800)

            val first = cards.value[flippedCards[0]]
            val second = cards.value[flippedCards[1]]

            if (first.emoji == second.emoji) {
                cards.value[flippedCards[0]].isMatched = true
                cards.value[flippedCards[1]].isMatched = true
                matchedPairs++

                if (matchedPairs == emojis.size) {
                    showWinDialog = true
                }
            } else {
                cards.value[flippedCards[0]].isFlipped = false
                cards.value[flippedCards[1]].isFlipped = false
            }

            flippedCards = emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFE5E5), Color(0xFFFFF0F5))
                )
            )
    ) {
        TopAppBar(
            title = { Text("Memory Game 🧠", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            },
            actions = {
                Text(
                    text = "Moves: $moves",
                    modifier = Modifier.padding(end = 16.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B5CF6)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cards.value.size) { index ->
                MemoryCardItem(
                    card = cards.value[index],
                    onClick = {
                        if (flippedCards.size < 2 && !cards.value[index].isFlipped && !cards.value[index].isMatched) {
                            cards.value[index].isFlipped = true
                            flippedCards = flippedCards + index
                        }
                    }
                )
            }
        }

        if (showWinDialog) {
            WinDialog(
                moves = moves,
                onPlayAgain = {
                    cards.value = (emojis + emojis).mapIndexed { index, emoji ->
                        MemoryCard(index, emoji)
                    }.shuffled().toMutableList()
                    moves = 0
                    matchedPairs = 0
                    showWinDialog = false
                },
                onClose = { navController.navigateUp() }
            )
        }
    }
}

@Composable
fun MemoryCardItem(card: MemoryCard, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFlipped || card.isMatched) 180f else 0f,
        animationSpec = tween(400)
    )

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .rotate(rotation)
            .clickable(enabled = !card.isMatched) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (card.isMatched) Color(0xFF10B981)
            else if (card.isFlipped) Color.White
            else Color(0xFF8B5CF6)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (card.isFlipped || card.isMatched) {
                Text(
                    text = card.emoji,
                    fontSize = 40.sp,
                    modifier = Modifier.rotate(180f)
                )
            } else {
                Text(
                    text = "?",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// ==================== QUIZ GAME ====================
data class QuizQuestion(
    val question: String,
    val emoji: String,
    val options: List<String>,
    val correctAnswer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizGameScreen(navController: NavController) {
    val questions = remember {
        listOf(
            QuizQuestion("What letter is this?", "A", listOf("A", "B", "C", "D"), "A"),
            QuizQuestion("What letter comes after B?", "🔤", listOf("A", "C", "D", "E"), "C"),
            QuizQuestion("First letter of Apple?", "🍎", listOf("B", "A", "P", "L"), "A"),
            QuizQuestion("What letter is this?", "م", listOf("م", "ن", "س", "ل"), "م"),
            QuizQuestion("Last letter of Book?", "📚", listOf("B", "O", "K", "N"), "K")
        )
    }

    var currentQuestion by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFDCFCE7), Color(0xFFF0FDF4))
                )
            )
    ) {
        TopAppBar(
            title = { Text("Quiz Time! ❓", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            },
            actions = {
                Text(
                    text = "Score: $score/${questions.size}",
                    modifier = Modifier.padding(end = 16.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF10B981)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        if (currentQuestion < questions.size) {
            val question = questions[currentQuestion]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress
                LinearProgressIndicator(
                    progress = (currentQuestion + 1) / questions.size.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF10B981)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Emoji
                Text(
                    text = question.emoji,
                    fontSize = 100.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Question
                Text(
                    text = question.question,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Options
                question.options.forEach { option ->
                    QuizOption(
                        text = option,
                        isSelected = selectedAnswer == option,
                        isCorrect = showResult && option == question.correctAnswer,
                        isWrong = showResult && selectedAnswer == option && option != question.correctAnswer,
                        onClick = {
                            if (!showResult) {
                                selectedAnswer = option
                                isCorrect = option == question.correctAnswer
                                showResult = true
                                if (isCorrect) score++
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.weight(1f))

                if (showResult) {
                    Button(
                        onClick = {
                            currentQuestion++
                            selectedAnswer = null
                            showResult = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(
                            text = if (currentQuestion < questions.size - 1) "Next Question →" else "Finish!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            QuizComplete(score = score, total = questions.size, navController = navController)
        }
    }
}

@Composable
fun QuizOption(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isWrong: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isCorrect -> Color(0xFF10B981)
        isWrong -> Color(0xFFEF4444)
        isSelected -> Color(0xFF8B5CF6)
        else -> Color.White
    }

    val textColor = if (isCorrect || isWrong || isSelected) Color.White else Color(0xFF1F2937)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(if (isSelected) 12.dp else 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun QuizComplete(score: Int, total: Int, navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(text = "🎉", fontSize = 100.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Quiz Complete!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF10B981)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your Score: $score/$total",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = { navController.navigateUp() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B5CF6)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("Back to Home", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WinDialog(moves: Int, onPlayAgain: () -> Unit, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🎊", fontSize = 60.sp)
                Text("You Won!", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Text(
                "Completed in $moves moves!",
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = onPlayAgain) {
                Text("Play Again")
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text("Close")
            }
        }
    )
}