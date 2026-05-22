package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppAccentColor
import com.example.data.AppThemeMode
import com.example.ui.components.AnimatedEntrance
import com.example.ui.components.glassmorphic
import com.example.ui.viewmodel.PortfolioViewModel

@Composable
fun SettingsScreen(
    viewModel: PortfolioViewModel,
    modifier: Modifier = Modifier
) {
    val activeTheme by viewModel.themeMode.collectAsState()
    val activeAccent by viewModel.accentColor.collectAsState()
    val themeAccent = Color(activeAccent.hex)

    val fontSize by viewModel.terminalFontSize.collectAsState()
    val termOpacity by viewModel.terminalOpacity.collectAsState()
    val isMatrix by viewModel.isMatrixBackground.collectAsState()
    val isParticle by viewModel.isParticleBackground.collectAsState()
    val cursorStyleState by viewModel.cursorStyle.collectAsState()
    val fontName by viewModel.terminalFontFamily.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(start = 14.dp, top = 14.dp, end = 14.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Settings greeting header
        AnimatedEntrance(delayMillis = 100) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphic(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), cornerRadius = 14.dp)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Workspace Preferences",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Personalize IDE skins, opacity, and background flows",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(themeAccent.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Settings, "Settings Core", tint = themeAccent, modifier = Modifier.size(18.dp))
                }
            }
        }

        // Section 1: Terminal Font & Sizing Parameters
        AnimatedEntrance(delayMillis = 150) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TextFields, "Typography selectors", tint = themeAccent, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Terminal Typography & Bounds", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Font Size stepper Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Standard Font Size", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Text("Current: ${fontSize}sp console size", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { if (fontSize > 10) viewModel.terminalFontSize.value = fontSize - 1 },
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp))
                                    .testTag("btn_zoom_out")
                            ) {
                                Icon(Icons.Default.Remove, "zoom out", tint = themeAccent, modifier = Modifier.size(16.dp))
                            }

                            Text(fontSize.toString(), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

                            IconButton(
                                onClick = { if (fontSize < 24) viewModel.terminalFontSize.value = fontSize + 1 },
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp))
                                    .testTag("btn_zoom_in")
                            ) {
                                Icon(Icons.Default.Add, "zoom in", tint = themeAccent, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Font Family switcher
                    Text("Select Monospace Font Picker", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = themeAccent)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("JetBrains Mono", "Fira Code", "Monospace").forEach { f ->
                            val isSelected = fontName == f
                            val btnBg = if (isSelected) themeAccent else Color.Transparent
                            val btnTextColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(btnBg)
                                    .clickable { viewModel.terminalFontFamily.value = f }
                                    .padding(vertical = 8.dp)
                                    .testTag("font_family_$f"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(f, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = btnTextColor)
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Terminal Background Glow & Animation Canvas controls
        AnimatedEntrance(delayMillis = 200) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Brush, "Background overlays customization", tint = themeAccent, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Interactive Skins & Overlays", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Opacity Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Console Grid Transparency", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Text("${(termOpacity * 100).toInt()}% opacity", style = MaterialTheme.typography.labelSmall, color = themeAccent)
                        }

                        Slider(
                            value = termOpacity,
                            onValueChange = { viewModel.terminalOpacity.value = it },
                            valueRange = 0.3f..1.0f,
                            colors = SliderDefaults.colors(
                                thumbColor = themeAccent,
                                activeTrackColor = themeAccent,
                                inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("terminal_opacity_slider")
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Matrix Flow Background Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Aesthetic Matrix Rain Overlay", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Text("Render procedurally falling falling green codes", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = isMatrix,
                            onCheckedChange = {
                                viewModel.isMatrixBackground.value = it
                                if (it) viewModel.isParticleBackground.value = false
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = themeAccent,
                                checkedTrackColor = themeAccent.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier.testTag("switch_matrix_rain")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Falling Particles Background Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Glowing Cosmic Particles Overlay", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Text("Proc background with flowing vector stars", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = isParticle,
                            onCheckedChange = {
                                viewModel.isParticleBackground.value = it
                                if (it) viewModel.isMatrixBackground.value = false
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = themeAccent,
                                checkedTrackColor = themeAccent.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier.testTag("switch_particle_rain")
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Cursor blink toggler Row
                    Text("Terminal Cursor Style Indicator", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = themeAccent)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("BLOCK GLO", "UNDERLINE", "RETRO LINE").forEach { cursor ->
                            val isSelected = cursorStyleState == cursor
                            val cellBg = if (isSelected) themeAccent else Color.Transparent
                            val cellTextColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(cellBg)
                                    .clickable { viewModel.cursorStyle.value = cursor }
                                    .padding(vertical = 8.dp)
                                    .testTag("cursor_style_$cursor"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(cursor, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = cellTextColor)
                            }
                        }
                    }
                }
            }
        }

        // Section 3: App Themes & Colors config
        AnimatedEntrance(delayMillis = 250) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DarkMode, null, tint = themeAccent, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Theme Mode & Branding Accents", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Capsules
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AppThemeMode.entries.forEach { mode ->
                            val isSelected = mode == activeTheme
                            val bgCol = if (isSelected) themeAccent else Color.Transparent
                            val txtCol = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bgCol)
                                    .clickable { viewModel.setThemeMode(mode) }
                                    .padding(vertical = 10.dp)
                                    .testTag("settings_theme_${mode.name}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(mode.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = txtCol)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Accents spheres row
                    Text("Select Primary Accent Brand Color", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = themeAccent)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppAccentColor.entries.forEach { accent ->
                            val isSelected = accent == activeAccent
                            val colValue = Color(accent.hex)

                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) colValue.copy(alpha = 0.3f) else Color.Transparent)
                                    .padding(if (isSelected) 4.dp else 0.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .clip(CircleShape)
                                    .background(colValue)
                                    .clickable { viewModel.setAccentColor(accent) }
                                    .testTag("accent_color_picker_${accent.name}")
                            )
                        }
                    }
                }
            }
        }

        // Section 4: Host System Specs
        AnimatedEntrance(delayMillis = 300) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.08f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = themeAccent, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Specification Details", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val infoList = listOf(
                        Pair("Application Name", "NeoTerm Cloud Terminal IDE"),
                        Pair("Integration Kernel", "Antigravity VM Sandbox Engine"),
                        Pair("Design Specification", "Material Design 3 Glassmorph Icons"),
                        Pair("Compiler Core Status", "ARM64 Pre-compiled Binaries Verified")
                    )

                    infoList.forEach { info ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(info.first, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(info.second, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
