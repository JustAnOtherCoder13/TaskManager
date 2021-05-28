package com.picone.newArchitectureViewModels.androidUiManager.androidNavActions

import androidx.navigation.NavController
import com.google.gson.Gson

class AndroidNavActionManager(private val navController: NavController) {

    fun navigate(androidNavAction : AndroidNavAction){
        when(androidNavAction){
            AndroidNavObjects.Home -> AndroidNavObjects.Home.doNavAction(navController)
            AndroidNavObjects.Project -> AndroidNavObjects.Project.doNavAction(navController)
            else -> throw Exception("Unknown nav action in ${this.javaClass.simpleName}")
        }
    }

    fun navigate(androidNavAction : AndroidNavAction, argument : String){
        when(androidNavAction){
            AndroidNavObjects.Detail -> AndroidNavObjects.Detail.doNavAction(navController,argument)
            //AndroidNavObjects.Add -> AndroidNavObjects.Add.doNavAction(navController,argument)
            //AndroidNavObjects.AddFromEdit -> AndroidNavObjects.AddFromEdit.doNavAction(navController, argument)
            else -> throw Exception("Unknown nav action in ${this.javaClass.simpleName}")
        }
    }

    fun navigate(androidNavAction : AndroidNavAction, argument : String, secondaryArgument : String, thirdArgument : String){
        when(androidNavAction){
            AndroidNavObjects.Add -> AndroidNavObjects.Add.doNavAction(navController,argument, secondaryArgument, thirdArgument)
            else -> throw Exception("Unknown nav action in ${this.javaClass.simpleName}")
        }
    }

    fun onBottomNavItemSelected(selectedItem: String) {
        when (selectedItem) {
            AndroidNavObjects.Home.destination -> AndroidNavObjects.Home.doNavAction(navController)
            AndroidNavObjects.Project.destination -> AndroidNavObjects.Project.doNavAction(navController)
            else -> throw Exception("not allowed destination")
        }
    }
}
