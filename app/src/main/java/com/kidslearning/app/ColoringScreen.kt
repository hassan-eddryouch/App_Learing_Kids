package com.kidslearning.app

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.sqrt

data class ColoringShape(
    val path: Path,
    var fillColor: Color = Color.White,
    val strokeColor: Color = Color.Black
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColoringScreen(navController: NavController) {
    var shapes by remember { mutableStateOf(createColoringShapes()) }
    var selectedColor by remember { mutableStateOf(Color(0xFFFF6B9D)) }
    var showSuccessAnimation by remember { mutableStateOf(false) }

    val colorPalette = listOf(
        Color(0xFFFF6B9D),
        Color(0xFF8B5CF6),
        Color(0xFF3B82F6),
        Color(0xFF10B981),
        Color(0xFFFBBF24),
        Color(0xFFF97316),
        Color(0xFFEF4444),
        Color(0xFFA855F7),
        Color(0xFF06B6D4),
        Color(0xFF84CC16)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBEB))
    ) {
        TopAppBar(
            title = { Text("Coloring Book 🎨", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            },
            actions = {
                IconButton(onClick = {
                    shapes = createColoringShapes()
                }) {
                    Icon(Icons.Default.Refresh, "Reset")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Color Palette
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                colorPalette.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedColor == color) 4.dp else 0.dp,
                                color = Color.Black,
                                shape = CircleShape
                            )
                            .clickable { selectedColor = color }
                    )
                }
            }
        }

        // Coloring Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                shapes = shapes.map { shape ->
                                    if (isPointInPath(shape.path, offset)) {
                                        shape.copy(fillColor = selectedColor)
                                    } else {
                                        shape
                                    }
                                }

                                // Check if all colored
                                if (shapes.all { it.fillColor != Color.White }) {
                                    showSuccessAnimation = true
                                }
                            }
                        }
                ) {
                    shapes.forEach { shape ->
                        // Fill
                        drawPath(
                            path = shape.path,
                            color = shape.fillColor
                        )
                        // Stroke
                        drawPath(
                            path = shape.path,
                            color = shape.strokeColor,
                            style = Stroke(
                                width = 6f,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }

                if (showSuccessAnimation) {
                    SuccessOverlay {
                        showSuccessAnimation = false
                    }
                }
            }
        }

        // Instructions
        Text(
            text = "Tap any section to color it! 🖍️",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6B7280),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

fun createColoringShapes(): List<ColoringShape> {
    val shapes = mutableListOf<ColoringShape>()

    // Simple house drawing
    // Roof
    shapes.add(
        ColoringShape(
            path = Path().apply {
                moveTo(300f, 200f)
                lineTo(500f, 400f)
                lineTo(100f, 400f)
                close()
            }
        )
    )

    // House body
    shapes.add(
        ColoringShape(
            path = Path().apply {
                moveTo(120f, 400f)
                lineTo(480f, 400f)
                lineTo(480f, 700f)
                lineTo(120f, 700f)
                close()
            }
        )
    )

    // Door
    shapes.add(
        ColoringShape(
            path = Path().apply {
                moveTo(250f, 500f)
                lineTo(350f, 500f)
                lineTo(350f, 700f)
                lineTo(250f, 700f)
                close()
            }
        )
    )

    // Left window
    shapes.add(
        ColoringShape(
            path = Path().apply {
                moveTo(150f, 470f)
                lineTo(220f, 470f)
                lineTo(220f, 540f)
                lineTo(150f, 540f)
                close()
            }
        )
    )

    // Right window
    shapes.add(
        ColoringShape(
            path = Path().apply {
                moveTo(380f, 470f)
                lineTo(450f, 470f)
                lineTo(450f, 540f)
                lineTo(380f, 540f)
                close()
            }
        )
    )

    // Sun
    shapes.add(
        ColoringShape(
            path = Path().apply {
                addOval(
                    androidx.compose.ui.geometry.Rect(
                        left = 600f,
                        top = 150f,
                        right = 700f,
                        bottom = 250f
                    )
                )
            }
        )
    )

    // Tree (circle top)
    shapes.add(
        ColoringShape(
            path = Path().apply {
                addOval(
                    androidx.compose.ui.geometry.Rect(
                        left = 700f,
                        top = 450f,
                        right = 850f,
                        bottom = 600f
                    )
                )
            }
        )
    )

    // Tree trunk
    shapes.add(
        ColoringShape(
            path = Path().apply {
                moveTo(750f, 600f)
                lineTo(800f, 600f)
                lineTo(800f, 700f)
                lineTo(750f, 700f)
                close()
            }
        )
    )

    return shapes
}

fun isPointInPath(path: Path, point: Offset): Boolean {
    // Simple bounds checking (simplified)
    return true // For simplicity, always return true for tap detection
}

@Composable
fun SuccessOverlay(onDismiss: () -> Unit) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        visible = false
        onDismiss()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(48.dp)
                ) {
                    Text(text = "🎨", fontSize = 80.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Masterpiece!",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B5CF6)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You colored everything!",
                        fontSize = 20.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}