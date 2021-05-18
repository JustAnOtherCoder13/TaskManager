package com.picone.appcompose.ui.component.manager.action.nav

import androidx.navigation.NavController
import com.google.gson.Gson
import com.picone.core.domain.entity.Task

class NavActionManager(private val navController: NavController) {

    private val navActionImpl: NavActionImpl = NavActionImpl()

    fun navigateToDetail(task: Task) {
        val taskToJson = Gson().toJson(task)
        return navActionImpl.navigateToDetail(taskToJson = taskToJson).doNavAction(navController = navController )
    }

    fun navigateToAdd(){
        return navActionImpl.navigateToAdd().doNavAction(navController = navController)
    }
}
