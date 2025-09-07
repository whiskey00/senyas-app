package com.example.senyas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.senyas.ui.theme.SenyasColors
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit = {}, onLoginSuccess: () -> Unit = {}) {
    val auth = Firebase.auth
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SenyasColors.Background)
    ) {
        // Background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SenyasColors.Background,
                            SenyasColors.BackgroundVariant
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo and Welcome Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.senyas_w),
                    contentDescription = "Senyas Logo",
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Welcome Back",
                    style = MaterialTheme.typography.headlineMedium,
                    color = SenyasColors.OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Sign in to continue learning FSL",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SenyasColors.OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Login Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SenyasColors.Surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        "Sign In",
                        style = MaterialTheme.typography.titleLarge,
                        color = SenyasColors.OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null
                        },
                        placeholder = { 
                            Text(
                                "Email address",
                                color = SenyasColors.OnSurfaceLight
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = SenyasColors.OnSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SenyasColors.Primary,
                            unfocusedBorderColor = SenyasColors.Border,
                            focusedTextColor = SenyasColors.OnSurface,
                            unfocusedTextColor = SenyasColors.OnSurface,
                            focusedContainerColor = SenyasColors.SurfaceVariant,
                            unfocusedContainerColor = SenyasColors.SurfaceVariant,
                            errorBorderColor = SenyasColors.Error
                        ),
                        singleLine = true,
                        isError = emailError != null
                    )
                    
                    if (emailError != null) {
                        Text(
                            text = emailError!!,
                            color = SenyasColors.Error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null
                        },
                        placeholder = { 
                            Text(
                                "Password",
                                color = SenyasColors.OnSurfaceLight
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = SenyasColors.OnSurfaceVariant
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password",
                                    tint = SenyasColors.OnSurfaceVariant
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SenyasColors.Primary,
                            unfocusedBorderColor = SenyasColors.Border,
                            focusedTextColor = SenyasColors.OnSurface,
                            unfocusedTextColor = SenyasColors.OnSurface,
                            focusedContainerColor = SenyasColors.SurfaceVariant,
                            unfocusedContainerColor = SenyasColors.SurfaceVariant,
                            errorBorderColor = SenyasColors.Error
                        ),
                        singleLine = true,
                        isError = passwordError != null
                    )
                    
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = SenyasColors.Error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Login Button
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                emailError = null
                                passwordError = null

                                try {
                                    auth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                onLoginSuccess()
                                            } else {
                                                val exception = task.exception
                                                when (exception) {
                                                    is FirebaseAuthInvalidUserException -> {
                                                        emailError = "No account found with this email"
                                                    }
                                                    is FirebaseAuthInvalidCredentialsException -> {
                                                        passwordError = "Incorrect password"
                                                    }
                                                    else -> {
                                                        emailError = "Login failed. Please try again."
                                                    }
                                                }
                                            }
                                            isLoading = false
                                        }
                                } catch (e: Exception) {
                                    emailError = "Please fill in all fields"
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SenyasColors.Primary,
                            contentColor = SenyasColors.OnPrimary
                        ),
                        enabled = email.isNotBlank() && password.isNotBlank() && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = SenyasColors.OnPrimary,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Login,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Sign In",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = SenyasColors.Border
                        )
                        Text(
                            "or",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = SenyasColors.OnSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = SenyasColors.Border
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Register Link
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Don't have an account?",
                            color = SenyasColors.OnSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        TextButton(
                            onClick = onNavigateToRegister,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = SenyasColors.Primary
                            )
                        ) {
                            Text(
                                "Sign Up",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
