package com.skillMatcher.buildMate.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.skillMatcher.buildMate.data.entities.UserSkillEntity
import com.skillMatcher.buildMate.presentation.nav.Routes
import com.skillMatcher.buildMate.viewmodel.MyViewModel

@Composable
fun SkillScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController,
    firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    val userId = remember { firebaseAuth.currentUser?.uid ?: "unknown_user" }
    
    // Form states
    val techStackState = remember { mutableStateOf("") }
    val experienceLevelState = remember { mutableStateOf("") }
    val goalState = remember { mutableStateOf("") }
    val additionalNotesState = remember { mutableStateOf("") }
    val librariesState = remember { mutableStateListOf<String>() }
    val currentLibraryState = remember { mutableStateOf("") }
    
    // UI states
    val isGenerating = remember { mutableStateOf(false) }
    
    // ViewModel states
    val generateIdeaState = viewModel.generateIdeaState.collectAsState()
    val currentIdea = viewModel.currentIdea.collectAsState()

    LaunchedEffect(currentIdea.value) {
        if (currentIdea.value != null && currentIdea.value!!.ideaId > 0) {
            android.util.Log.d("SkillScreen", "Current idea ID: ${currentIdea.value?.ideaId}, navigating to IdeaScreen...")
            navController.navigate(Routes.IdeaScreenRoutes(ideaId = currentIdea.value!!.ideaId))
        }
    }

    LaunchedEffect(generateIdeaState.value) {
        if (generateIdeaState.value.isLoading) {
            android.util.Log.d("SkillScreen", "Ideas loading...")
            isGenerating.value = true
        } else if (generateIdeaState.value.isSuccess != null) {
            // ✅ When ideas are generated, wait for save to complete
            android.util.Log.d("SkillScreen", "Ideas generated: ${generateIdeaState.value.isSuccess?.size} ideas")
            isGenerating.value = false
            // Navigation will happen via lastGeneratedIdeaId LaunchedEffect
        } else if (generateIdeaState.value.isError != null) {
            android.util.Log.d("SkillScreen", "Error generating ideas: ${generateIdeaState.value.isError}")
            isGenerating.value = false
            // Error is shown in ErrorBanner, no dialog needed
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Header with back button
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2196F3))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Enter Your Skills",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily,
                        color = Color.White
                    )
                )
            }

            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Error Message Display
                if (generateIdeaState.value.isError != null) {
                    item {
                        ErrorBanner(
                            message = generateIdeaState.value.isError ?: "Unknown error",
                            onDismiss = { viewModel.resetGenerateIdeaState() }
                        )
                    }
                }

                // Tech Stack Input
                item {
                    SkillInputField(
                        label = "Tech Stack",
                        placeholder = "e.g., Kotlin, Android",
                        value = techStackState.value,
                        onValueChange = { techStackState.value = it },
                        icon = "📚"
                    )
                }

                // Experience Level Selector
                item {
                    SkillLevelSelector(
                        label = "Experience Level",
                        selectedValue = experienceLevelState.value,
                        options = listOf("Beginner", "Intermediate", "Advanced"),
                        onSelect = { experienceLevelState.value = it }
                    )
                }

                // Goal Input
                item {
                    SkillInputField(
                        label = "Goal",
                        placeholder = "e.g., Build a social networking app",
                        value = goalState.value,
                        onValueChange = { goalState.value = it },
                        icon = "🎯",
                        singleLine = false,
                        maxLines = 3
                    )
                }

                // Libraries Input with Tag Display
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                    ) {
                        Text(
                            text = "Libraries/Dependencies",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = PoppinsFontFamily,
                                color = Color(0xFF1E1E1E)
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Input row for libraries
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = currentLibraryState.value,
                                onValueChange = { currentLibraryState.value = it },
                                placeholder = {
                                    Text(
                                        "e.g., Hilt, Room",
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            color = Color(0xFFAAAAAA)
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                textStyle = TextStyle(fontSize = 12.sp),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2196F3),
                                    unfocusedBorderColor = Color(0xFFE0E0E0)
                                )
                            )
                            Button(
                                onClick = {
                                    if (currentLibraryState.value.isNotBlank()) {
                                        librariesState.add(currentLibraryState.value)
                                        currentLibraryState.value = ""
                                    }
                                },
                                modifier = Modifier.height(44.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2196F3)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Add", style = TextStyle(fontSize = 12.sp))
                            }
                        }

                        // Display library tags
                        if (librariesState.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            SkillTagsDisplay(
                                tags = librariesState,
                                onRemove = { librariesState.remove(it) }
                            )
                        }
                    }
                }

                // Additional Notes
                item {
                    SkillInputField(
                        label = "Additional Notes (Optional)",
                        placeholder = "Any other details...",
                        value = additionalNotesState.value,
                        onValueChange = { additionalNotesState.value = it },
                        icon = "📝",
                        singleLine = false,
                        maxLines = 3
                    )
                }

                // Generate Idea Button
                item {
                    Button(
                        onClick = {
                            if (techStackState.value.isNotBlank() && 
                                experienceLevelState.value.isNotBlank() && 
                                goalState.value.isNotBlank()) {
                                
                                val skill = UserSkillEntity(
                                    userId = userId,
                                    techStack = techStackState.value,
                                    libraries = librariesState.toList(),
                                    experienceLevel = experienceLevelState.value,
                                    goal = goalState.value,
                                    additionalNotes = if (additionalNotesState.value.isNotBlank()) 
                                        additionalNotesState.value else null
                                )
                                
                                // Save skill to database
                                // This will automatically trigger idea generation after skill is saved
                                viewModel.saveSkill(skill)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isGenerating.value && techStackState.value.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            disabledContainerColor = Color(0xFFCCCCCC)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isGenerating.value) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Text(
                                    "Generating Ideas...",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = PoppinsFontFamily,
                                        color = Color.White
                                    )
                                )
                            }
                        } else {
                            Text(
                                "Generate Ideas with AI",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = PoppinsFontFamily,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun SkillInputField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: String = "",
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (icon.isNotEmpty()) {
                Text(icon, fontSize = 16.sp)
            }
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily,
                    color = Color(0xFF1E1E1E)
                )
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp)),
            placeholder = {
                Text(
                    placeholder,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color(0xFFAAAAAA)
                    )
                )
            },
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily
            ),
            singleLine = singleLine,
            maxLines = maxLines,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFFAFAFA)
            )
        )
    }
}

