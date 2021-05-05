package com.picone.appcompose.ui.component.screen

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.picone.appcompose.ui.component.baseComponent.AppBar
import com.picone.appcompose.ui.component.baseComponent.Fab
import com.picone.appcompose.ui.component.baseComponent.TaskRecyclerView
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.core.domain.entity.CompleteTask


@Composable
fun Home(items: List<CompleteTask>) {
    Scaffold(
        topBar = { AppBar() {} },
        content = { TaskRecyclerView(items,"all") },
    )
}