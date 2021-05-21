package com.picone.appcompose.ui.component.navAction

import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument
import com.picone.core.util.Constants

object NavObjects {

    val Home = object : NavAction {
        override val destination: String = "home"
        override val arguments: List<NamedNavArgument> = emptyList()
    }
    val Project = object : NavAction {
        override val arguments: List<NamedNavArgument> = emptyList()
        override val destination: String = "project"
    }

    val Detail = object : NavAction {
        override val KEY: String = Constants.KEY_TASK
        override val arguments: List<NamedNavArgument> =
            listOf(navArgument(KEY) { type = NavType.StringType })
        override val destination: String = "detail"
    }

    val Add = object : NavAction {
        //override val KEY: String = KEY_ITEM
        override val arguments: List<NamedNavArgument> =
            emptyList() //listOf(navArgument(KEY){type = NavType.StringType})
        override val destination: String = "add"
    }
}