package com.picone.appcompose.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument
import com.picone.core.domain.entity.Task

object NavigationDirections {

    val Home = object : NavigationCommand{
        override val arguments: List<NamedNavArgument> = emptyList()
        override val destination: String = "home"
    }
    val Detail = object : NavigationCommand{
        override val arguments: List<NamedNavArgument> = emptyList()
        override val destination: String = "detail"
    }
}
object DetailNavigation{
    private val KEY_TASK = "task"
    val route = "${NavigationDirections.Detail.destination}/{$KEY_TASK}"
    val arguments_ : List<NamedNavArgument> = listOf(navArgument(KEY_TASK){type = NavType.StringType})

    fun detailDestination(task:Task)= object : NavigationCommand{

        override val arguments: List<NamedNavArgument> = arguments_

        override val destination: String
            get() = TODO("Not yet implemented")

    }
}