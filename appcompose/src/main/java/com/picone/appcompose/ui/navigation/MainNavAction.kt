package com.picone.appcompose.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.picone.appcompose.ui.navigation.MainDestinations.ADD
import com.picone.appcompose.ui.navigation.MainDestinations.DETAIL
import com.picone.appcompose.ui.navigation.MainDestinations.HOME
import com.picone.appcompose.ui.navigation.MainDestinations.PROJECT
import com.picone.core.domain.entity.Task

// HOME TO DETAIL
fun <T>  navigateToDetailOnTaskClicked  (navController: NavController, item : T): () -> Unit{
    fun navigateToTask(task: Task) {
        navController.navigate("$DETAIL/${task.id}")
    }
    return { if (item is Task) navigateToTask (item as Task) }
}
//TOP BAR ADD TO ADD SCREEN
fun navigateToAddScreenOnAddItemClicked(navController: NavController, itemType : String){
    navController.navigate("$ADD/$itemType")
}

//HOME SCREEN
fun navigateToHome (navController: NavController){
    navController.navigate(HOME)
}

//PROJECT SCREEN
fun navigateToProject(navController: NavController){
    navController.navigate(PROJECT)
}