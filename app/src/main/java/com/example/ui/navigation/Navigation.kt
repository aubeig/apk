package com.example.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.glassmorphic
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.viewmodel.PortfolioViewModel

data class NavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun AppNavigation(
    viewModel: PortfolioViewModel,
    modifier: Modifier = Modifier
) {
    var activeRoute by remember { mutableStateOf("home") }
    val accentColor by viewModel.accentColor.collectAsState()
    val themeAccentByState = Color(accentColor.hex)

    val navBarItems = remember {
        listOf(
            NavItem("home", "Home", Icons.Default.Home, Icons.Default.Home, "nav_home"),
            NavItem("about", "About", Icons.Default.Person, Icons.Default.PersonOutline, "nav_about"),
            NavItem("projects", "Projects", Icons.Default.BusinessCenter, Icons.Default.BusinessCenter, "nav_projects"),
            NavItem("contact", "Contact", Icons.Default.Email, Icons.Default.Email, "nav_contact"),
            NavItem("settings", "Settings", Icons.Default.Settings, Icons.Default.Settings, "nav_settings")
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            // Floating Premium Bottom Navigation Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding() // CRITICAL: Avoid software overlay clipping
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassmorphic(
                            backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                            borderColor = themeAccentByState.copy(alpha = 0.2f),
                            cornerRadius = 24.dp,
                            elevation = 12.dp
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .testTag("floating_navigation_bar"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navBarItems.forEach { navItem ->
                        val isSelected = navItem.route == activeRoute
                        val tabColor = if (isSelected) themeAccentByState else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
                        
                        // Animated scale spring effect
                        val scaleAnim by animateFloatAsState(
                            targetValue = if (isSelected) 1.15f else 1.0f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium),
                            label = "tabScaleAnimation"
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    activeRoute = navItem.route
                                }
                                .padding(vertical = 4.dp, horizontal = 10.dp)
                                .scale(scaleAnim)
                                .testTag(navItem.testTag)
                        ) {
                            Icon(
                                imageVector = if (isSelected) navItem.selectedIcon else navItem.unselectedIcon,
                                contentDescription = navItem.label,
                                tint = tabColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = navItem.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                fontSize = 10.sp,
                                color = tabColor
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // Render screen content using premium crossfade animations
        Crossfade(
            targetState = activeRoute,
            animationSpec = tween(400),
            label = "nav_screen_crossfade",
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding()) // Account for system heights
        ) { route ->
            when (route) {
                "home" -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToTab = { target -> activeRoute = target }
                )
                "about" -> AboutScreen(viewModel = viewModel)
                "projects" -> ProjectsScreen(viewModel = viewModel)
                "contact" -> ContactScreen(viewModel = viewModel)
                "settings" -> SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
