package com.example.senyas

import android.content.Context
import android.os.SystemClock
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.MediaImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.ImageProcessingOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizer
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizerResult

class GestureRecognizerHelper(
    context: Context,
    private val onTopGestureUpdate: (name: String, score: Float) -> Unit,
    private val onError: (Throwable) -> Unit = {}
) {

    private var gestureRecognizer: GestureRecognizer? = null

    init {
        setupRecognizer(context)
    }

    private fun setupRecognizer(context: Context) {
        val baseOptions = BaseOptions.builder()
            .setModelAssetPath(MODEL_ASSET_PATH)
            .build()

        val options = GestureRecognizer.GestureRecognizerOptions.builder()
            .setBaseOptions(baseOptions)
            .setNumHands(1)
            .setRunningMode(RunningMode.LIVE_STREAM)
            .setResultListener { result: GestureRecognizerResult, _ ->
                // Take the top gesture for the first detected hand
                val top = result.gestures().firstOrNull()?.firstOrNull()
                if (top != null) {
                    onTopGestureUpdate(top.categoryName(), top.score())
                }
            }
            .setErrorListener { e -> onError(e) }
            .build()

        gestureRecognizer = GestureRecognizer.createFromOptions(context, options)
    }

    fun analyze(imageProxy: ImageProxy) {
        try {
            val mediaImage = imageProxy.image ?: return
            val mpImage = MediaImageBuilder(mediaImage).build()

            val imageProcessingOptions = ImageProcessingOptions.builder()
                .setRotationDegrees(imageProxy.imageInfo.rotationDegrees)
                .build()

            // Use monotonic time for live stream timestamps
            val timestampMs = SystemClock.uptimeMillis()
            gestureRecognizer?.recognizeAsync(mpImage, imageProcessingOptions, timestampMs)
        } catch (t: Throwable) {
            onError(t)
        } finally {
            imageProxy.close()
        }
    }

    fun close() {
        gestureRecognizer?.close()
        gestureRecognizer = null
    }

    companion object {
        private const val MODEL_ASSET_PATH = "gesture_recognizer.task"
    }
}

