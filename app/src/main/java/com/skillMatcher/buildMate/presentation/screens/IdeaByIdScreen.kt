package com.skillMatcher.buildMate.presentation.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.skillMatcher.buildMate.R
import com.skillMatcher.buildMate.viewmodel.MyViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val PoppinsFontFamilyIdea = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

@Composable
fun IdeaByIdScreen(
    modifier: Modifier = Modifier,
    ideaId: String,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController,
    firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    val currentIdea = viewModel.currentIdea.collectAsState()
    val userId = firebaseAuth.currentUser?.uid ?: ""

    // Fetch idea by ID when screen loads
    LaunchedEffect(ideaId) {
        android.util.Log.d("IdeaByIdScreen", "Fetching idea with ID: $ideaId")
        try {
            val id = ideaId.toIntOrNull() ?: 0
            if (id > 0 && userId.isNotEmpty()) {
                viewModel.fetchIdeaById(userId, id)
            }
        } catch (e: Exception) {
            android.util.Log.e("IdeaByIdScreen", "Error converting ideaId: $ideaId", e)
        }
    }

    val idea = currentIdea.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        if (idea == null) {
            // Loading state
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading idea details...")
            }
        } else {
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
                            android.util.Log.d("IdeaByIdScreen", "Back button clicked")
                            navController.popBackStack()
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
                        text = "Idea Details",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = PoppinsFontFamilyIdea,
                            color = Color(0xFF1E1E1E)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Title Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
                            text = "💡 ${idea.ideaTitle}",
                            style = TextStyle(
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = PoppinsFontFamilyIdea,
                                color = Color.White
                            )
                        )

                        // Difficulty & Date Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = when (idea.difficulty) {
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
                                    text = "⚡ ${idea.difficulty}",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = PoppinsFontFamilyIdea,
                                        color = Color.White
                                    )
                                )
                            }

                            Text(
                                text = "📅 ${formatDateFull(idea.createdAt)}",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = PoppinsFontFamilyIdea,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Description
                IdeaDetailSection(
                    title = "📖 Description",
                    content = idea.description
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Technologies
                IdeaDetailSection(
                    title = "🛠️ Technologies & Libraries",
                    content = idea.techUsed
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Learning Focus
                IdeaDetailSection(
                    title = "🎯 What You'll Learn",
                    content = idea.learningFocus
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun IdeaDetailSection(
    title: String,
    content: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = PoppinsFontFamilyIdea,
                color = Color(0xFF1E1E1E)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = content,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = PoppinsFontFamilyIdea,
                    color = Color(0xFF666666),
                    lineHeight = 20.sp
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

fun formatDateFull(timestamp: Long): String {
    return try {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
        formatter.format(date)
    } catch (e: Exception) {
        "Unknown date"
    }
}