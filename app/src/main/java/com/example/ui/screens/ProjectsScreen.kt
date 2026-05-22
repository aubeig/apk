package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.components.AnimatedEntrance
import com.example.ui.components.glassmorphic
import com.example.ui.viewmodel.PackageInstallState
import com.example.ui.viewmodel.PortfolioViewModel

@Composable
fun ProjectsScreen(
    viewModel: PortfolioViewModel,
    modifier: Modifier = Modifier
) {
    val installedWheels by viewModel.installedWheels.collectAsState()
    val packageState by viewModel.packageState.collectAsState()
    val accentColorState by viewModel.accentColor.collectAsState()
    val themeAccent = Color(accentColorState.hex)

    val packagesList = listOf(
        Pair("pydantic-core", "Required by fast-validation models; compiles native C/Rust speed bindings"),
        Pair("numpy", "Core multidimensional array & matrix linear algebra computations binary"),
        Pair("pandas", "Advanced high-performance data manipulation constructs (C-optimized charts)"),
        Pair("scipy", "Scientific integrations, differential solver matrices & signal compilers")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // App header & Terminal info
        item {
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
                            "Compilers & Packages",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Build native Rust/C++ pre-compiled wheels for termux",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(themeAccent.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .border(1.dp, themeAccent.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text("HOST: ARM64", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = themeAccent)
                    }
                }
            }
        }

        // Section 1: Compilation output telemetry frame (Displays live rust bindings outputs)
        item {
            AnimatedEntrance(delayMillis = 200) {
                when (val state = packageState) {
                    is PackageInstallState.Idle -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.1f))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Build, null, tint = themeAccent.copy(alpha = 0.6f), modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Awaiting Wheel Compilation Request", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text("Target package and click compiler below to build wheel outputs locally.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    is PackageInstallState.Installing -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.9f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, themeAccent, RoundedCornerShape(12.dp))
                                .testTag("installer_compiling_panel")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "🔨 Compiling: ${state.packageName}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontFamily = FontFamily.Monospace,
                                        color = themeAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "${(state.progress * 100).toInt()}%",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color.Green
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                LinearProgressIndicator(
                                    progress = { state.progress },
                                    color = themeAccent,
                                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    modifier = Modifier.fillMaxWidth().height(4.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Log stream box
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .background(Color(0xFF07050A))
                                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = state.log,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color.Green,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                    is PackageInstallState.Success -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Green, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.Green.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CheckCircle, null, tint = Color.Green, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Compilation Successful!", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.Green)
                                    Text(state.message, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                    is PackageInstallState.Error -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Red, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.Red.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Error, null, tint = Color.Red, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Build pipeline warning", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.Red)
                                    Text(state.message, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Compiling triggers package listing list
        item {
            AnimatedEntrance(delayMillis = 300) {
                Text(
                    "PRE-COMPILED PYTHON WHEELS MANAGER",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = themeAccent,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        items(packagesList) { pkg ->
            val isInstalled = installedWheels.contains(pkg.first)
            
            AnimatedEntrance(delayMillis = 350) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.1f))
                        .testTag("package_card_${pkg.first}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = pkg.first,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (isInstalled) Color.Green.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                            RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isInstalled) "INSTALLED" else "SOURCE",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isInstalled) Color.Green else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = pkg.second,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = { viewModel.compilePackageWheel(pkg.first) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isInstalled) themeAccent.copy(alpha = 0.2f) else themeAccent
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            enabled = packageState !is PackageInstallState.Installing,
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("btn_build_${pkg.first}")
                        ) {
                            Text(
                                text = if (isInstalled) "RECOMPILE" else "COMPILE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isInstalled) themeAccent else Color.White
                            )
                        }
                    }
                }
            }
        }

        // Section 3: Environment VM Diagnostics panel
        item {
            AnimatedEntrance(delayMillis = 400) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.12f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(themeAccent.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Dns, "VM details monitor", tint = themeAccent, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Docker / Host SDK VM Config", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                                    Text("Target compilers status trackers", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val indicators = listOf(
                            Triple("Node.js Env V18", "Pre-configured Yarn / PNPM", true),
                            Triple("Python virtualenv", "Auto-isolate script context", true),
                            Triple("Rust Compiler (cargo)", "v1.75.0 targets enabled", true),
                            Triple("Pre-compiled Wheel Cache", "pip download buffer paths", true)
                        )

                        indicators.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item.first, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(item.second, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(if (item.third) Color.Green else Color.Red, CircleShape)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Fix pipeline conflicts diagnostic trigger
                        Button(
                            onClick = {
                                viewModel.addNewTerminalTab("Local Shell") // fall over bypass
                                viewModel.removeTab(viewModel.terminalTabs.value.last().id)
                                // Add command triggers
                                viewModel.setCommandInput(viewModel.terminalTabs.value.first().id, "pkg fix")
                                viewModel.runTerminalCommand(viewModel.terminalTabs.value.first().id)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            border = androidx.compose.foundation.BorderStroke(1.dp, themeAccent.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .testTag("btn_pipeline_repair_fix")
                        ) {
                            Text("🔧 Auto-Resolve Compiler Conflict (Run Diagnostics)", fontSize = 12.sp, color = themeAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
