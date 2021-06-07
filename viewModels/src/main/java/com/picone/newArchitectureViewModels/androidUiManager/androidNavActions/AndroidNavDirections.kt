package com.picone.newArchitectureViewModels.androidUiManager.androidNavActions

import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument
import com.picone.core.util.Constants
import com.picone.core.util.Constants.KEY_EDIT_PROJECT
import com.picone.core.util.Constants.KEY_EDIT_TASK
import com.picone.core.util.Constants.KEY_ITEM

object AndroidNavDirections {

    val Home = object : AndroidNavAction {
        override val destination: String = "home"
        override val arguments: List<NamedNavArgument> = emptyList()
    }
    val Project = object : AndroidNavAction {
        override val arguments: List<NamedNavArgument> = emptyList()
        override val destination: String = "project"
    }

    val Detail = object : AndroidNavAction {
        override val KEY: String = Constants.KEY_TASK
        override val arguments: List<NamedNavArgument> =
            listOf(navArgument(KEY) { type = NavType.StringType })
        override val destination: String = "detail"
    }

    val Add = object : AndroidNavAction {
        override val KEY: String = KEY_ITEM
        override val arguments: List<NamedNavArgument> =
            listOf(navArgument(KEY) { type = NavType.StringType },
                navArgument(KEY_EDIT_TASK) { type = NavType.StringType },
                navArgument(KEY_EDIT_PROJECT){ type = NavType.StringType}
                )
        override val destination: String = "add"
    }
}