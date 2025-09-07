package com.example.senyas.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Modern Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = SenyasColors.Primary,
    onPrimary = SenyasColors.OnPrimary,
    primaryContainer = SenyasColors.PrimaryVariant,
    onPrimaryContainer = SenyasColors.OnPrimary,
    
    secondary = SenyasColors.Secondary,
    onSecondary = SenyasColors.OnSecondary,
    secondaryContainer = SenyasColors.SecondaryVariant,
    onSecondaryContainer = SenyasColors.OnSecondary,
    
    tertiary = SenyasColors.Accent,
    onTertiary = SenyasColors.OnPrimary,
    tertiaryContainer = SenyasColors.AccentDark,
    onTertiaryContainer = SenyasColors.OnPrimary,
    
    background = SenyasColors.Background,
    onBackground = SenyasColors.OnBackground,
    surface = SenyasColors.Surface,
    onSurface = SenyasColors.OnSurface,
    surfaceVariant = SenyasColors.SurfaceVariant,
    onSurfaceVariant = SenyasColors.OnSurfaceVariant,
    
    error = SenyasColors.Error,
    onError = SenyasColors.OnPrimary,
    errorContainer = SenyasColors.Error,
    onErrorContainer = SenyasColors.OnPrimary,
    
    outline = SenyasColors.Border,
    outlineVariant = SenyasColors.BorderLight,
    
    scrim = SenyasColors.Overlay,
    surfaceTint = SenyasColors.Primary
)

// Modern Light Color Scheme (for future use)
private val LightColorScheme = lightColorScheme(
    primary = SenyasColors.Primary,
    onPrimary = SenyasColors.OnPrimary,
    primaryContainer = SenyasColors.PrimaryLight,
    onPrimaryContainer = SenyasColors.PrimaryDark,
    
    secondary = SenyasColors.Secondary,
    onSecondary = SenyasColors.OnSecondary,
    secondaryContainer = SenyasColors.SecondaryLight,
    onSecondaryContainer = SenyasColors.SecondaryDark,
    
    tertiary = SenyasColors.Accent,
    onTertiary = SenyasColors.OnPrimary,
    tertiaryContainer = SenyasColors.AccentLight,
    onTertiaryContainer = SenyasColors.AccentDark,
    
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    
    error = SenyasColors.Error,
    onError = SenyasColors.OnPrimary,
    errorContainer = SenyasColors.Error,
    onErrorContainer = SenyasColors.OnPrimary,
    
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    
    scrim = SenyasColors.Overlay,
    surfaceTint = SenyasColors.Primary
)

@Composable
fun SenyasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled by default to maintain brand consistency
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Make the system bars transparent
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            
            // Enable edge-to-edge
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}