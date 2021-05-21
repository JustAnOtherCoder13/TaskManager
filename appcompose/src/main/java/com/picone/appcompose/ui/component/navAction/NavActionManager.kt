package com.picone.appcompose.ui.component.navAction

import androidx.navigation.NavController
import com.google.gson.Gson

class NavActionManager(private val navController: NavController) {

    fun navigate(navAction : NavAction){
        when(navAction){
            NavObjects.Home -> NavObjects.Home.doNavAction(navController)
            NavObjects.Project-> NavObjects.Project.doNavAction(navController)
            NavObjects.Add -> NavObjects.Add.doNavAction(navController)
            else -> throw Exception("Unknown nav action in ${this.javaClass.simpleName}")
        }
    }

    fun <T> navigate(navAction : NavAction, argument : T){
        val argumentToJson = Gson().toJson(argument)
        when(navAction){
            NavObjects.Detail -> NavObjects.Detail.doNavAction(navController,argumentToJson)
            else -> throw Exception("Unknown nav action in ${this.javaClass.simpleName}")
        }
    }
}
