package com.picone.appcompose.ui.component.screen

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.picone.appcompose.ui.component.baseComponent.AppBar
import com.picone.appcompose.ui.component.baseComponent.Fab
import com.picone.appcompose.ui.values.TaskManagerTheme

@Preview
@Composable
fun Home() {
    Scaffold(
        topBar = { AppBar() {} },
        content = { Text(text = "test") },
    )
}