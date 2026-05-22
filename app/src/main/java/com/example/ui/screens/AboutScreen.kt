package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AnimatedEntrance
import com.example.ui.components.glassmorphic
import com.example.ui.viewmodel.FileItem
import com.example.ui.viewmodel.PortfolioViewModel

@Composable
fun AboutScreen(
    viewModel: PortfolioViewModel,
    modifier: Modifier = Modifier
) {
    val files by viewModel.workspaceFiles.collectAsState()
    val activeEditingFile by viewModel.currentEditingFile.collectAsState()
    val editorContent by viewModel.editorContent.collectAsState()

    val accentColorState by viewModel.accentColor.collectAsState()
    val themeAccent = Color(accentColorState.hex)

    // State for create file dialog
    var showCreateDialog by remember { mutableStateOf(false) }
    var newFileName by remember { mutableStateOf("") }

    // Trigger sync on loading
    LaunchedEffect(Unit) {
        viewModel.refreshFiles()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // App header & File toolbar
        AnimatedEntrance(delayMillis = 100) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphic(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), cornerRadius = 14.dp)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Workspace Explorer",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Manage, edit and save local modules",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.refreshFiles() },
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(Icons.Default.Refresh, "Refresh Files", tint = themeAccent, modifier = Modifier.size(18.dp))
                    }

                    Button(
                        onClick = { showCreateDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = themeAccent),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("explorer_btn_new_file")
                    ) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New File", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Main Splits: Left Sidebar Files, Right Live Code Editor
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left Panel: Flat Local Files tree
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxHeight()
                    .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.1f), cornerRadius = 16.dp)
                    .testTag("sidebar_file_tree")
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        "LOCAL FILES",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = themeAccent,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    if (files.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No files in dir.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            items(files) { file ->
                                val isSelected = activeEditingFile?.path == file.path
                                val nodeBg = if (isSelected) themeAccent.copy(alpha = 0.15f) else Color.Transparent
                                val nodeTextWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(nodeBg)
                                        .clickable { viewModel.openFileInEditor(file) }
                                        .padding(horizontal = 8.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.Default.InsertDriveFile,
                                        contentDescription = "File Type",
                                        tint = if (file.isDirectory) themeAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = file.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 12.sp,
                                        fontWeight = nodeTextWeight,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        modifier = Modifier.weight(1f)
                                    )
                                    
                                    // Delete file trigger
                                    IconButton(
                                        onClick = { viewModel.deleteWorkspaceFile(file) },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, "Delete", tint = Color.Red.copy(alpha = 0.6f), modifier = Modifier.size(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Right Panel: Text editor workspace panel
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                modifier = Modifier
                    .weight(0.55f)
                    .fillMaxHeight()
                    .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.1f), cornerRadius = 16.dp)
                    .testTag("code_editor_panel")
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Editor Top Status Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Edit, "active file identifier", tint = themeAccent, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = activeEditingFile?.name ?: "No Native File Selected",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                modifier = Modifier.widthIn(max = 120.dp)
                            )
                        }

                        if (activeEditingFile != null) {
                            IconButton(
                                onClick = { viewModel.saveCurrentFile() },
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(themeAccent, RoundedCornerShape(6.dp))
                                    .testTag("save_current_file_button")
                            ) {
                                Icon(Icons.Default.Save, "Save changes", tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }
                    }

                    // Content Editor Input Stream
                    if (activeEditingFile == null) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CodeOff, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.size(40.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Select a local module from sidebar to start editing real-time code assets.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 12.dp))
                            }
                        }
                    } else {
                        TextField(
                            value = editorContent,
                            onValueChange = { viewModel.editorContent.value = it },
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("code_editor_textarea"),
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = Color.White
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            placeholder = { Text("Write code scripts...") }
                        )
                    }
                }
            }
        }

        // Dialog for adding file
        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                title = { Text("Initialize Workspace File") },
                text = {
                    Column {
                        Text("Add a custom local script target inside directory context e.g. tester.py or test.sh")
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = newFileName,
                            onValueChange = { newFileName = it },
                            placeholder = { Text("module_name.py") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = themeAccent
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("add_file_dialog_input")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.createWorkspaceFile(newFileName)
                            showCreateDialog = false
                            newFileName = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = themeAccent)
                    ) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
