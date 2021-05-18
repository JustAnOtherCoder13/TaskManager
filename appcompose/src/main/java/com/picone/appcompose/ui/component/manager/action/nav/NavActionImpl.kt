package com.picone.appcompose.ui.component.manager.action.nav


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
}