package com.example.senyas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.senyas.ui.theme.SenyasColors
import kotlinx.coroutines.launch
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    val auth = Firebase.auth

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo and Welcome Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp, bottom = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.senyas_w),
                    contentDescription = "Senyas Logo",
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Join Senyas",
                    style = MaterialTheme.typography.headlineMedium,
                    color = SenyasColors.OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Create your account to start learning FSL",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SenyasColors.OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Registration Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SenyasColors.Surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "Create Account",
                        style = MaterialTheme.typography.titleLarge,
                        color = SenyasColors.OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Full Name Field
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            if (nameError != null) nameError = null
                        },
                        placeholder = { 
                            Text(
                                "Full Name",
                                color = SenyasColors.OnSurfaceLight
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
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
                        isError = nameError != null
                    )
                    
                    if (nameError != null) {
                        Text(
                            text = nameError!!,
                            color = SenyasColors.Error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (emailError != null) emailError = null
                        },
                        placeholder = { 
                            Text(
                                "Email Address",
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (passwordError != null) passwordError = null
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
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password",
                                    tint = SenyasColors.OnSurfaceVariant
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Password Field
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            if (confirmError != null) confirmError = null
                        },
                        placeholder = { 
                            Text(
                                "Confirm Password",
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
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password",
                                    tint = SenyasColors.OnSurfaceVariant
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                        isError = confirmError != null
                    )
                    
                    if (confirmError != null) {
                        Text(
                            text = confirmError!!,
                            color = SenyasColors.Error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Register Button
                    Button(
                        onClick = {
                            nameError = null
                            emailError = null
                            passwordError = null
                            confirmError = null

                            val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
                            var isValid = true

                            if (fullName.isBlank()) {
                                nameError = "Please enter your name"
                                isValid = false
                            }
                            if (email.isBlank() || !email.matches(emailPattern)) {
                                emailError = "Please enter a valid email"
                                isValid = false
                            }
                            if (password.isBlank()) {
                                passwordError = "Password cannot be empty"
                                isValid = false
                            }
                            if (confirmPassword.isBlank()) {
                                confirmError = "Confirm your password"
                                isValid = false
                            } else if (confirmPassword != password) {
                                confirmError = "Passwords do not match"
                                isValid = false
                            }

                            if (isValid) {
                                isLoading = true
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            auth.signInWithEmailAndPassword(email, password)
                                                .addOnCompleteListener { loginTask ->
                                                    if (loginTask.isSuccessful) {
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar("Welcome, ${fullName.split(" ").firstOrNull() ?: "User"}!")
                                                            onNavigateToHome()
                                                        }
                                                    } else {
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar("Registered, but login failed: ${loginTask.exception?.message}")
                                                        }
                                                    }
                                                    isLoading = false
                                                }
                                        } else {
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Registration failed: ${task.exception?.message}")
                                            }
                                            isLoading = false
                                        }
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
                        enabled = !isLoading
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
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Create Account",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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

                    // Login Link
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Already have an account?",
                            color = SenyasColors.OnSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        TextButton(
                            onClick = onNavigateToLogin,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = SenyasColors.Primary
                            )
                        ) {
                            Text(
                                "Sign In",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}