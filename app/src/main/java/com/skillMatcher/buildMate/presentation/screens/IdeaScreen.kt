package com.skillMatcher.buildMate.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.skillMatcher.buildMate.presentation.nav.Routes
import com.skillMatcher.buildMate.viewmodel.MyViewModel

@Composable
fun IdeaScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController,
    ideaId: Int? = null,
    firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    val currentIdea = viewModel.currentIdea.collectAsState()
    val userId = firebaseAuth.currentUser?.uid ?: ""
    val isSaved = remember { mutableStateOf(false) }
    
    LaunchedEffect(ideaId) {
        android.util.Log.d("IdeaScreen", "IdeaScreen composed with ideaId: $ideaId")
        if (ideaId != null && ideaId > 0) {
            android.util.Log.d("IdeaScreen", "Fetching idea with ID: $ideaId for user: $userId")
            viewModel.fetchIdeaById(userId, ideaId)
        } else {
            android.util.Log.d("IdeaScreen", "No valid ideaId provided")
        }
    }

    // Get the current idea
    val idea = currentIdea.value
    
    LaunchedEffect(Unit) {
        android.util.Log.d("IdeaScreen", "IdeaScreen composed")
        android.util.Log.d("IdeaScreen", "UserId: $userId, IdeaId: $ideaId")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = { 
                       // android.util.Log.d("IdeaScreen", "Back button clicked - navigating to HomeScreen")
                        navController.navigate(Routes.HomeScreenRoutes) {
                            popUpTo(Routes.HomeScreenRoutes) { 
                                inclusive = false
                            }
                        }
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1E1E1E),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Project Idea",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily,
                        color = Color(0xFF1E1E1E)
                    )
                )
            }

            if (idea != null) {
                val ideaData = idea

                Spacer(modifier = Modifier.height(20.dp))

                // Main Idea Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .animateContentSize()
                ) {
                    // Title Section
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "💡 Project Idea",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = PoppinsFontFamily,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                            Text(
                                text = ideaData.ideaTitle,
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = PoppinsFontFamily,
                                    color = Color.White
                                )
                            )

                            // Difficulty Badge
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = when (ideaData.difficulty) {
                                            "Easy" -> Color(0xFF81C784)
                                            "Intermediate" -> Color(0xFFFFA726)
                                            "Hard" -> Color(0xFFE57373)
                                            else -> Color(0xFF90CAF9)
                                        },
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "⚡ ${ideaData.difficulty} Level",
                                    style = TextStyle(
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = PoppinsFontFamily,
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Description Card
                    IdeaDetailCard(
                        icon = "📖",
                        title = "Description",
                        content = ideaData.description
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tech Used Card
                    IdeaDetailCard(
                        icon = "🛠️",
                        title = "Technologies & Libraries",
                        content = ideaData.techUsed
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Learning Focus Card
                    IdeaDetailCard(
                        icon = "🎯",
                        title = "What You'll Learn",
                        content = ideaData.learningFocus
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "← Back",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = PoppinsFontFamily,
                                    color = Color(0xFF333333)
                                )
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.saveIdea(idea)
                                isSaved.value = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            enabled = !isSaved.value,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50),
                                disabledContainerColor = Color(0xFFC8E6C9)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                if (isSaved.value) "✓ Saved" else "💾 Save Idea",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = PoppinsFontFamily,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Info Banner
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF3E5F5)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "ℹ️ How to Use This Idea",
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = PoppinsFontFamily,
                                    color = Color(0xFF6A1B9A)
                                )
                            )
                            Text(
                                text =  "1. Review the project description and requirements\n" +
                                        "2. Gather all technologies mentioned\n" +
                                        "3. Create a project plan with milestones\n" +
                                        "4. Build incrementally, starting with core features\n" +
                                        "5. Save this idea for future reference",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = PoppinsFontFamily,
                                    color = Color(0xFF4A148C)
                                ),
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "No idea selected",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PoppinsFontFamily,
                            color = Color(0xFF999999)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate(Routes.HomeScreenRoutes)
                                  navController.popBackStack()},
                        modifier = Modifier.height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Go Back",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IdeaDetailCard(
    icon: String,
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = icon,
                    fontSize = 20.sp
                )
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily,
                        color = Color(0xFF1E1E1E)
                    )
                )
            }

            Text(
                text = content,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = PoppinsFontFamily,
                    color = Color(0xFF333333),
                    lineHeight = 20.sp
                )
            )
        }
    }
}
