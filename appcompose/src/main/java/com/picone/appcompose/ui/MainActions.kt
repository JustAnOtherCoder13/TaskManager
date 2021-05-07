package com.picone.appcompose.ui

import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.picone.appcompose.ui.MainDestinations.HOME

class MainActions(navController: NavController) {

    val homeScreen : () -> Unit = {
        navController.navigate(HOME)
    }
}