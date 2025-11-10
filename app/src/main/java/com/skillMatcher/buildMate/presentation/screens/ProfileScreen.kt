package com.skillMatcher.buildMate.presentation.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Delete
import coil3.compose.AsyncImage
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import java.io.File
import java.io.InputStream
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.skillMatcher.buildMate.domain.models.UserDataModels
import com.skillMatcher.buildMate.presentation.nav.Routes
import com.skillMatcher.buildMate.ui.theme.PoppinsFontFamily
import com.skillMatcher.buildMate.viewmodel.MyViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController,
    firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    val userNameState = viewModel.userName.collectAsState()
    val fetchUserDataState = viewModel.fetchUserDataState.collectAsState()
    val signOutState = viewModel.signOutState.collectAsState()
    val deleteAccountState = viewModel.deleteAccountState.collectAsState()
    val updateUserState = viewModel.updateUserState.collectAsState()
    
    val context = LocalContext.current
    
    val showDeleteDialog = remember { mutableStateOf(false) }
    val showUpdateDialog = remember { mutableStateOf(false) }
    val profileImageUri = remember { mutableStateOf<Uri?>(null) }
    val updateName = remember { mutableStateOf("") }
    val updateEmail = remember { mutableStateOf("") }
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // Copy image to app's internal storage
            val savedUri = context.copyImageToAppStorage(uri)
            if (savedUri != null) {
                profileImageUri.value = savedUri
            }
        }
    }
    
    LaunchedEffect(Unit) {
        // Load saved profile image from app storage
        val savedImageUri = context.getProfileImageUri()
        if (savedImageUri != null) {
            profileImageUri.value = savedImageUri
        }
        
        viewModel.fetchUserName()
        viewModel.fetchUserData()
    }
    
    // Handle sign out navigation
    LaunchedEffect(signOutState.value.isSuccess) {
        if (signOutState.value.isSuccess) {
            navController.navigate(Routes.LoginScreenRoutes) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    
    // Handle delete account navigation
    LaunchedEffect(deleteAccountState.value.isSuccess) {
        if (deleteAccountState.value.isSuccess != null) {
            navController.navigate(Routes.SignUpScreenRoutes) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    
    // Handle update user success
    LaunchedEffect(updateUserState.value.isSuccess) {
        if (updateUserState.value.isSuccess != null) {
            showUpdateDialog.value = false
            // Refresh user data after update
            viewModel.fetchUserName()
            viewModel.fetchUserData()
        }
    }
    
    val userName = userNameState.value.isSuccess ?: "User"
    val userData = fetchUserDataState.value.isSuccess
    val userEmail = userData?.email ?: firebaseAuth.currentUser?.email ?: "user@example.com"

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { -300 }) + fadeIn()
            ) {
                Text(
                    text = "My Profile",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily,
                        color = Color(0xFF1E1E1E)
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            // Profile Image Section with Camera Button
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 300 }) + fadeIn()
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3F2FD))
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    // Display actual image or placeholder
                    if (profileImageUri.value != null) {
                        AsyncImage(
                            model = profileImageUri.value,
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Add Photo",
                            modifier = Modifier.size(60.dp),
                            tint = Color(0xFF2196F3)
                        )
                    }

                    // Camera button overlay
                    IconButton(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(Color(0xFF2196F3), shape = CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Change Photo",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // User Information Card
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 500 }) + fadeIn()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Name Section
                        ProfileInfoItem(
                            label = "Name",
                            value = userName,
                            icon = "👤"
                        )

                        // Email Section
                        ProfileInfoItem(
                            label = "Email",
                            value = userEmail,
                            icon = "📧"
                        )

                        // Account Type Section
                        ProfileInfoItem(
                            label = "Account Type",
                            value = "Premium",
                            icon = "⭐"
                        )

                        // Member Since Section
                        ProfileInfoItem(
                            label = "Member Since",
                            value = "Nov 2025",
                            icon = "📅"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons Section
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 500 }) + fadeIn()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Update Personal Details Button
                    ActionButton(
                        text = "Update Personal Details",
                        icon = Icons.Default.Edit,
                        backgroundColor = Color(0xFF4CAF50),
                        onClick = {
                            showUpdateDialog.value = true
                        }
                    )

                    // Sign Out Button
                    ActionButton(
                        text = "Sign Out",
                        icon = Icons.Default.Logout,
                        backgroundColor = Color(0xFF2196F3),
                        onClick = {
                            viewModel.signOut()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delete Account Button (Bottom)
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 500 }) + fadeIn()
            ) {
                ActionButton(
                    text = "Delete Account",
                    icon = Icons.Default.Delete,
                    backgroundColor = Color(0xFFFF6B6B),
                    onClick = {
                        showDeleteDialog.value = true
                    }
                )
            }

            // Loading and Error States
            if (userNameState.value.isLoading || fetchUserDataState.value.isLoading) {
                Spacer(modifier = Modifier.height(24.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = Color(0xFF2196F3)
                )
            }

            // Error Messages
            if (userNameState.value.isError != null) {
                Spacer(modifier = Modifier.height(16.dp))
                ErrorMessageBox(message = userNameState.value.isError ?: "Unknown error")
            }

            if (deleteAccountState.value.isError != null) {
                Spacer(modifier = Modifier.height(16.dp))
                ErrorMessageBox(message = deleteAccountState.value.isError ?: "Failed to delete account")
            }

            // Delete Loading
            if (deleteAccountState.value.isLoading) {
                Spacer(modifier = Modifier.height(24.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = Color(0xFFFF6B6B)
                )
            }
        }
    }

    // Delete Account Confirmation Dialog
    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = {
                Text(
                    "Delete Account",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily
                    )
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete your account? This action cannot be undone and all your data will be permanently deleted.",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        color = Color(0xFF666666)
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAccount()
                        showDeleteDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B6B)
                    )
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Update Personal Details Dialog
    if (showUpdateDialog.value) {
        AlertDialog(
            onDismissRequest = { showUpdateDialog.value = false },
            title = {
                Text(
                    "Update Personal Details",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = updateName.value,
                        onValueChange = { updateName.value = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color(0xFFDDDDDD),
                            focusedLabelColor = Color(0xFF4CAF50)
                        ),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = updateEmail.value,
                        onValueChange = { updateEmail.value = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color(0xFFDDDDDD),
                            focusedLabelColor = Color(0xFF4CAF50)
                        ),
                        singleLine = true
                    )
                    
                    if (updateUserState.value.isError != null) {
                        Text(
                            text = updateUserState.value.isError ?: "Error updating profile",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily,
                                color = Color(0xFFD32F2F)
                            )
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (updateName.value.isNotEmpty() || updateEmail.value.isNotEmpty()) {
                            val userData = UserDataModels(
                                username = updateName.value.ifEmpty { userName },
                                email = updateEmail.value.ifEmpty { userEmail },
                                password = "" // Not needed for update
                            )
                            viewModel.updateUser(userData)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    enabled = !updateUserState.value.isLoading
                ) {
                    if (updateUserState.value.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Update", color = Color.White)
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showUpdateDialog.value = false },
                    enabled = !updateUserState.value.isLoading
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileInfoItem(
    label: String,
    value: String,
    icon: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = icon,
                fontSize = 18.sp
            )
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500,
                    fontFamily = PoppinsFontFamily,
                    color = Color(0xFF999999)
                )
            )
        }
        Text(
            text = value,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                fontFamily = PoppinsFontFamily,
                color = Color(0xFF1E1E1E)
            ),
            modifier = Modifier.padding(start = 26.dp)
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600,
                    fontFamily = PoppinsFontFamily,
                    color = Color.White
                )
            )
        }
    }
}

@Composable
fun ErrorMessageBox(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFFF6B6B),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
    ) {
        Text(
            text = message,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = Color(0xFFD32F2F)
            ),
            modifier = Modifier.padding(12.dp)
        )
    }
}

// Profile Image File Storage Helper Functions
fun Context.copyImageToAppStorage(sourceUri: Uri): Uri? {
    return try {
        val inputStream: InputStream? = contentResolver.openInputStream(sourceUri)
        if (inputStream != null) {
            val fileName = "profile_image.jpg"
            val file = File(filesDir, fileName)
            
            // Copy file to internal storage
            inputStream.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            
            // Return file URI
            Uri.fromFile(file)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Context.getProfileImageUri(): Uri? {
    return try {
        val fileName = "profile_image.jpg"
        val file = File(filesDir, fileName)
        
        if (file.exists()) {
            Uri.fromFile(file)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Context.clearProfileImageUri() {
    try {
        val fileName = "profile_image.jpg"
        val file = File(filesDir, fileName)
        if (file.exists()) {
            file.delete()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}