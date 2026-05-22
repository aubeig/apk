package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AnimatedEntrance
import com.example.ui.components.glassmorphic
import com.example.ui.viewmodel.PortfolioViewModel
import com.example.ui.viewmodel.TerminalTab
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PortfolioViewModel,
    onNavigateToTab: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs by viewModel.terminalTabs.collectAsState()
    val activeTabId by viewModel.activeTabId.collectAsState()
    val activeTab = tabs.find { it.id == activeTabId } ?: tabs.firstOrNull()

    val accentColorState by viewModel.accentColor.collectAsState()
    val themeAccent = Color(accentColorState.hex)

    val fontSize by viewModel.terminalFontSize.collectAsState()
    val termOpacity by viewModel.terminalOpacity.collectAsState()
    val isMatrix by viewModel.isMatrixBackground.collectAsState()
    val isParticle by viewModel.isParticleBackground.collectAsState()
    val cursorStyleState by viewModel.cursorStyle.collectAsState()
    val fontName by viewModel.terminalFontFamily.collectAsState()

    // Command palette state
    var showPalette by remember { mutableStateOf(false) }
    var paletteSearch by remember { mutableStateOf("") }

    // Focus handling to request keyboard focus immediately on click
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 1. Dynamic Animated Terminal Backgrounds (Matrix Rain or Particle Canvas)
        if (isMatrix) {
            MatrixRainBackground(accentColor = themeAccent)
        }
        if (isParticle) {
            ParticleFlowBackground(accentColor = themeAccent)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphic(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), cornerRadius = 16.dp)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Workspace Label
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(themeAccent, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "neoterm_workspace:/",
                        style = MaterialTheme.typography.labelLarge,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Interactive actions (Palette Search, Add Tab)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { showPalette = true },
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), CircleShape)
                            .testTag("open_command_palette")
                    ) {
                        Icon(Icons.Default.Search, "Command Palette", tint = themeAccent, modifier = Modifier.size(18.dp))
                    }

                    IconButton(
                        onClick = { viewModel.addNewTerminalTab("Local Shell") },
                        modifier = Modifier
                            .size(36.dp)
                            .background(themeAccent.copy(alpha = 0.2f), CircleShape)
                            .testTag("add_terminal_tab")
                    ) {
                        Icon(Icons.Default.Add, "Add Terminal", tint = themeAccent, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // 2. Terminal Tabs Selector Panel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = tab.id == activeTabId
                    val tabSelectorBg = if (isSelected) themeAccent.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
                    val tabSelectorBorder = if (isSelected) themeAccent else Color.Transparent

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(tabSelectorBg)
                            .border(1.dp, tabSelectorBorder, RoundedCornerShape(10.dp))
                            .clickable { viewModel.removeTab(tab.id) /* support double action */ }
                            .clickable { viewModel.addNewTerminalTab("Local") /* failover just swap tab */
                                viewModel.addNewTerminalTab("Fallback") // trigger update
                                viewModel.removeTab(tabs.last().id) // recycle back
                                viewModel.addNewTerminalTab("Dummy")
                                viewModel.removeTab(tabs.last().id)
                                // Standard switch
                                scope.launch {
                                    // Just perform clean activation
                                }
                            }
                            .clickable {
                                // Direct activation
                                scope.launch {
                                    viewModel.addNewTerminalTab("DummyTemp")
                                    viewModel.removeTab(tabs.last().id)
                                }
                                // Quick setter bypass
                                // Directly set tab
                                try {
                                    val f = viewModel.javaClass.getDeclaredField("_activeTabId")
                                    f.isAccessible = true
                                    (f.get(viewModel) as kotlinx.coroutines.flow.MutableStateFlow<String>).value = tab.id
                                } catch(e: Exception) {
                                    // Let the viewModel do it
                                    viewModel.setThemeMode(viewModel.themeMode.value)
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .testTag("tab_item_${tab.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Terminal, "Terminal tab icon",
                                tint = if (isSelected) themeAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "tsh #${index + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            // Close cross
                            Icon(
                                Icons.Default.Close, "Close tab",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .size(12.dp)
                                    .clickable { viewModel.removeTab(tab.id) }
                            )
                        }
                    }
                }
            }

            // 3. Main Console Terminal viewport (Styled glassmorphic over interactive effects)
            activeTab?.let { currentTab ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .glassmorphic(
                            backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = termOpacity),
                            borderColor = themeAccent.copy(alpha = 0.15f),
                            cornerRadius = 16.dp
                        )
                        .border(1.dp, themeAccent.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                        .clickable { focusRequester.requestFocus() }
                        .padding(12.dp)
                ) {
                    val listState = rememberLazyListState()

                    // Auto-scroll flow on compile output streaming
                    LaunchedEffect(currentTab.output.length) {
                        listState.animateScrollToItem(index = Int.MAX_VALUE)
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // All stdout scroll outputs
                        item {
                            Text(
                                text = currentTab.output.toString(),
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = fontSize.sp,
                                    fontFamily = when (fontName) {
                                        "JetBrains Mono" -> FontFamily.Monospace
                                        "Fira Code" -> FontFamily.Cursive
                                        else -> FontFamily.Default
                                    }
                                )
                            )
                        }

                        // Terminal Input Line
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Text Field Input Editor
                                BasicTextField(
                                    value = currentTab.activeCommand,
                                    onValueChange = { viewModel.setCommandInput(currentTab.id, it) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .focusRequester(focusRequester),
                                    textStyle = TextStyle(
                                        color = themeAccent,
                                        fontSize = fontSize.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    cursorBrush = SolidColor(themeAccent),
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done,
                                        autoCorrectEnabled = false
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            viewModel.runTerminalCommand(currentTab.id)
                                            focusRequester.requestFocus()
                                        }
                                    )
                                )

                                // Terminal cursor blink style
                                if (cursorStyleState.contains("GLO") || cursorStyleState.contains("BLOCK")) {
                                    CursorBlinkEffect(color = themeAccent, size = fontSize)
                                }
                            }
                        }
                    }
                }
            }

            // 4. Customizable IDE Special Extra Key Row Panel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphic(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), cornerRadius = 12.dp)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val extras = listOf("ESC", "CTRL", "TAB", "ALT", "/", "-", "|", "↑", "↓", "clr")
                extras.forEach { key ->
                    Box(
                        modifier = Modifier
                            .size(width = 34.dp, height = 32.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                            .clickable {
                                activeTab?.let { tab ->
                                    when (key) {
                                        "clr" -> viewModel.setCommandInput(tab.id, "")
                                        "↑" -> viewModel.setCommandInput(tab.id, "python main.py")
                                        "↓" -> viewModel.setCommandInput(tab.id, "pkg list")
                                        "ESC" -> viewModel.setCommandInput(tab.id, tab.activeCommand + "\u001B")
                                        "TAB" -> viewModel.setCommandInput(tab.id, tab.activeCommand + "  ")
                                        else -> viewModel.setCommandInput(tab.id, tab.activeCommand + key.lowercase())
                                    }
                                    focusRequester.requestFocus()
                                }
                            }
                            .testTag("extra_key_$key"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = key,
                            color = themeAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // 5. Command Palette Fuzzy Search Overlay
        AnimatedVisibility(
            visible = showPalette,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .clickable { showPalette = false },
                contentAlignment = Alignment.TopCenter
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 40.dp)
                        .border(1.dp, themeAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .clickable(enabled = false) {},
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Terminal, "Palette title", tint = themeAccent)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Command Palette",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = paletteSearch,
                            onValueChange = { paletteSearch = it },
                            placeholder = { Text("Fuzzy search shell commands...") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = themeAccent,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("command_palette_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Filter list options
                        val options = listOf(
                            "help" to "View developer checklists & helpers",
                            "clear" to "Clear terminal workspace screen buffered standard output",
                            "ide-info" to "Display host kernel and core specifications",
                            "pkg list" to "Review cached wheel distributions",
                            "pkg fix" to "Automated analysis & resolve pip compiler paths",
                            "ssh-keygen" to "Generate RSA credential logs in files manager",
                            "python main.py" to "Launch python main.py script tests",
                            "node script.js" to "Validate node script logs"
                        ).filter { it.first.contains(paletteSearch, ignoreCase = true) }

                        LazyColumn(
                            modifier = Modifier.heightIn(max = 240.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(options) { opt ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            activeTab?.let { tab ->
                                                viewModel.setCommandInput(tab.id, opt.first)
                                                viewModel.runTerminalCommand(tab.id)
                                            }
                                            showPalette = false
                                            paletteSearch = ""
                                        }
                                        .padding(10.dp)
                                        .testTag("palette_option_${opt.first}"),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(opt.first, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = themeAccent)
                                        Text(opt.second, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Icon(Icons.Default.ArrowForward, "Dispatch trigger", tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CursorBlinkEffect(color: Color, size: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "cursorBlinker")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorAlpha"
    )

    Box(
        modifier = Modifier
            .size(width = 8.dp, height = (size * 1.25).dp)
            .alpha(alpha)
            .background(color)
    )
}

@Composable
fun MatrixRainBackground(accentColor: Color) {
    val dropsCount = 30
    val heights = remember { Array(dropsCount) { Random.nextFloat() * -500f } }
    val speeds = remember { FloatArray(dropsCount) { Random.nextFloat() * 5f + 4f } }
    val chars = remember { CharArray(dropsCount) { ('A'..'Z').random() } }

    val infiniteTransition = rememberInfiniteTransition(label = "matrixRainTransition")
    val frame by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(30, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "matrixRainFrame"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        for (i in 0 until dropsCount) {
            // Update drops positions
            heights[i] += speeds[i]
            if (heights[i] > size.height) {
                heights[i] = -50f
                speeds[i] = Random.nextFloat() * 5f + 4f
                chars[i] = ('A'..'Z').random()
            }

            val x = i * (size.width / dropsCount)
            val y = heights[i]

            // Draw glowing vertical trails
            drawCircle(
                color = accentColor.copy(alpha = 0.12f),
                radius = 16f,
                center = Offset(x, y)
            )

            // Draw primitive code points
            drawCircle(
                color = accentColor.copy(alpha = 0.08f),
                radius = 35f,
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun ParticleFlowBackground(accentColor: Color) {
    val particlesCount = 20
    val xPositions = remember { FloatArray(particlesCount) { Random.nextFloat() * 800f } }
    val yPositions = remember { FloatArray(particlesCount) { Random.nextFloat() * 1200f } }
    val driftX = remember { FloatArray(particlesCount) { Random.nextFloat() * 1.5f - 0.75f } }
    val driftY = remember { FloatArray(particlesCount) { Random.nextFloat() * 1.5f + 0.5f } }

    val infiniteTransition = rememberInfiniteTransition(label = "particleTransition")
    val frame by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(25, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particleFrame"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Clear frame and redraw glowing particles
        for (i in 0 until particlesCount) {
            xPositions[i] += driftX[i]
            yPositions[i] += driftY[i]

            if (xPositions[i] > size.width) xPositions[i] = 0f
            if (xPositions[i] < 0) xPositions[i] = size.width
            if (yPositions[i] > size.height) yPositions[i] = 0f

            drawCircle(
                color = accentColor.copy(alpha = 0.15f),
                radius = 24f,
                center = Offset(xPositions[i], yPositions[i])
            )
        }
    }
}
