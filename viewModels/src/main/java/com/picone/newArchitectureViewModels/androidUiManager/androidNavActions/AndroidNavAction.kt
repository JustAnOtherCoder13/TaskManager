package com.picone.newArchitectureViewModels.androidUiManager.androidNavActions

import androidx.navigation.NavController
import androidx.navigation.compose.NamedNavArgument

interface AndroidNavAction {

    val destination: String
    val arguments: List<NamedNavArgument>
    val KEY: String
        get() = ""

    fun doNavAction(navController: NavController) {
        navController.navigate(destination) {
            popUpTo(AndroidNavObjects.Home.destination) {}
            launchSingleTop = true
        }
    }

    fun doNavAction(navController: NavController, argument: String) {
        navController.navigate("$destination/${argument}") {
            popUpTo(AndroidNavObjects.Home.destination) {}
            launchSingleTop = true
        }
    }

    fun doNavAction(navController: NavController, argument: String, secondArgument : String, thirdArgument : String) {
        navController.navigate("$destination/${argument}/${secondArgument}/${thirdArgument}") {
            popUpTo(AndroidNavObjects.Home.destination) {}
            launchSingleTop = true
        }
    }

    fun getRoute(): String =
        if (KEY.trim().isNotEmpty()) "${destination}/{${KEY}}"
         else destination

}
