package com.picone.appcompose.ui.component.manager.action.navAction

import android.util.Log
import androidx.navigation.NavController
import com.google.gson.Gson
import com.picone.core.domain.entity.Task

class NavActionManager(private val navController: NavController) {

    private val navActionImpl: NavActionImpl = NavActionImpl()

    fun navigateToDetail(task: Task) {
        val taskToJson = Gson().toJson(task)
        navActionImpl.navigateToDetail(taskToJson = taskToJson).doNavAction(navController = navController )
    }

    fun navigateToAdd(){
        navActionImpl.navigateToAdd().doNavAction(navController = navController)
    }

    fun navigateToHome(){
        navActionImpl.navigateToHome().doNavAction(navController = navController)
    }

    fun navigateToProject(){
        navActionImpl.navigateToProject().doNavAction(navController = navController)
    }
}
