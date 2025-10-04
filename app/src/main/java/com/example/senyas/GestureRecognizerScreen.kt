package com.example.senyas

import android.Manifest
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.senyas.ui.theme.SenyasColors
import kotlinx.coroutines.android.awaitFrame
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun GestureRecognizerScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember { mutableStateOf(false) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        hasCameraPermission = granted
        if (!granted) requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    var gestureText by remember { mutableStateOf("Waiting for gesture…") }
    var gestureScore by remember { mutableStateOf(0f) }

    // Camera and analyzer infrastructure
    val previewView = remember {
        PreviewView(context).apply { scaleType = PreviewView.ScaleType.FILL_CENTER }
    }
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var imageAnalyzer by remember { mutableStateOf<ImageAnalysis?>(null) }
    var gestureHelper by remember {
        mutableStateOf(
            GestureRecognizerHelper(
                context,
                onTopGestureUpdate = { name, score ->
                    gestureText = name
                    gestureScore = score
                },
                onError = { /* swallow for now; could surface via snackbar */ }
            )
        )
    }

    // Bind CameraX when permission is ready
    LaunchedEffect(hasCameraPermission) {
        if (!hasCameraPermission) return@LaunchedEffect

        // Tiny delay to ensure PreviewView is attached
        awaitFrame()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val provider = cameraProviderFuture.get()
            cameraProvider = provider

            val preview = androidx.camera.core.Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            val analyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setTargetResolution(Size(640, 480))
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                        gestureHelper.analyze(imageProxy)
                    }
                }

            imageAnalyzer = analyzer
            try {
                provider.unbindAll()
                provider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analyzer
                )
            } catch (_: Exception) {
                // ignore
            }
        }, ContextCompat.getMainExecutor(context))
    }

    DisposableEffect(Unit) {
        onDispose {
            imageAnalyzer?.clearAnalyzer()
            cameraProvider?.unbindAll()
            gestureHelper.close()
            cameraExecutor.shutdown()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SenyasColors.Background)
    ) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Bottom overlay with recognized gesture text
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(SenyasColors.Surface.copy(alpha = 0.7f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (gestureText.isBlank()) "—" else "$gestureText  (${"%.1f".format(gestureScore * 100)}%)",
                color = SenyasColors.OnSurface
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SenyasColors.Primary,
                    contentColor = SenyasColors.OnPrimary
                )
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                Spacer(Modifier.width(6.dp))
                Text("Back")
            }
        }
    }
}

