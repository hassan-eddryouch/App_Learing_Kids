package com.kidslearning.app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class DrawPoint(val x: Float, val y: Float)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawLetterScreen(letter: String, navController: NavController) {
    var paths by remember { mutableStateOf(listOf<List<DrawPoint>>()) }
    var currentPath by remember { mutableStateOf(listOf<DrawPoint>()) }
    var showCelebration by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color(0xFF8B5CF6)) }
    var brushSize by remember { mutableStateOf(20f) }

    val colors = listOf(
        Color(0xFF8B5CF6),
        Color(0xFFEC4899),
        Color(0xFF10B981),
        Color(0xFFF59E0B),
        Color(0xFF3B82F6),
        Color(0xFFEF4444)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Draw: $letter",
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
                IconButton(onClick = {
                    // Play sound
                    showCelebration = true
                }) {
                    Icon(
                        Icons.Default.VolumeUp,
                        contentDescription = "Play Sound",
                        tint = Color(0xFF8B5CF6)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Letter Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFDDD6FE), Color(0xFFFAE8FF))
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter,
                fontSize = 120.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B5CF6).copy(alpha = 0.3f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Color Picker
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(color, CircleShape)
                        .then(
                            if (selectedColor == color) {
                                Modifier.padding(4.dp)
                            } else Modifier
                        )
                        .background(
                            if (selectedColor == color) Color.White else Color.Transparent,
                            CircleShape
                        )
                        .padding(if (selectedColor == color) 4.dp else 0.dp)
                        .background(color, CircleShape)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { selectedColor = color },
                                onDrag = { _, _ -> }
                            )
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Drawing Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentPath = listOf(DrawPoint(offset.x, offset.y))
                                },
                                onDrag = { change, _ ->
                                    currentPath = currentPath + DrawPoint(
                                        change.position.x,
                                        change.position.y
                                    )
                                },
                                onDragEnd = {
                                    paths = paths + listOf(currentPath)
                                    currentPath = emptyList()
                                }
                            )
                        }
                ) {
                    // Draw completed paths
                    paths.forEach { path ->
                        if (path.size > 1) {
                            for (i in 0 until path.size - 1) {
                                drawLine(
                                    color = selectedColor,
                                    start = Offset(path[i].x, path[i].y),
                                    end = Offset(path[i + 1].x, path[i + 1].y),
                                    strokeWidth = brushSize,
                                    cap = StrokeCap.Round
                                )
                            }
                        }
                    }

                    // Draw current path
                    if (currentPath.size > 1) {
                        for (i in 0 until currentPath.size - 1) {
                            drawLine(
                                color = selectedColor,
                                start = Offset(currentPath[i].x, currentPath[i].y),
                                end = Offset(currentPath[i + 1].x, currentPath[i + 1].y),
                                strokeWidth = brushSize,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }

                // Celebration Animation
                if (showCelebration) {
                    CelebrationAnimation {
                        showCelebration = false
                    }
                }
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    paths = emptyList()
                    currentPath = emptyList()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Clear")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Clear", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    showCelebration = true
                    // Save progress
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF10B981)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Done")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Done!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CelebrationAnimation(onComplete: () -> Unit) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        visible = false
        onComplete()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "🎉",
                    fontSize = 80.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Great Job!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF10B981)
                )
                Text(
                    text = "Keep practicing!",
                    fontSize = 20.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}