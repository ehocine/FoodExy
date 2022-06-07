package com.helic.foodexy.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF6200EE)
val Teal200 = Color(0xFF03DAC5)

val Green = Color(0xFF00C980)
val Yellow = Color(0xFFFFC114)
val DarkerGray = Color(0xFF141414)
val DarkGray = Color(0xFF4B4B4B)
val MediumGray = Color(0xFF9C9C9C)
val LightGray = Color(0xFFFCFCFC)
val Red = Color(0xFFFF4646)
val BlackWithAlpha = Color(0xAA000000)

val Colors.CardBorderColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Purple500 else Purple500

val Colors.CardTitleColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.Black else Color.White

val Colors.CardDesColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) DarkGray else MediumGray

val Colors.topAppBarBackgroundColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Purple500 else Color.Black

val Colors.topAppBarContentColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.White else LightGray

val Colors.backgroundColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.White else Color.Black

val Colors.ProgressIndicatorColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Purple500 else LightGray

val Colors.fabBackgroundColor: Color
    @Composable
    get() = if (isLight) Purple500 else Purple700

val Colors.ButtonColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Purple500 else Purple700

val Colors.DialogNoText: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Purple700 else Color.White