package com.skillMatcher.buildMate.presentation.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.skillMatcher.buildMate.R
import com.skillMatcher.buildMate.presentation.nav.Routes
import com.skillMatcher.buildMate.viewmodel.MyViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val PoppinsFontFamilyGetAll = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

@Composable
fun GetAllIdeaScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    userId: String,
    navController: NavController,
    firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    val allIdeas = viewModel.allIdeas.collectAsState()
    val isLoading = viewModel.allIdeasLoading.collectAsState()
    val error = viewModel.allIdeasError.collectAsState()
    
    val searchQuery = remember { mutableStateOf("") }
    val showDateFilter = remember { mutableStateOf(false) }

    // Fetch all ideas when screen loads or when userId changes
    LaunchedEffect(userId) {
        android.util.Log.d("GetAllIdeaScreen", "Fetching all ideas for user: $userId")
        viewModel.fetchAllIdeas(userId)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header
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
                        android.util.Log.d("GetAllIdeaScreen", "Back button clicked")
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
                    text = "My Ideas",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamilyGetAll,
                        color = Color(0xFF1E1E1E)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery.value,
                onValueChange = { 
                    searchQuery.value = it
                    viewModel.searchIdeas(it, userId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Search ideas...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ideas List
            when {
                isLoading.value -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
                error.value != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${error.value}",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = PoppinsFontFamilyGetAll,
                                color = Color.Red
                            )
                        )
                    }
                }
                allIdeas.value.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No ideas found",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = PoppinsFontFamilyGetAll,
                                color = Color.Gray
                            )
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(allIdeas.value) { idea ->
                            IdeaListItem(
                                idea = idea,
                                onIdeaClick = {
                                    android.util.Log.d("GetAllIdeaScreen", "Clicked idea: ${idea.ideaId}")
                                    navController.navigate(Routes.IdeaByIdScreenRoutes(ideaId = idea.ideaId.toString()))
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IdeaListItem(
    idea: com.skillMatcher.buildMate.data.entities.IdeaEntity,
    onIdeaClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onIdeaClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title
            Text(
                text = idea.ideaTitle,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFontFamilyGetAll,
                    color = Color(0xFF1E1E1E)
                ),
                maxLines = 2
            )

            // Description (truncated)
            Text(
                text = idea.description,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = PoppinsFontFamilyGetAll,
                    color = Color(0xFF666666)
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date and Difficulty Row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date
                Text(
                    text = formatDate(idea.createdAt),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PoppinsFontFamilyGetAll,
                        color = Color(0xFF999999)
                    )
                )

                // Difficulty Badge
                Box(
                    modifier = Modifier
                        .background(
                            color = when (idea.difficulty) {
                                "Easy" -> Color(0xFFE8F5E9)
                                "Intermediate" -> Color(0xFFFFF3E0)
                                "Hard" -> Color(0xFFFFEBEE)
                                else -> Color(0xFFE3F2FD)
                            },
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = idea.difficulty,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = PoppinsFontFamilyGetAll,
                            color = when (idea.difficulty) {
                                "Easy" -> Color(0xFF4CAF50)
                                "Intermediate" -> Color(0xFFFFA726)
                                "Hard" -> Color(0xFFEF5350)
                                else -> Color(0xFF42A5F5)
                            }
                        )
                    )
                }
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    return try {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        formatter.format(date)
    } catch (e: Exception) {
        "Unknown date"
    }
}
