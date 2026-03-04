package app.linger.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Indigo80,
    onPrimary = DeepCharcoal,
    primaryContainer = IndigoContainer,
    secondary = Slate80,
    onSecondary = Color.Black,
    tertiary = Amber80, // Used for the Resonance Glow
    onTertiary = Color.Black,
    background = DeepCharcoal,
    surface = DeepCharcoal,
    onBackground = OffWhite,
    onSurface = OffWhite,
    error = EchoCrimson
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo40,
    onPrimary = Color.White,
    secondary = Slate40,
    tertiary = Amber40,
    background = SoftMist,
    surface = SoftMist,
    onBackground = DeepCharcoal,
    onSurface = DeepCharcoal,
    error = EchoCrimson
)

@Composable
fun LingerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}