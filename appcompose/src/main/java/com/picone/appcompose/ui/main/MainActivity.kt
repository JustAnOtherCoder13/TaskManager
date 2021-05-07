package com.picone.appcompose.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.picone.appcompose.ui.component.screen.AddScreen
import com.picone.appcompose.ui.component.screen.DetailScreen
import com.picone.appcompose.ui.component.screen.HomeScreen
import com.picone.appcompose.ui.navigation.MainDestinations.ADD
import com.picone.appcompose.ui.navigation.MainDestinations.DETAIL
import com.picone.appcompose.ui.navigation.MainDestinations.HOME
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.core.domain.entity.CompleteTask
import com.picone.core.util.Constants.FIRST_ELEMENT
import com.picone.core.util.Constants.TASK_ID
import com.picone.core.util.Constants.UnknownTask
import com.picone.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel.getAllTasks()

        setContent {
            TaskManagerTheme {
                val allTasks: List<CompleteTask> by taskViewModel.mAllTasksMutableLD.observeAsState(
                    listOf()
                )
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = HOME) {
                    composable(HOME) { HomeScreen(allTasks, navController = navController) }
                    composable(
                        "$DETAIL/{$TASK_ID}",
                        arguments = listOf(navArgument(TASK_ID) { type = NavType.IntType })
                    ) { backStackEntry ->
                        val taskId: Int = backStackEntry.arguments?.getInt(TASK_ID) ?: 0
                        DetailScreen(task = taskToPass(allTasks, taskId), navController = navController)
                    }
                    composable(ADD){AddScreen()}
                }
            }
        }

    }

    private fun taskToPass(allTasks: List<CompleteTask>, taskId: Int): CompleteTask {
        var taskToPass = CompleteTask(UnknownTask)
        if (allTasks.any { it.task.id == taskId }) {
            taskToPass = allTasks.filter { it.task.id == taskId }[FIRST_ELEMENT]
        }
        return taskToPass
    }
}