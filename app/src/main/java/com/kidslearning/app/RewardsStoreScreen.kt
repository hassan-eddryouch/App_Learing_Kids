package com.kidslearning.app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kidslearning.app.data.Reward
import com.kidslearning.app.viewmodels.RewardsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsStoreScreen(
    navController: NavController,
    viewModel: RewardsViewModel = viewModel()
) {
    val rewards by viewModel.rewards.collectAsState()
    val coinBalance by viewModel.coinBalance.collectAsState()
    val purchasedRewards by viewModel.purchasedRewards.collectAsState()
    
    var showPurchaseDialog by remember { mutableStateOf<Reward?>(null) }
    var showInsufficientCoinsDialog by remember { mutableStateOf(false) }
    
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
            // Top Bar with Coin Balance
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Rewards Store",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        CoinBalanceCard(coinBalance)
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
            
            // Rewards Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(rewards) { reward ->
                    RewardCard(
                        reward = reward,
                        isPurchased = purchasedRewards.contains(reward.id),
                        canAfford = coinBalance >= reward.cost,
                        onPurchaseClick = {
                            if (coinBalance >= reward.cost) {
                                showPurchaseDialog = reward
                            } else {
                                showInsufficientCoinsDialog = true
                            }
                        }
                    )
                }
            }
        }
        
        // Purchase Confirmation Dialog
        showPurchaseDialog?.let { reward ->
            PurchaseDialog(
                reward = reward,
                onConfirm = {
                    viewModel.purchaseReward(reward)
                    showPurchaseDialog = null
                },
                onDismiss = { showPurchaseDialog = null }
            )
        }
        
        // Insufficient Coins Dialog
        if (showInsufficientCoinsDialog) {
            InsufficientCoinsDialog(
                onDismiss = { showInsufficientCoinsDialog = false }
            )
        }
    }
}

@Composable
fun CoinBalanceCard(balance: Int) {
    val infiniteTransition = rememberInfiniteTransition()
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Card(
        modifier = Modifier.scale(shimmer),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFD700)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🪙", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                balance.toString(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
        }
    }
}

@Composable
fun RewardCard(
    reward: Reward,
    isPurchased: Boolean,
    canAfford: Boolean,
    onPurchaseClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                enabled = !isPurchased && canAfford
            ) {
                isPressed = true
                onPurchaseClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isPurchased -> Color(0xFFE8F5E8)
                canAfford -> Color.White
                else -> Color(0xFFF5F5F5)
            }
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                reward.emoji,
                fontSize = 48.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                reward.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (canAfford || isPurchased) Color(0xFF2D3748) else Color.Gray
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                reward.description,
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            when {
                isPurchased -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Purchased",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Owned",
                            fontSize = 12.sp,
                            color = Color(0xFF10B981),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                canAfford -> {
                    Button(
                        onClick = onPurchaseClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🪙 ${reward.cost}", color = Color(0xFF8B4513))
                    }
                }
                else -> {
                    Button(
                        onClick = { },
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🪙 ${reward.cost}", color = Color.White)
                    }
                }
            }
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
fun PurchaseDialog(
    reward: Reward,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Purchase ${reward.name}?",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(reward.emoji, fontSize = 48.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "This will cost ${reward.cost} coins.",
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF10B981)
                )
            ) {
                Text("Purchase")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun InsufficientCoinsDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Not Enough Coins!",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF4444)
            )
        },
        text = {
            Column {
                Text("💰", fontSize = 48.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "You need more coins to purchase this item. Play games to earn more!",
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B5CF6)
                )
            ) {
                Text("OK")
            }
        }
    )
}