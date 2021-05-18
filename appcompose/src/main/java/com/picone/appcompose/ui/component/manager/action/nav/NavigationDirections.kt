package com.picone.appcompose.ui.component.manager.action.nav

import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument
import com.picone.core.util.Constants.KEY_ITEM
import com.picone.core.util.Constants.KEY_TASK

object NavigationDirections {

    val Home = object : NavigationCommand {
        override val arguments: List<NamedNavArgument> = emptyList()
        override val destination: String = "home"
    }

    val Project = object : NavigationCommand {
        override val arguments: List<NamedNavArgument> = emptyList()
        override val destination: String = "project"
    }

    val Detail = object : NavigationCommand {
        override val arguments: List<NamedNavArgument> = listOf(navArgument(KEY_TASK){type = NavType.StringType})
        override val destination: String = "detail"
    }

    val Add = object : NavigationCommand {
        override val arguments: List<NamedNavArgument> = listOf(navArgument(KEY_ITEM){type = NavType.StringType})
        override val destination: String = "add"
    }
}