@Composable
fun SkillLevelSelector(
    label: String,
    selectedValue: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PoppinsFontFamily,
                color = Color(0xFF1E1E1E)
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { option ->
                SelectableChip(
                    text = option,
                    isSelected = selectedValue == option,
                    onClick = { onSelect(option) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SelectableChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(44.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF2196F3) else Color(0xFFE8E8E8)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontFamily = PoppinsFontFamily,
                    color = if (isSelected) Color.White else Color(0xFF333333)
                )
            )
        }
    }
}

@Composable
fun SkillTagsDisplay(
    tags: List<String>,
    onRemove: (String) -> Unit
) {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.chunked(2).forEach { rowTags ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowTags.forEach { tag ->
                    SkillTag(text = tag, onRemove = { onRemove(tag) })
                }
            }
        }
    }
}

@Composable
fun SkillTag(text: String, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .height(36.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFontFamily,
                    color = Color(0xFF1976D2)
                )
            )
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Remove",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onRemove() },
                tint = Color(0xFF1976D2)
            )
        }
    }
}

@Composable
fun ErrorBanner(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Error icon
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = Color(0xFFEF5350).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "⚠️",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Error message
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Error",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily,
                        color = Color(0xFFC62828)
                    )
                )
                Text(
                    text = message,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = PoppinsFontFamily,
                        color = Color(0xFF5E35B1)
                    )
                )
            }

            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Dismiss error",
                    tint = Color(0xFFEF5350),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}