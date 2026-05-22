package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.ui.components.AnimatedEntrance
import com.example.ui.components.glassmorphic
import com.example.ui.components.shimmerBrush
import com.example.ui.viewmodel.PortfolioViewModel
import com.example.ui.viewmodel.Project

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: PortfolioViewModel,
    onNavigateToTab: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor by viewModel.accentColor.collectAsState()
    val isDark = isSystemInDarkTheme()
    val themeAccent = Color(accentColor.hex)

    // Breathing float animation for the header circle
    val floatOffset = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        floatOffset.animateTo(
            targetValue = 12f,
            animationSpec = infiniteRepeatable(
                animation = tween(2200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Aesthetic ambient fluid background gradient meshes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .background(
                    Brush.radialGradient(
                        colors = if (isDark) {
                            listOf(themeAccent.copy(alpha = 0.15f), Color.Transparent)
                        } else {
                            listOf(themeAccent.copy(alpha = 0.08f), Color.Transparent)
                        },
                        radius = 800f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Animated Header Section
            AnimatedEntrance(delayMillis = 100) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(0, floatOffset.value.toInt()) }
                        .size(136.dp)
                        .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.4f), cornerRadius = 68.dp)
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=350&q=80",
                        contentDescription = "Avatar portrait",
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(shimmerBrush(), CircleShape)
                            )
                        },
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .testTag("home_profile_image")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Name & Professional Title
            AnimatedEntrance(delayMillis = 200) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Samantha Sterling",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("home_profile_name")
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Senior Android Systems Architect",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = themeAccent,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("home_profile_title")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Professional description summary card
            AnimatedEntrance(delayMillis = 300) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassmorphic(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), cornerRadius = 24.dp)
                        .padding(1.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Aesthetic. Fast. Native.",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Crafting high-fidelity, high-fps fluid mobile systems. Focused on advanced Kotlin compiler pipelines, Jetpack Compose shader brush mechanics, offline state durability, and Material 3 responsive dynamics.",
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Kotlin", "Compose", "Architecture", "Shaders", "Room DB").forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .background(themeAccent.copy(alpha = 0.1f), CircleShape)
                                        .border(1.dp, themeAccent.copy(alpha = 0.2f), CircleShape)
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = themeAccent
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Highlight Grid Cards
            AnimatedEntrance(delayMillis = 400) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Left Highlight
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .glassmorphic(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), cornerRadius = 20.dp)
                            .clickable { onNavigateToTab("about") }
                            .testTag("about_highlight_card"),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(themeAccent.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Code, null, tint = themeAccent, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Professional", style = MaterialTheme.typography.labelSmall, color = themeAccent, fontWeight = FontWeight.Bold)
                            Text("Skills & Experience", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(8.dp))
                            Icon(Icons.AutoMirrored.Default.ArrowForward, "View skills", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Right Highlight
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .glassmorphic(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), cornerRadius = 20.dp)
                            .clickable { onNavigateToTab("projects") }
                            .testTag("projects_highlight_card"),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(themeAccent.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Star, null, tint = themeAccent, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Portfolio", style = MaterialTheme.typography.labelSmall, color = themeAccent, fontWeight = FontWeight.Bold)
                            Text("Featured Projects", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(8.dp))
                            Icon(Icons.AutoMirrored.Default.ArrowForward, "View portfolio", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Quick Call To Action Button
            AnimatedEntrance(delayMillis = 500) {
                Button(
                    onClick = { onNavigateToTab("contact") },
                    colors = ButtonDefaults.buttonColors(containerColor = themeAccent),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("home_contact_cta"),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Get In Touch",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
