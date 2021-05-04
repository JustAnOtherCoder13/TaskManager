package com.picone.appcompose.ui.values

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Blue700 = Color(0xff0d47a1)
val Blue600 = Color(0xff5472d3)
val Blue800 = Color(0xff002171)
val LightBlue700 = Color(0xff82b1ff)
val LightBlue600 = Color(0xffb6e3ff)
val LightBlue800 = Color(0xff4d82cb)
val Red700 = Color(0xffbf360c)
val Red600 = Color(0xfff9683a)

val LightColors = lightColors(
    primary = Blue700,
    primaryVariant = Blue800,
    onPrimary = Color.White,
    secondary = Blue600,
    secondaryVariant = Blue700,
    onSecondary = Color.White,
    error = Red700
)

val DarkColors = darkColors(
    primary = LightBlue700,
    primaryVariant = LightBlue800,
    onPrimary = Color.Black,
    secondary = LightBlue600,
    onSecondary = Color.Black,
    error = Red600
)
