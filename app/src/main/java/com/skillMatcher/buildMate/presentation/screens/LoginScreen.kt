package com.skillMatcher.buildMate.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.skillMatcher.buildMate.R
import com.skillMatcher.buildMate.domain.models.UserDataModels
import com.skillMatcher.buildMate.presentation.nav.Routes
import com.skillMatcher.buildMate.viewmodel.MyViewModel

@Composable
fun LoginScreen(viewModel: MyViewModel = hiltViewModel(), navController: NavController) {

    val backgroundColor = Color(0xFF161622)
    val buttonColor = Color(0xFFFFA000)
    val accentColor = Color(0xFFFFA000)

    val context = LocalContext.current

    val loginState = viewModel.loginUserState.collectAsState()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }



    LaunchedEffect(loginState.value.isSuccess) {


        if (loginState.value.isSuccess) {
            Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.HomeScreenRoutes) {
                popUpTo(Routes.LoginScreenRoutes) {
                    inclusive = true
                }
            }
        }
    }

//    SideEffect {
//        if (loginState.value.isError != null) {
//            Toast.makeText(context, "User Not Found", Toast.LENGTH_SHORT).show()
//        }
//    }


    when{
        loginState.value.isLoading ->{
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        loginState.value.isError != null -> {
            // Show error message
            //Text("User Not Found")

            // Show toast when error occurs
        }

    }

    LaunchedEffect(loginState.value.isError) {
        loginState.value.isError?.let { errorMessage ->
            Toast.makeText(context, "User Not Found", Toast.LENGTH_SHORT).show()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {


                // Logo and Title
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp),
                    contentScale = ContentScale.Crop

                )

                Text(
                    text = "HOSTILIA",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.W600,
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.sora_semibold))
                    )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 7.dp, bottom = 9.dp, top = 9.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Login",
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                    ),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Username


//                0xFF1F1F2E
            // Email Field

            Text("Email",style = TextStyle(
                color = Color(0xFFCDCDE0),
                fontFamily = FontFamily(Font(R.font.poppins_regular),
                )
            ),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email", color = Color.Gray) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                ),
            )


            // Password Field
            val passwordVisible = remember { mutableStateOf(false) }

            Text("Password",style = TextStyle(
                color = Color(0xFFCDCDE0),
                fontFamily = FontFamily(Font(R.font.poppins_regular),
                )
            ),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisible.value = !passwordVisible.value
                    }) {
                        Image(painter = painterResource(R.drawable.eye), contentDescription = null, modifier = Modifier.size(24.dp))
                    }
                },
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
            )



            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    } else {
                        val userData = UserDataModels(email = email, password = password)
                        viewModel.loginUser(userData)

                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Login",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Signup Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account?",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Signup",
                    color = accentColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.SignUpScreenRoutes) {
                            popUpTo(Routes.LoginScreenRoutes) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}