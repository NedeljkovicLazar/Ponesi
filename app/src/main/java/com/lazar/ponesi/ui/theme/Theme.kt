package com.lazar.ponesi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PonesiDarkColorScheme = darkColorScheme(
    primary = PonesiPrimary,
    onPrimary = PonesiOnPrimary,
    primaryContainer = PonesiPrimaryContainer,
    onPrimaryContainer = PonesiOnPrimaryContainer,
    inversePrimary = PonesiInversePrimary,

    secondary = PonesiSecondary,
    onSecondary = PonesiOnSecondary,
    secondaryContainer = PonesiSecondaryContainer,
    onSecondaryContainer = PonesiOnSecondaryContainer,

    tertiary = PonesiTertiary,
    onTertiary = PonesiOnTertiary,
    tertiaryContainer = PonesiTertiaryContainer,
    onTertiaryContainer = PonesiOnTertiaryContainer,

    background = PonesiBackground,
    onBackground = PonesiOnBackground,

    surface = PonesiSurface,
    onSurface = PonesiOnSurface,
    surfaceVariant = PonesiSurfaceVariant,
    onSurfaceVariant = PonesiOnSurfaceVariant,
    surfaceTint = PonesiSurfaceTint,

    inverseSurface = PonesiInverseSurface,
    inverseOnSurface = PonesiInverseOnSurface,

    outline = PonesiOutline,
    outlineVariant = PonesiOutlineVariant,

    error = PonesiError,
    onError = PonesiOnError,
    errorContainer = PonesiErrorContainer,
    onErrorContainer = PonesiOnErrorContainer,

    scrim = PonesiScrim
)

@Composable
fun PonesiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PonesiDarkColorScheme,
        typography = Typography,
        content = content
    )
}