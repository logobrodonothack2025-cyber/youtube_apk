package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Instagram Brand Colors
val IgGradientStart = Color(0xFFF58529)
val IgGradientOrange = Color(0xFFFEDA77)
val IgGradientPink = Color(0xFFDD2A7B)
val IgGradientPurple = Color(0xFF8134AF)
val IgGradientBlue = Color(0xFF515BD4)
val IgPrimary = Color(0xFF0095F6) // Instagram Action Blue
val IgRedHeart = Color(0xFFED4956)
val IgCloseFriendsGreen = Color(0xFF10B981)

val IgStoryBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFBAA47),
        Color(0xFFD91A46),
        Color(0xFFA60F93)
    )
)

val IgCloseFriendsBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFF34D399),
        Color(0xFF059669)
    )
)

val IgViewedStoryBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFF9E9E9E),
        Color(0xFF757575)
    )
)

// Dark Theme Colors (Instagram Dark Mode)
val IgDarkBackground = Color(0xFF000000)
val IgDarkSurface = Color(0xFF121212)
val IgDarkCard = Color(0xFF181818)
val IgDarkBorder = Color(0xFF262626)
val IgDarkTextPrimary = Color(0xFFF5F5F5)
val IgDarkTextSecondary = Color(0xFFA8A8A8)
val IgDarkIcon = Color(0xFFF5F5F5)

// Light Theme Colors
val IgLightBackground = Color(0xFFFFFFFF)
val IgLightSurface = Color(0xFFFAFAFA)
val IgLightCard = Color(0xFFFFFFFF)
val IgLightBorder = Color(0xFFDBDBDB)
val IgLightTextPrimary = Color(0xFF262626)
val IgLightTextSecondary = Color(0xFF8E8E8E)
val IgLightIcon = Color(0xFF262626)
