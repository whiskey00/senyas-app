package com.example.senyas

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.navArgument
import com.example.senyas.HistoryScreen



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = Firebase.auth

    var isCheckingAuth by remember { mutableStateOf(true) }
    var startDestination by remember { mutableStateOf("login") }

    LaunchedEffect(Unit) {
        if (auth.currentUser != null) {
            startDestination = "home"
        }
        isCheckingAuth = false
    }

    if (isCheckingAuth) {
        LoadingScreen()
    } else {
        AnimatedNavHost(
            navController = navController,
            startDestination = startDestination,
            enterTransition = { fadeIn() + slideInVertically(initialOffsetY = { it / 2 }) },
            exitTransition = { fadeOut() + slideOutVertically(targetOffsetY = { -it / 4 }) },
            popEnterTransition = { fadeIn() + slideInVertically(initialOffsetY = { it / 2 }) },
            popExitTransition = { fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }) }
        ) {
            composable("login") {
                LoginScreen(
                    onNavigateToRegister = { navController.navigate("register") },
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("register") {
                RegisterScreen(
                    onNavigateToLogin = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("home?playText={playText}", arguments = listOf(
                navArgument("playText") { nullable = true }
            )) { backStackEntry ->
                val playText = backStackEntry.arguments?.getString("playText")
                HomeScreen(
                    onSettingsClick = { navController.navigate("settings") },
                    onLogout = {
                        navController.navigate("login") { popUpTo("home") { inclusive = true } }
                    },
                    onHistoryClick = { navController.navigate("history") },
                    onLearnFSLClick = { navController.navigate("learnFSL") },
                    playFromHistory = playText // Pass this to trigger playback
                )
            }

            composable("settings") {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        Firebase.auth.signOut()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
            composable("history") {
                HistoryScreen(
                    onBack = { navController.popBackStack() },
                    navController = navController
                )
            }

            composable("learnFSL") {
                LearnFSLScreen(onBack = { navController.popBackStack() })
            }


        }
    }
}



@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.senyas_w),
            contentDescription = "Senyas Logo",
            modifier = Modifier.size(300.dp)
        )
    }
}

