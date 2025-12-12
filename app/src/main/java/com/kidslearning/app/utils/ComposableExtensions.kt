package com.kidslearning.app.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Composable extension functions
 */

// Clickable with ripple effect
fun Modifier.clickableWithRipple(
    enabled: Boolean = true,
    rippleColor: Color = Color.Gray,
    rippleRadius: Dp = 24.dp,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    this.clickable(
        enabled = enabled,
        interactionSource = interactionSource,
        indication = rememberRipple(
            bounded = true,
            radius = rippleRadius,
            color = rippleColor
        ),
        onClick = onClick
    )
}

// Conditional modifier
fun Modifier.conditional(
    condition: Boolean,
    modifier: Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

@Composable
fun rememberContext() = LocalContext.current