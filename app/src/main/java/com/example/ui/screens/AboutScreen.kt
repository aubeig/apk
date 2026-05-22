package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AnimatedEntrance
import com.example.ui.components.HeaderSyncBar
import com.example.ui.components.SkillBar
import com.example.ui.components.StatsCounter
import com.example.ui.components.glassmorphic
import com.example.ui.viewmodel.ExperienceItem
import com.example.ui.viewmodel.PortfolioViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AboutScreen(
    viewModel: PortfolioViewModel,
    modifier: Modifier = Modifier
) {
    val accentColor by viewModel.accentColor.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val themeAccent = Color(accentColor.hex)

    // Tracks which experience card index is expanded for description dropdowns
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Pull to refresh custom indicator bar
        item {
            HeaderSyncBar(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refreshData() },
                accentColor = themeAccent
            )
        }

        // Animated Statistics grid item
        item {
            AnimatedEntrance(delayMillis = 100) {
                Column {
                    Text(
                        text = "Professional Statistics",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Simple grid of stats columns
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            StatsCounter(
                                title = "Experience",
                                targetVal = viewModel.stats[0].targetVal,
                                displayValue = viewModel.stats[0].displayValue,
                                accentColor = themeAccent
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            StatsCounter(
                                title = "Projects Done",
                                targetVal = viewModel.stats[1].targetVal,
                                displayValue = viewModel.stats[1].displayValue,
                                accentColor = themeAccent
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            StatsCounter(
                                title = "Happy Clients",
                                targetVal = viewModel.stats[2].targetVal,
                                displayValue = viewModel.stats[2].displayValue,
                                accentColor = themeAccent
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            StatsCounter(
                                title = "GitHub Commits",
                                targetVal = viewModel.stats[3].targetVal,
                                displayValue = viewModel.stats[3].displayValue,
                                accentColor = themeAccent
                            )
                        }
                    }
                }
            }
        }

        // Skills Grouping block
        item {
            AnimatedEntrance(delayMillis = 200) {
                Column {
                    Text(
                        text = "Competencies & Skills",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    viewModel.skillGroups.forEach { group ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.1f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = group.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = themeAccent
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                group.skills.forEach { skill ->
                                    SkillBar(
                                        name = skill.name,
                                        targetValue = skill.level,
                                        iconName = skill.iconName,
                                        accentColor = themeAccent
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Experience History list banner
        item {
            AnimatedEntrance(delayMillis = 300) {
                Text(
                    text = "Professional Experience Timeline",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        itemsIndexed(viewModel.experiences) { index, experience ->
            val isExpanded = expandedStates[experience.id] ?: false
            
            AnimatedEntrance(delayMillis = 300 + (index * 100)) {
                ExperienceTimelineCard(
                    experience = experience,
                    isExpanded = isExpanded,
                    accentColor = themeAccent,
                    onToggleExpand = {
                        expandedStates[experience.id] = !isExpanded
                    }
                )
            }
        }

        // Add small structural spacer at bottom
        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExperienceTimelineCard(
    experience: ExperienceItem,
    isExpanded: Boolean,
    accentColor: Color,
    onToggleExpand: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = accentColor.copy(alpha = 0.12f))
            .clickable { onToggleExpand() }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .testTag("experience_card_${experience.id}")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = experience.role,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = experience.company,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = experience.period,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            // Expandable detail drawer
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = experience.description,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        experience.tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .background(accentColor.copy(alpha = 0.1f), CircleShape)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = accentColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
