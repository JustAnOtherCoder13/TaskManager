package com.picone.appcompose.ui.component.manager.action.nav

import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.picone.appcompose.ui.component.manager.action.Action

interface NavAction:Action {

    val destination: String
        get() = ""
    val argument: String
        get() = ""

    fun doNavAction(navController: NavController){
        doAction {
            if (argument.trim().isNotEmpty())
                navController.navigate("$destination/${argument}")
            else
                navController.navigate(destination)
        }
    }
}