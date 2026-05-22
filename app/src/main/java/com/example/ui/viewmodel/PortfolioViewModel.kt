package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppAccentColor
import com.example.data.AppThemeMode
import com.example.data.ThemePreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// Data Models
data class Project(
    val id: String,
    val title: String,
    val description: String,
    val category: String, // "Mobile", "Web", "Design"
    val imageUrl: String,
    val githubUrl: String,
    val techStack: List<String>,
    val featured: Boolean = false
)

data class SkillItem(
    val name: String,
    val level: Float, // 0.0f to 1.0f
    val iconName: String
)

data class SkillGroup(
    val title: String,
    val skills: List<SkillItem>
)

data class StatItem(
    val title: String,
    val displayValue: String,
    val targetVal: Int
)

data class ExperienceItem(
    val id: String,
    val company: String,
    val role: String,
    val period: String,
    val description: String,
    val tags: List<String>
)

sealed interface ContactFormState {
    data object Idle : ContactFormState
    data object Sending : ContactFormState
    data object Success : ContactFormState
    data class Error(val message: String) : ContactFormState
}

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {

    private val themePrefs = ThemePreferences(application)

    // Theme states
    val themeMode: StateFlow<AppThemeMode> = themePrefs.themeModeFlow.stateInScope(AppThemeMode.SYSTEM)
    val accentColor: StateFlow<AppAccentColor> = themePrefs.accentColorFlow.stateInScope(AppAccentColor.PURPLE)

    private fun <T> kotlinx.coroutines.flow.Flow<T>.stateInScope(initialValue: T): StateFlow<T> {
        val flow = this
        val mutable = MutableStateFlow(initialValue)
        viewModelScope.launch {
            flow.collect { mutable.value = it }
        }
        return mutable.asStateFlow()
    }

    // Projects Category Filter
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Loading & Refreshing States
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Contact Form Inputs
    val contactName = MutableStateFlow("")
    val contactEmail = MutableStateFlow("")
    val contactMessage = MutableStateFlow("")
    
    private val _contactFormState = MutableStateFlow<ContactFormState>(ContactFormState.Idle)
    val contactFormState: StateFlow<ContactFormState> = _contactFormState.asStateFlow()

    // Toast/Feedback event emitter
    private val _feedbackChannel = MutableSharedFlow<String>()
    val feedbackChannel: SharedFlow<String> = _feedbackChannel.asSharedFlow()

    // Static Data
    val categories = listOf("All", "Mobile", "Web", "Design")

    val stats = listOf(
        StatItem("Years Experience", "4+", 4),
        StatItem("Projects Completed", "24+", 24),
        StatItem("Happy Clients", "15+", 15),
        StatItem("GitHub Contributions", "850+", 850)
    )

    val skillGroups = listOf(
        SkillGroup("Mobile & Architecture", listOf(
            SkillItem("Kotlin", 0.95f, "code"),
            SkillItem("Jetpack Compose", 0.90f, "brush"),
            SkillItem("Android SDK", 0.88f, "android"),
            SkillItem("Coroutines & Flow", 0.85f, "speed")
        )),
        SkillGroup("Backend & Services", listOf(
            SkillItem("Ktor & Spring", 0.75f, "dns"),
            SkillItem("Room & SQL", 0.82f, "storage"),
            SkillItem("Firebase Suite", 0.80f, "cloud"),
            SkillItem("REST & GraphQL", 0.85f, "api")
        )),
        SkillGroup("Design & Quality", listOf(
            SkillItem("Figma / UI Design", 0.80f, "palette"),
            SkillItem("Shaders & Canvas", 0.70f, "layers"),
            SkillItem("TDD & Testing", 0.78f, "check_circle"),
            SkillItem("CI / CD Pipelines", 0.72f, "build")
        ))
    )

    val experiences = listOf(
        ExperienceItem(
            "1",
            "Nexis Mobile Studio",
            "Senior Android Developer",
            "2024 - Present",
            "Architected high-fidelity dynamic client products utilizing modern M3 Jetpack Compose, state flow handlers, and clean micro-services pattern.",
            listOf("Compose", "M3", "Ktor", "Hilt")
        ),
        ExperienceItem(
            "2",
            "PixelVibe Agency",
            "Mobile UI Specialist",
            "2022 - 2024",
            "Designed and coded animations and interactive features using Canvas brushes, gesture systems, and advanced custom shaders for media companies.",
            listOf("UI/UX", "Canvas", "Animations", "Theme Design")
        ),
        ExperienceItem(
            "3",
            "ByteLogic Corp",
            "Software Engineer",
            "2021 - 2022",
            "Developed responsive native layouts and offline data stores using SQLite/Room database structures, improving client app reliability by 35%.",
            listOf("Android SDK", "Room", "Flow", "Java")
        )
    )

    private val allProjects = listOf(
        Project(
            "1",
            "AuraMusic Player",
            "A beautiful audio player with live visualizers, dynamic colors based on album art, and custom gesture controllers.",
            "Mobile",
            "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=500&q=80",
            "https://github.com/example/auramusic",
            listOf("Compose", "Audio", "Dynamic Theme", "Canvas"),
            featured = true
        ),
        Project(
            "2",
            "Zenith Task Engine",
            "Minimalist planner with staggering visual progress indicators, local persistence with Room, and Gemini-assisted summaries.",
            "Mobile",
            "https://images.unsplash.com/photo-1484480974693-6db0a0177a59?w=500&q=80",
            "https://github.com/example/zenith",
            listOf("Room", "Gemini", "AlarmManager", "M3"),
            featured = true
        ),
        Project(
            "3",
            "Synthetix Board",
            "Premium audio synthesizer canvas providing granular audio tuning and fluid high-fps interactive waveforms.",
            "Design",
            "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=500&q=80",
            "https://github.com/example/synthetix",
            listOf("Canvas", "Synthesizer", "Audio Synthesis", "Kotlin"),
            featured = false
        ),
        Project(
            "4",
            "Vortex Admin",
            "A comprehensive client command center featuring interactive analytics charts, live web sockets, and system monitors.",
            "Web",
            "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=500&q=80",
            "https://github.com/example/vortex",
            listOf("React", "Ktor WebSockets", "D3 Charts", "Tailwind"),
            featured = false
        ),
        Project(
            "5",
            "Cosmos Explorer",
            "Educational galaxy mapping app highlighting procedural space particle generation and celestial orbits.",
            "Design",
            "https://images.unsplash.com/photo-1506318137071-a8e063b4bec0?w=500&q=80",
            "https://github.com/example/cosmos",
            listOf("Custom Canvas", "Physics Engine", "Animations"),
            featured = true
        ),
        Project(
            "6",
            "Stratos Chat",
            "Realtime end-to-end encrypted messaging engine incorporating modern design with typing state feedback indicators.",
            "Mobile",
            "https://images.unsplash.com/photo-1611746872915-64382b5c76da?w=500&q=80",
            "https://github.com/example/stratos",
            listOf("WebSocket", "AES Encryption", "Room", "Sub_Compose"),
            featured = false
        )
    )

    private val _filteredProjects = MutableStateFlow<List<Project>>(allProjects)
    val filteredProjects: StateFlow<List<Project>> = _filteredProjects.asStateFlow()

    init {
        viewModelScope.launch {
            // Initial loading simulation for premium feel skeleton screen
            delay(1200)
            _isLoading.value = false
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        filterProjects()
    }

    private fun filterProjects() {
        val cat = _selectedCategory.value
        _filteredProjects.value = if (cat == "All") {
            allProjects
        } else {
            allProjects.filter { it.category.equals(cat, ignoreCase = true) }
        }
    }

    fun refreshData() {
        if (_isRefreshing.value) return
        viewModelScope.launch {
            _isRefreshing.value = true
            _feedbackChannel.emit("Refreshing portfolio contents...")
            delay(1800) // Simulate refresh pulling delays
            _isRefreshing.value = false
            _feedbackChannel.emit("Portfolio synced successfully!")
        }
    }

    fun setThemeMode(mode: AppThemeMode) {
        viewModelScope.launch {
            themePrefs.setThemeMode(mode)
        }
    }

    fun setAccentColor(color: AppAccentColor) {
        viewModelScope.launch {
            themePrefs.setAccentColor(color)
            _feedbackChannel.emit("Accent accent customized to ${color.nameLabel}")
        }
    }

    fun submitContactForm() {
        val name = contactName.value.trim()
        val email = contactEmail.value.trim()
        val message = contactMessage.value.trim()

        if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
            viewModelScope.launch {
                _contactFormState.value = ContactFormState.Error("Please complete all sections.")
                _feedbackChannel.emit("All section inputs are required!")
            }
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            viewModelScope.launch {
                _contactFormState.value = ContactFormState.Error("Please provide a valid email format.")
                _feedbackChannel.emit("Unable to parse email.")
            }
            return
        }

        viewModelScope.launch {
            _contactFormState.value = ContactFormState.Sending
            delay(2000) // Simulate transmission delay for quality state feel
            _contactFormState.value = ContactFormState.Success
            _feedbackChannel.emit("Message delivered successfully! Checking mailbox soon.")

            // Clear inputs on success
            contactName.value = ""
            contactEmail.value = ""
            contactMessage.value = ""
        }
    }

    fun resetContactState() {
        _contactFormState.value = ContactFormState.Idle
    }
}
