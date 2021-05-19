package com.picone.appcompose.ui.component.manager.action.navAction

import android.util.Log
import androidx.navigation.NavController


class NavActionImpl : NavAction{

    fun navigateToDetail(taskToJson : String):NavAction{
        return object : NavAction{
            override val destination: String = NavigationDirections.Detail.destination
            override val argument: String = taskToJson
        }
    }

    fun navigateToAdd():NavAction{
        return object : NavAction{
            override val destination: String = NavigationDirections.Add.destination
        }
    }

    fun navigateToHome():NavAction{
        return object :NavAction{
            override val destination: String= NavigationDirections.Home.destination
        }
    }

    fun navigateToProject():NavAction{
        return object :NavAction{
            override val destination: String= NavigationDirections.Project.destination
        }
    }
}