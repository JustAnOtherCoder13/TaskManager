package com.picone.viewModels.androidUiManager.androidNavActions

import androidx.navigation.NavController
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navigate
import com.picone.core.util.Constants

interface AndroidNavAction {

    val destination: String
    val arguments: List<NamedNavArgument>
    val KEY: String
        get() = ""

    fun doNavAction(navController: NavController) {
        navController.navigate(destination) {
            popUpTo(navController.graph.startDestination) {}
            launchSingleTop = true
        }
    }

    fun doNavAction(navController: NavController, argument: String) {
        navController.navigate("$destination/${argument}") {
            popUpTo(navController.graph.startDestination) {}
            launchSingleTop = true
        }
    }

    fun getRoute(): String =
        if (KEY.trim().isNotEmpty()) "${destination}/{${Constants.KEY_TASK}}"
         else destination

}
