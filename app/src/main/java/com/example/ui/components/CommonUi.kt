package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun shimmerBrush(showShimmer: Boolean = true): Brush {
    if (!showShimmer) return Brush.linearGradient(colors = listOf(Color.Transparent, Color.Transparent))

    val isDark = isSystemInDarkTheme()
    val baseColor = if (isDark) Color(0xFF221F38) else Color(0xFFE5E7EB)
    val highlightColor = if (isDark) Color(0xFF332F51) else Color(0xFFF3F4F6)

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1300f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1350, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )

    return Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(translateAnimation - 350f, translateAnimation - 350f),
        end = Offset(translateAnimation, translateAnimation)
    )
}

fun Modifier.glassmorphic(
    backgroundColor: Color,
    borderColor: Color = Color.White.copy(alpha = 0.15f),
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 10.dp
) = this.then(
    Modifier
        .shadow(elevation = elevation, shape = RoundedCornerShape(cornerRadius), clip = false)
        .background(
            color = backgroundColor.copy(alpha = 0.72f),
            shape = RoundedCornerShape(cornerRadius)
        )
        .border(1.dp, borderColor, RoundedCornerShape(cornerRadius))
)

@Composable
fun AnimatedEntrance(
    delayMillis: Int = 0,
    content: @Composable () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        isVisible = true
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(600, easing = FastOutSlowInEasing)) +
                slideInVertically(
                    initialOffsetY = { 60 },
                    animationSpec = tween(600, easing = FastOutSlowInEasing)
                )
    ) {
        content()
    }
}

@Composable
fun SkillBar(
    name: String,
    targetValue: Float,
    iconName: String,
    accentColor: Color
) {
    var animatedValue by remember { mutableStateOf(0f) }
    val progressAnim by animateFloatAsState(
        targetValue = animatedValue,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "progress"
    )

    LaunchedEffect(Unit) {
        delay(120)
        animatedValue = targetValue
    }

    val icon = remember(iconName) { getIconByName(iconName) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(accentColor.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${(progressAnim * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                modifier = Modifier.testTag("skill_percent_$name")
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Modern Custom Animated Slide Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressAnim)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(accentColor.copy(alpha = 0.7f), accentColor)
                        )
                    )
            )
        }
    }
}

@Composable
fun StatsCounter(
    title: String,
    targetVal: Int,
    displayValue: String,
    accentColor: Color
) {
    var animatedValue by remember { mutableStateOf(0) }
    val valueAnim by animateIntAsState(
        targetValue = animatedValue,
        animationSpec = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
        label = "counter"
    )

    LaunchedEffect(Unit) {
        delay(100)
        animatedValue = targetVal
    }

    // Format suffix
    val formattedCount = remember(valueAnim, targetVal, displayValue) {
        if (valueAnim >= targetVal) {
            displayValue
        } else {
            val suffix = displayValue.filter { !it.isDigit() }
            "$valueAnim$suffix"
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .glassmorphic(MaterialTheme.colorScheme.surface)
            .padding(1.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = formattedCount,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = accentColor,
                modifier = Modifier.testTag("stat_counter_${title.replace(" ", "_")}")
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun HeaderSyncBar(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    accentColor: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rotate")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotateAnim"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .glassmorphic(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f), cornerRadius = 16.dp)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = "Pull to refresh elements or sync",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onRefresh,
            modifier = Modifier
                .size(36.dp)
                .testTag("refresh_action_button")
        ) {
            if (isRefreshing) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Syncing",
                    tint = accentColor,
                    modifier = Modifier.rotate(rotation)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Sync",
                    tint = accentColor
                )
            }
        }
    }
}

// Helper to look up premium material icons for dynamic skill tags
fun getIconByName(name: String): ImageVector {
    return when (name.lowercase()) {
        "code" -> Icons.Default.Code
        "brush" -> Icons.Default.Brush
        "android" -> Icons.Default.Android
        "speed" -> Icons.Default.Speed
        "dns" -> Icons.Default.Dns
        "storage" -> Icons.Default.Storage
        "cloud" -> Icons.Default.Cloud
        "api" -> Icons.Default.Wifi
        "palette" -> Icons.Default.Palette
        "layers" -> Icons.Default.Layers
        "check_circle" -> Icons.Default.CheckCircle
        "build" -> Icons.Default.Build
        else -> Icons.Default.Code
    }
}
