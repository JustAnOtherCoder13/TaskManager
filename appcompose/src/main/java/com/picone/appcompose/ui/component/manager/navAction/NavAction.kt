package com.picone.appcompose.ui.component.manager.navAction

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.picone.appcompose.ui.utils.interfaces.Action
import java.lang.Exception

interface NavAction : Action {

    val destination: String

    fun doNavAction(navController: NavController) {
        doAction {
            try {
                navController.navigate(destination) {
                    popUpTo(navController.graph.startDestination) {}
                    launchSingleTop = true
                }
            } catch (e: Exception) {
                Log.e(
                    "TAG",
                    "doNavAction: if KEY is define in NavigationDirection you have to pass argument in NavActionImpl ",
                    e
                )
            }
        }
    }
    fun doNavAction(navController: NavController, argument: String) {
        try {
            navController.navigate("$destination/${argument}") {
                popUpTo(navController.graph.startDestination) {}
                launchSingleTop = true
            }
        } catch (e: Exception) {
            Log.e(
                "TAG",
                "doNavAction: if KEY is define in NavigationDirection you have to pass argument in NavActionImpl ",
                e
            )
        }
    }
}