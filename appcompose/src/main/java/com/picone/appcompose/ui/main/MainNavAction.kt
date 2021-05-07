package com.picone.appcompose.ui.main

import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.picone.appcompose.ui.MainDestinations
import com.picone.core.domain.entity.Task


// HOME SCREEN
fun <T>  navigateToDetailOnTaskClicked  (navController: NavController, item : T): () -> Unit{
    fun navigateToTask(task: Task) {
        navController.navigate("${MainDestinations.DETAIL}/${task.id}")
    }
    return { if (item is Task) navigateToTask (item as Task) }
}