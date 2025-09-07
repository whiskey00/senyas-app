package com.example.senyas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.compose.animation.*
import androidx.navigation.navArgument
import com.example.senyas.HistoryScreen
import com.example.senyas.ui.theme.SenyasColors

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
                    onSavedClick = { navController.navigate("saved") },
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
                    },
                    onProfileClick = { navController.navigate("profile") },
                    onAboutClick = { navController.navigate("about") }
                )
            }
            composable("history") {
                HistoryScreen(
                    onBack = { navController.popBackStack() },
                    navController = navController
                )
            }

            composable("saved") {
                SavedScreen(
                    onBack = { navController.popBackStack() },
                    navController = navController
                )
            }

            composable("profile") {
                ProfileScreen(onBack = { navController.popBackStack() })
            }

            composable("about") {
                AboutScreen(onBack = { navController.popBackStack() })
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
            .background(SenyasColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.senyas_w),
                contentDescription = "Senyas Logo",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Initializing Senyas...",
                color = SenyasColors.OnSurfaceVariant,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


