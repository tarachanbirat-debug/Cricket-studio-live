package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CricBestColorScheme = darkColorScheme(
  primary = CricketGreen,
  onPrimary = PitchNavyDark,
  primaryContainer = CricketGreenDark,
  onPrimaryContainer = TextPrimary,
  secondary = BroadcastCyan,
  onSecondary = PitchNavyDark,
  tertiary = ScoreGold,
  background = PitchNavyDark,
  onBackground = TextPrimary,
  surface = CardNavy,
  onSurface = TextPrimary,
  surfaceVariant = SurfaceNavy,
  onSurfaceVariant = TextSecondary,
  outline = BorderSubtle,
  error = OutRed
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = CricBestColorScheme,
    typography = Typography,
    content = content
  )
}

