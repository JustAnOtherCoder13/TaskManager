package com.picone.appcompose.ui.values

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun TaskManagerTheme(
    darkTheme : Boolean = isSystemInDarkTheme(),
    content : @Composable () -> Unit
    ){
    MaterialTheme (
        content = content,
        colors = if (darkTheme)DarkColors else LightColors,
        typography = TaskManagerTypography,
        shapes = TaskManagerShapes,
            )
}