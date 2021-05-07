package com.picone.appcompose.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.core.domain.entity.CompleteTask
import com.picone.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.picone.appcompose.R
import com.picone.appcompose.ui.MainDestinations.DETAIL
import com.picone.appcompose.ui.MainDestinations.HOME
import com.picone.appcompose.ui.component.screen.Detail
import com.picone.appcompose.ui.component.screen.Home
import com.picone.core.domain.entity.Task
import java.util.*

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
                    composable(HOME) { Home(allTasks, navController = navController) }
                    composable(
                        "$DETAIL/{taskId}",
                        arguments = listOf(
                            navArgument("taskId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val taskId : Int =  backStackEntry.arguments?.getInt("taskId") ?: 0
                        Detail(
                            task = allTasks[taskId], navController = navController
                           )


                    }
                }
            }
        }

    }
}