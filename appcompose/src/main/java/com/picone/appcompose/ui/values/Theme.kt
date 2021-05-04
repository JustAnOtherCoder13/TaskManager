package com.picone.appcompose.ui.values

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun TaskManagerTheme(
    content : @Composable () -> Unit,
    darkTheme : Boolean = isSystemInDarkTheme()
    ){
    MaterialTheme (
        colors = if (darkTheme)DarkColors else LightColors,
        typography = TaskManagerTypography,
        shapes = TaskManagerShapes,
        content = content
            )
}