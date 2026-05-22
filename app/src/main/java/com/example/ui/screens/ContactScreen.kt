package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AnimatedEntrance
import com.example.ui.components.glassmorphic
import com.example.ui.viewmodel.ContactFormState
import com.example.ui.viewmodel.PortfolioViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContactScreen(
    viewModel: PortfolioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val accentColor by viewModel.accentColor.collectAsState()
    val themeAccent = Color(accentColor.hex)

    val contactName by viewModel.contactName.collectAsState()
    val contactEmail by viewModel.contactEmail.collectAsState()
    val contactMessage by viewModel.contactMessage.collectAsState()
    val contactFormState by viewModel.contactFormState.collectAsState()

    val scrollState = rememberScrollState()

    val professionalEmail = "samantha.sterling@dev.com"

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Contact header info
        AnimatedEntrance(delayMillis = 100) {
            Column {
                Text(
                    text = "Let's Collaborate",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Whether you have an interesting job opportunity, want to discuss software scaling, or just want to network.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }

        // Direct Email Clipboard Card
        AnimatedEntrance(delayMillis = 150) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.15f))
                    .clickable {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("E-Mail Address", professionalEmail)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "E-Mail copied to clipboard!", Toast.LENGTH_SHORT).show()
                    }
                    .testTag("contact_email_block")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(18.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(themeAccent.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AlternateEmail, null, tint = themeAccent, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Primary Professional Mail",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = professionalEmail,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Mail Address",
                        tint = themeAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Social Networks Grid banner Row
        AnimatedEntrance(delayMillis = 200) {
            Column {
                Text(
                    text = "Developer Social Channels",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(10.dp))
                
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val socials = listOf(
                        SocialItem("GitHub", "https://github.com"),
                        SocialItem("LinkedIn", "https://linkedin.com"),
                        SocialItem("Twitter", "https://twitter.com"),
                        SocialItem("Medium", "https://medium.com")
                    )

                    socials.forEach { soc ->
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(soc.url))
                                context.startActivity(intent)
                            },
                            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                                brush = Brush.linearGradient(colors = listOf(themeAccent.copy(alpha = 0.4f), themeAccent))
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("social_redirect_${soc.label}")
                        ) {
                            Text(
                                text = soc.label,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = themeAccent
                            )
                        }
                    }
                }
            }
        }

        // Contact Form Block Card
        AnimatedEntrance(delayMillis = 250) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphic(MaterialTheme.colorScheme.surface, borderColor = themeAccent.copy(alpha = 0.1f))
                    .testTag("contact_form_card")
            ) {
                AnimatedContent(
                    targetState = contactFormState,
                    transitionSpec = { fadeIn(tween(350)) togetherWith fadeOut(tween(350)) },
                    label = "contact_form_animation"
                ) { state ->
                    when (state) {
                        is ContactFormState.Success -> {
                            // High Fidelity Success feedback block
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(
                                            Brush.radialGradient(
                                                colors = listOf(
                                                    themeAccent.copy(alpha = 0.2f),
                                                    Color.Transparent
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Success",
                                        tint = themeAccent,
                                        modifier = Modifier.size(44.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Talk to you soon!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Your connection message has bypassed filtering and landed in my secure mailbox. I'll get back to you within 24 hours.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    onClick = { viewModel.resetContactState() },
                                    colors = ButtonDefaults.buttonColors(containerColor = themeAccent),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Send Another Message", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        is ContactFormState.Sending -> {
                            // Pulsing / Rotating Sending progress indicator
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = themeAccent,
                                    strokeWidth = 4.dp,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "Transmitting Message securely...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = themeAccent
                                )
                            }
                        }
                        else -> {
                            // Render Form Inputs (Idle / Error states)
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Send a Secure Message",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                // Inline validation banner on validation error
                                if (state is ContactFormState.Error) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                                                RoundedCornerShape(10.dp)
                                            )
                                            .border(
                                                1.dp,
                                                MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                                                RoundedCornerShape(10.dp)
                                            )
                                            .padding(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ErrorOutline,
                                            contentDescription = "Alert",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = state.message,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                }

                                // Name filled input
                                OutlinedTextField(
                                    value = contactName,
                                    onValueChange = { viewModel.contactName.value = it },
                                    label = { Text("Your Name") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Person, null, tint = themeAccent.copy(alpha = 0.6f))
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = themeAccent,
                                        focusedLabelColor = themeAccent
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("contact_name_input")
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Email Input
                                OutlinedTextField(
                                    value = contactEmail,
                                    onValueChange = { viewModel.contactEmail.value = it },
                                    label = { Text("Your Email") },
                                    leadingIcon = {
                                        Icon(Icons.Default.AlternateEmail, null, tint = themeAccent.copy(alpha = 0.6f))
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = themeAccent,
                                        focusedLabelColor = themeAccent
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("contact_email_input")
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Message input
                                OutlinedTextField(
                                    value = contactMessage,
                                    onValueChange = { viewModel.contactMessage.value = it },
                                    label = { Text("Your Message") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Message, null, tint = themeAccent.copy(alpha = 0.6f))
                                    },
                                    minLines = 4,
                                    maxLines = 6,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = themeAccent,
                                        focusedLabelColor = themeAccent
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("contact_message_input")
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                // Submit action
                                Button(
                                    onClick = { viewModel.submitContactForm() },
                                    colors = ButtonDefaults.buttonColors(containerColor = themeAccent),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("contact_submit_btn")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(Icons.Default.Send, null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Send Connection Message", fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}

data class SocialItem(val label: String, val url: String)
