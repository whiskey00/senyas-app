package com.example.senyas.ui.theme

import androidx.compose.ui.graphics.Color

// Modern Color Palette for Senyas
object SenyasColors {
    // Primary Colors - Modern Indigo
    val Primary = Color(0xFF6366F1)
    val PrimaryVariant = Color(0xFF4F46E5)
    val PrimaryLight = Color(0xFF818CF8)
    val PrimaryDark = Color(0xFF3730A3)
    
    // Secondary Colors - Modern Emerald
    val Secondary = Color(0xFF10B981)
    val SecondaryVariant = Color(0xFF059669)
    val SecondaryLight = Color(0xFF34D399)
    val SecondaryDark = Color(0xFF047857)
    
    // Accent Colors
    val Accent = Color(0xFFF59E0B) // Amber
    val AccentLight = Color(0xFFFBBF24)
    val AccentDark = Color(0xFFD97706)
    
    // Background Colors - Modern Dark Theme
    val Background = Color(0xFF0F172A) // Slate 900
    val BackgroundVariant = Color(0xFF020617) // Slate 950
    val BackgroundLight = Color(0xFF1E293B) // Slate 800
    
    // Surface Colors
    val Surface = Color(0xFF1E293B) // Slate 800
    val SurfaceVariant = Color(0xFF334155) // Slate 700
    val SurfaceLight = Color(0xFF475569) // Slate 600
    val SurfaceDark = Color(0xFF0F172A) // Slate 900
    
    // Text Colors
    val OnPrimary = Color.White
    val OnSecondary = Color.White
    val OnBackground = Color.White
    val OnSurface = Color.White
    val OnSurfaceVariant = Color(0xFFCBD5E1) // Slate 300
    val OnSurfaceLight = Color(0xFF94A3B8) // Slate 400
    
    // Status Colors
    val Success = Color(0xFF10B981) // Green
    val Warning = Color(0xFFF59E0B) // Amber
    val Error = Color(0xFFEF4444) // Red
    val Info = Color(0xFF3B82F6) // Blue
    
    // Gradient Colors
    val GradientStart = Color(0xFF6366F1)
    val GradientEnd = Color(0xFF8B5CF6)
    
    // Overlay Colors
    val Overlay = Color(0x80000000)
    val OverlayLight = Color(0x40000000)
    
    // Border Colors
    val Border = Color(0xFF334155) // Slate 700
    val BorderLight = Color(0xFF475569) // Slate 600
    val BorderDark = Color(0xFF1E293B) // Slate 800
}

// Legacy colors for backward compatibility
val Purple80 = SenyasColors.PrimaryLight
val PurpleGrey80 = SenyasColors.SurfaceLight
val Pink80 = SenyasColors.AccentLight

val Purple40 = SenyasColors.Primary
val PurpleGrey40 = SenyasColors.Surface
val Pink40 = SenyasColors.Accent