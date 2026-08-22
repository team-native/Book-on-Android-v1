package com.teamnative.bookon.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Immutable

@Immutable
data class BookOnExtraColors(
    val navigationDivider: Color,
)

val LocalBookOnExtraColors = staticCompositionLocalOf<BookOnExtraColors> {
    error("BookOnExtraColors is not provided.")
}

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightSurface,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnBackground,
    secondary = LightPrimaryPressed,
    onSecondary = LightSurface,
    tertiary = LightPrimaryPressed,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceContainerHighest = LightSurfaceContainerHighest,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    tertiaryContainer = LightTertiaryContainer,
    error = LightError,
    errorContainer = LightErrorContainer,
    onError = LightSurface,
    onErrorContainer = LightError,
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkBackground,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnBackground,
    secondary = DarkPrimaryPressed,
    onSecondary = DarkBackground,
    tertiary = DarkPrimaryPressed,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceContainerHighest = DarkSurfaceContainerHighest,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    tertiaryContainer = DarkTertiaryContainer,
    error = DarkError,
    errorContainer = DarkErrorContainer,
    onError = DarkBackground,
    onErrorContainer = DarkError,
)

@Composable
fun BookOnTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    val extraColors = if (darkTheme) {
        BookOnExtraColors(
            navigationDivider = DarkNavigationDivider,
        )
    } else {
        BookOnExtraColors(
            navigationDivider = LightNavigationDivider,
        )
    }

    CompositionLocalProvider(
        LocalBookOnExtraColors provides extraColors,
        LocalBookOnExtraTypography provides DefaultBookOnExtraTypography,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}
