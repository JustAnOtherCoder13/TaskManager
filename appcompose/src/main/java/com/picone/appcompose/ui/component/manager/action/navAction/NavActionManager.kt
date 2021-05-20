package com.picone.appcompose.ui.component.manager.action.navAction

import androidx.navigation.NavController
import com.google.gson.Gson

class NavActionManager(private val navController: NavController) {

    fun navigate(navAction : NavActions){
        when(navAction){
            NavActions.NAV_TO_HOME -> navigateToHome()
            NavActions.NAV_TO_PROJECT-> navigateToProject()
            NavActions.NAV_TO_ADD -> navigateToAdd()
            else -> throw Exception("Unknown nav action in ${this.javaClass.simpleName}")
        }
    }
    fun <T> navigate(navAction : NavActions, argument : T){
        val argumentToJson = Gson().toJson(argument)
        when(navAction){
            NavActions.NAV_TO_DETAIL -> navigateToDetail(taskToJson = argumentToJson )
            else -> throw Exception("Unknown nav action in ${this.javaClass.simpleName}")
        }
    }

    private fun navigateToHome() { NavActions.NAV_TO_HOME.getNavAction().doNavAction(navController) }

    private fun navigateToProject() { NavActions.NAV_TO_PROJECT.getNavAction().doNavAction(navController) }

    private fun navigateToDetail(taskToJson: String) { NavActions.NAV_TO_DETAIL.getNavAction().doNavAction(navController,taskToJson) }

    private fun navigateToAdd() { NavActions.NAV_TO_ADD.getNavAction().doNavAction(navController) }

}
