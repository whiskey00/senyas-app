package com.example.senyas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable
fun RegisterTextField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = Color.LightGray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF3B82F6),
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color(0xFF1E293B)
        ),
        singleLine = true
    )
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    visible: Boolean,
    onVisibilityToggle: () -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = Color.LightGray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Toggle Password",
                    tint = Color.LightGray
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF3B82F6),
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color(0xFF1E293B)
        )
    )
}

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

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A))
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Create Account",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            RegisterTextField("Full Name", fullName) {
                fullName = it
                if (nameError != null) nameError = null
            }
            if (nameError != null) {
                Text(nameError!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
            }

            RegisterTextField("Email", email, KeyboardType.Email) {
                email = it
                if (emailError != null) emailError = null
            }
            if (emailError != null) {
                Text(emailError!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
            }

            PasswordField("Password", password, passwordVisible, { passwordVisible = !passwordVisible }) {
                password = it
                if (passwordError != null) passwordError = null
            }
            if (passwordError != null) {
                Text(passwordError!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
            }

            PasswordField("Confirm Password", confirmPassword, confirmPasswordVisible, { confirmPasswordVisible = !confirmPasswordVisible }) {
                confirmPassword = it
                if (confirmError != null) confirmError = null
            }
            if (confirmError != null) {
                Text(confirmError!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                                        }
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Registration failed: ${task.exception?.message}")
                                    }
                                }
                            }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6),
                    contentColor = Color.White
                )
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text("Already have an account?", color = Color.LightGray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Login",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}