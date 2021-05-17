package com.picone.appcompose.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.picone.appcompose.ui.component.screen.HomeScreen
import com.picone.appcompose.ui.component.screen.ProjectRecyclerView
import com.picone.appcompose.ui.component.screen.TaskRecyclerView
import com.picone.appcompose.ui.navigation.DetailNavigation
import com.picone.appcompose.ui.navigation.NavigationDirections
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.newArchitectureViewModels.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val homeScreenViewModel: HomeScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeScreenViewModel.getAllTasks()
        homeScreenViewModel.getAllProjects()

        setContent {
            TaskManagerTheme {
                val navController = rememberNavController()
                val allTasks: List<Task> by homeScreenViewModel.mAllTasksMutableLD.observeAsState(
                    listOf()
                )
                val allProjects: List<Project> by homeScreenViewModel.mAllProjectMutableLD.observeAsState(
                    listOf()
                )
                NavHost(
                    navController = navController,
                    startDestination = NavigationDirections.Home.destination
                ) {
                    composable(NavigationDirections.Home.destination) {
                        var bottomNavSelectedItemState by remember {
                            mutableStateOf(NavigationDirections.Home.destination)
                        }
                        HomeScreen(
                            onBottomNavItemSelected = { selectedItem ->
                                bottomNavSelectedItemState = selectedItem
                            },
                            onAddItemSelected = { selectedAddItem -> },
                            mainContent = {
                                MainContentNavigation(
                                    bottomNavSelectedItemState,
                                    allTasks,
                                    allProjects,
                                    onTaskSelected = { selectedTask ->
                                        //nav to detail
                                        val task = Gson().toJson(selectedTask)
                                        Log.i("TAG", "onCreate: $task")
                                        //navController.navigate("${NavigationDirections.Detail.destination}/{$task}")
                                    }
                                )
                            }
                        )
                    }
                    composable(
                        route = DetailNavigation.route,
                        arguments = DetailNavigation.arguments_
                    ) { backStackEntry ->
                        backStackEntry.arguments?.getString("task")?.let { json ->
                            val task = Gson().fromJson(json,Task::class.java)
                            Log.i("TAG", "onCreate: $task")
                            //DetailScreen(task = task)
                        }

                    }

                }
            }
        }
    }

    @Composable
    private fun MainContentNavigation(
        bottomNavSelectedItemState: String,
        allTasks: List<Task>,
        allProjects: List<Project>,
        onTaskSelected: (task: Task) -> Unit
    ) {
        if (bottomNavSelectedItemState == NavigationDirections.Home.destination)
            TaskRecyclerView(
                allTasks,
                "all",
                onTaskSelected = { task -> onTaskSelected(task) },
            )
        else ProjectRecyclerView(allProjects = allProjects)
    }
}