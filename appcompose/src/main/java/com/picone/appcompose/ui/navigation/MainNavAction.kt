package com.picone.appcompose.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.picone.appcompose.ui.navigation.MainDestinations.ADD
import com.picone.appcompose.ui.navigation.MainDestinations.DETAIL
import com.picone.appcompose.ui.navigation.MainDestinations.HOME
import com.picone.core.domain.entity.Task


// HOME SCREEN
fun <T>  navigateToDetailOnTaskClicked  (navController: NavController, item : T): () -> Unit{
    fun navigateToTask(task: Task) {
        navController.navigate("$DETAIL/${task.id}")
    }
    return { if (item is Task) navigateToTask (item as Task) }
}
//ADD NEW ITEM NAV
fun navigateToAddScreenOnAddItemClicked(navController: NavController, itemType : String){
    navController.navigate("$ADD/$itemType")
}


//ADD SCREEN
fun navigateToHomeOnAddNewItem (navController: NavController){
    navController.navigate(HOME)
}