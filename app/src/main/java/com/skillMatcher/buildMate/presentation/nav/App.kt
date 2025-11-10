package com.skillMatcher.buildMate.presentation.nav


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.skillMatcher.buildMate.presentation.screens.GetAllIdeaScreen
import com.skillMatcher.buildMate.presentation.screens.HomeScreen
import com.skillMatcher.buildMate.presentation.screens.IdeaByIdScreen
import com.skillMatcher.buildMate.presentation.screens.IdeaScreen
import com.skillMatcher.buildMate.presentation.screens.LoginScreen
import com.skillMatcher.buildMate.presentation.screens.ProfileScreen
import com.skillMatcher.buildMate.presentation.screens.SignupScreen
import com.skillMatcher.buildMate.presentation.screens.SkillScreen
import com.skillMatcher.buildMate.presentation.screens.SplashScreen

@Composable
fun App(firebaseAuth: FirebaseAuth, modifier: Modifier = Modifier) {

    val navController = rememberNavController()

//    val isAuthenticated = firebaseAuth.currentUser != null
//
//    // Determine start destination without navigating
//    val startDestination: Routes = if (isAuthenticated) {
//        Routes.HomeScreenRoutes
//    } else {
//        Routes.LoginScreenRoutes
//    }

    var startScreen = if(firebaseAuth.currentUser != null){
        SubNavigation.HomeScreenRoutes
    }else{
        SubNavigation.LoginSignUpScreenRoutes
    }

    NavHost(navController, startDestination = startScreen, modifier = modifier) {
        navigation<SubNavigation.LoginSignUpScreenRoutes>(startDestination = Routes.LoginScreenRoutes){
            composable<Routes.LoginScreenRoutes> {
                LoginScreen(navController = navController)
            }
            composable<Routes.SignUpScreenRoutes> {
                SignupScreen(navController = navController)
            }
        }
        navigation<SubNavigation.HomeScreenRoutes>(startDestination = Routes.HomeScreenRoutes){
            composable<Routes.HomeScreenRoutes> {
                HomeScreen(navController = navController)
            }
            composable<Routes.ProfileScreenRoutes> {
                ProfileScreen(navController = navController)
            }
            composable <Routes.SkillScreenRoutes> {
                SkillScreen(navController = navController)
            }
            composable<Routes.SplashScreenRoutes> {
                SplashScreen(navController = navController)
            }
            composable<Routes.IdeaScreenRoutes> {
                val ideaId = it.toRoute<Routes.IdeaScreenRoutes>()
                IdeaScreen(navController = navController, ideaId = ideaId.ideaId)
            }
            composable <Routes.GetAllIdeaScreenRoutes>{
                val userId = it.toRoute<Routes.GetAllIdeaScreenRoutes>()
                GetAllIdeaScreen(navController = navController, userId = userId.userId)
            }
            composable <Routes.IdeaByIdScreenRoutes>{
                val ideaId = it.toRoute<Routes.IdeaByIdScreenRoutes>()
                IdeaByIdScreen(navController = navController, ideaId = ideaId.ideaId)
            }
        }
    }
}