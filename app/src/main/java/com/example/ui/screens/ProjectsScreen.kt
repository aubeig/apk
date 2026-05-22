package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.ui.components.AnimatedEntrance
import com.example.ui.components.glassmorphic
import com.example.ui.components.shimmerBrush
import com.example.ui.viewmodel.PortfolioViewModel
import com.example.ui.viewmodel.Project

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProjectsScreen(
    viewModel: PortfolioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val accentColor by viewModel.accentColor.collectAsState()
    val themeAccent = Color(accentColor.hex)

    val isLoading by viewModel.isLoading.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredProjects by viewModel.filteredProjects.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedProjectForDetails by remember { mutableStateOf<Project?>(null) }
    val detailsBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val projectsToDisplay = remember(filteredProjects, searchQuery) {
        if (searchQuery.trim().isEmpty()) {
            filteredProjects
        } else {
            filteredProjects.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true) ||
                it.techStack.any { tech -> tech.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Search and filter headers
        AnimatedEntrance(delayMillis = 100) {
            Column {
                Text(
                    text = "Project Catalog",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(10.dp))
                
                // Tech search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search projects or stacks (e.g. Compose)...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = themeAccent
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = themeAccent,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        focusedLabelColor = themeAccent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("project_search_bar")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Categories sliding capsule tab row
        AnimatedEntrance(delayMillis = 150) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("categories_row")
            ) {
                items(viewModel.categories) { cat ->
                    val isSelected = cat == selectedCategory
                    val bg = if (isSelected) themeAccent else MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    val textCol = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(bg)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                            .clickable {
                                viewModel.selectCategory(cat)
                            }
                            .padding(horizontal = 18.dp, vertical = 9.dp)
                            .testTag("category_pill_$cat"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = textCol
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Grid contents / Shimmer Skeletons
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(400)) },
            label = "loading_projects_transition",
            modifier = Modifier.weight(1f)
        ) { loading ->
            if (loading) {
                // Skeleton Loader of Grid placeholders
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(4) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            modifier = Modifier
                                .height(220.dp)
                                .glassmorphic(MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .background(shimmerBrush())
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .width(100.dp)
                                        .height(16.dp)
                                        .background(shimmerBrush())
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .fillMaxWidth(0.8f)
                                        .height(12.dp)
                                        .background(shimmerBrush())
                                )
                            }
                        }
                    }
                }
            } else {
                if (projectsToDisplay.isEmpty()) {
                    // Empty result view
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null,
                                tint = themeAccent.copy(alpha = 0.5f),
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No projects match your query.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    // Real project grid loading
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(projectsToDisplay, key = { _, proj -> proj.id }) { index, proj ->
                            AnimatedEntrance(delayMillis = index * 80) {
                                ProjectCard(
                                    project = proj,
                                    accentColor = themeAccent,
                                    onClick = { selectedProjectForDetails = proj }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal details sheet
    if (selectedProjectForDetails != null) {
        val proj = selectedProjectForDetails!!
        ModalBottomSheet(
            onDismissRequest = { selectedProjectForDetails = null },
            sheetState = detailsBottomSheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            scrimColor = Color.Black.copy(alpha = 0.65f),
            modifier = Modifier.testTag("project_bottom_sheet")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 40.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = proj.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { selectedProjectForDetails = null },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(Icons.Default.Close, "Dismiss details", modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                // Large modal image header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, themeAccent.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                ) {
                    SubcomposeAsyncImage(
                        model = proj.imageUrl,
                        contentDescription = proj.title,
                        loading = {
                            Box(modifier = Modifier.fillMaxSize().background(shimmerBrush()))
                        },
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Category: ${proj.category}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = themeAccent
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = proj.description,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tech Stack Used",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                
                // Wrap chips in dynamic FlowRow
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    proj.techStack.forEach { tech ->
                        Box(
                            modifier = Modifier
                                .background(themeAccent.copy(alpha = 0.12f), CircleShape)
                                .border(1.dp, themeAccent.copy(alpha = 0.25f), CircleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = tech,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = themeAccent
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                // GitHub Navigation clicker
                TextButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(proj.githubUrl))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(themeAccent, RoundedCornerShape(14.dp))
                        .testTag("project_github_redirect"),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Launch, null, tint = Color.White)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Explore Source Code",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectCard(
    project: Project,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
        ),
        modifier = Modifier
            .height(210.dp)
            .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = accentColor.copy(alpha = 0.12f))
            .clickable { onClick() }
            .testTag("project_card_${project.id}")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                SubcomposeAsyncImage(
                    model = project.imageUrl,
                    contentDescription = project.title,
                    loading = {
                        Box(modifier = Modifier.fillMaxSize().background(shimmerBrush()))
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Small overlay category tag
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = project.category,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
