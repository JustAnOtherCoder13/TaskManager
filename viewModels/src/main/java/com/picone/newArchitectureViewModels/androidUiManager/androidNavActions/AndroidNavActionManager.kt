package com.picone.newArchitectureViewModels.androidUiManager.androidNavActions

import androidx.navigation.NavController

class AndroidNavActionManager(private val navController: NavController) {

    fun navigate(androidNavAction : AndroidNavAction){
        when(androidNavAction){
            AndroidNavDirections.Home -> AndroidNavDirections.Home.doNavAction(navController)
            AndroidNavDirections.Project -> AndroidNavDirections.Project.doNavAction(navController)
            else -> throw Exception("Unknown nav action in ${this.javaClass.simpleName}")
        }
    }

    fun navigate(androidNavAction : AndroidNavAction, argument : String){
        when(androidNavAction){
            AndroidNavDirections.Detail -> AndroidNavDirections.Detail.doNavAction(navController,argument)
            else -> throw Exception("Unknown nav action in ${this.javaClass.simpleName}")
        }
    }

    fun navigate(androidNavAction : AndroidNavAction, argument : String, secondaryArgument : String, thirdArgument : String){
        when(androidNavAction){
            AndroidNavDirections.Add -> AndroidNavDirections.Add.doNavAction(navController,argument, secondaryArgument, thirdArgument)
            else -> throw Exception("Unknown nav action in ${this.javaClass.simpleName}")
        }
    }

    fun onBottomNavItemSelected(selectedItem: String) {
        when (selectedItem) {
            AndroidNavDirections.Home.destination -> AndroidNavDirections.Home.doNavAction(navController)
            AndroidNavDirections.Project.destination -> AndroidNavDirections.Project.doNavAction(navController)
            else -> throw Exception("Unknown nav action in ${this.javaClass.simpleName}")
        }
    }
}
