package com.skillMatcher.buildMate.presentation.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.skillMatcher.buildMate.R
import com.skillMatcher.buildMate.domain.models.UserDataModels
import com.skillMatcher.buildMate.presentation.nav.Routes
import com.skillMatcher.buildMate.viewmodel.MyViewModel

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController
) {

    val backgroundColor = Color(0xFF161622)
    val buttonColor = Color(0xFFFFA000)
    val accentColor = Color(0xFFFFA000)

    val context = LocalContext.current

    val signupState = viewModel.createUserState.collectAsState()

    val scope = rememberCoroutineScope()
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ){
//        GoogleSignIn.doSignInWithGoogle(
//            context = context,
//            scope = scope,
//            launcher = null,
//            login = {
//                navController.navigate(Routes.HomeScreenRoutes) {
//                    popUpTo(Routes.SignUpScreenRoutes) {
//                        inclusive = true
//                    }
//                }
//            }
//        )
//    }


    when{
        signupState.value.isLoading ->{
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        signupState.value.isSuccess != null ->{
            Toast.makeText(context, "Signup Successfully", Toast.LENGTH_SHORT).show()
        }
        signupState.value.isError != null ->{
            Text(signupState.value.isError.toString())
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
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {


                // Logo and Title
                Image(
                    painter = painterResource(id = R.drawable.home),
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
                modifier = Modifier.fillMaxWidth().padding(bottom = 9.dp, top = 9.dp, start = 7.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Sign up",
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                    ),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            // Username
            Text("Username",style = TextStyle(
                color = Color(0xFFCDCDE0),
                fontFamily = FontFamily(Font(R.font.poppins_regular),
                )
            ),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "username", color = Color.Gray) },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                    ),
                )
                Spacer(modifier = Modifier.width(8.dp))

            }

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


            //Phone Number Field

            Text("Phone Number",style = TextStyle(
                color = Color(0xFFCDCDE0),
                fontFamily = FontFamily(Font(R.font.poppins_regular),
                )
            ),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start)

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text(text = "Phone Number", color = Color.Gray) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
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
                    val data = UserDataModels(
                        username = name,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password,
                    )
                    viewModel.createUser(data)

                    navController.navigate(Routes.LoginScreenRoutes) {
                        popUpTo(Routes.SignUpScreenRoutes) {
                            inclusive = true
                        }
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
                    text = "Signup",
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
                    text = "Already have an account?",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Login",
                    color = accentColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.LoginScreenRoutes) {
                            popUpTo(Routes.SignUpScreenRoutes) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}