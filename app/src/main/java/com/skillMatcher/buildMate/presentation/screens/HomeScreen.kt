package com.skillMatcher.buildMate.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

// Custom Font Family
val PoppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.W500),
    Font(R.font.poppins_semibold, FontWeight.W600),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_light, FontWeight.Light),
)

val SoraFontFamily = FontFamily(
    Font(R.font.sora_semibold, FontWeight.SemiBold),
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController,
    firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    val userNameState = viewModel.userName.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.fetchUserName()
    }

    val userName = userNameState.value.isSuccess ?: "User"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Welcome Header
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 300 }) + fadeIn()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Welcome back, $userName! 👋",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = PoppinsFontFamily,
                            color = Color(0xFF1E1E1E)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Let's make something awesome today",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = PoppinsFontFamily,
                            color = Color(0xFF7A7A7A)
                        )
                    )
                }
            }

            // Spacer
            Box(modifier = Modifier.padding(top = 32.dp))

            // Option Cards Container
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 500 }) + fadeIn()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Skill Screen Option Card
                    HomeOptionCard(
                        title = "Enter Your Skills",
                        subtitle = "Add and manage your professional skills",
                        icon = Icons.Default.EmojiEvents,
                        backgroundColor = Color(0xFFE3F2FD),
                        iconTint = Color(0xFF2196F3),
                        textColor = Color(0xFF1976D2),
                        onClick = {
                            navController.navigate(Routes.SkillScreenRoutes)
                        }
                    )

                    // Ideas Screen Option Card
                    HomeOptionCard(
                        title = "Show All Ideas",
                        subtitle = "View your generated AI-powered ideas",
                        icon = Icons.Default.Lightbulb,
                        backgroundColor = Color(0xFFFFF3E0),
                        iconTint = Color(0xFFFFA726),
                        textColor = Color(0xFFE65100),
                        onClick = {
                            navController.navigate(Routes.GetAllIdeaScreenRoutes(userId = firebaseAuth.currentUser?.uid ?: "unknown_user"))
                        }
                    )

                    // Profile Screen Option Card
                    HomeOptionCard(
                        title = "Your Profile",
                        subtitle = "View and edit your profile information",
                        icon = Icons.Default.AccountCircle,
                        backgroundColor = Color(0xFFF3E5F5),
                        iconTint = Color(0xFF9C27B0),
                        textColor = Color(0xFF7B1FA2),
                        onClick = {
                            navController.navigate(Routes.ProfileScreenRoutes)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconTint: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = true, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Icon and Title Row
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                                .background(
                                    color = iconTint.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp),
                            tint = iconTint
                        )

                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = PoppinsFontFamily,
                                color = textColor
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Text(
                            text = subtitle,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = PoppinsFontFamily,
                                color = textColor.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            }
        }
    }
}