package com.picone.newArchitectureViewModels.androidUiManager.androidNavActions

import android.util.Log
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
            AndroidNavObjects.Add -> {
                AndroidNavObjects.Add.doNavAction(navController, argument)
            }
            else -> throw Exception("Unknown nav action in ${this.javaClass.simpleName}")
        }
    }

    fun <T> navigate(androidNavAction : AndroidNavAction, argument : T){
        val argumentToJson = Gson().toJson(argument)
        when(androidNavAction){
            AndroidNavObjects.Detail -> AndroidNavObjects.Detail.doNavAction(navController,argumentToJson)
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
