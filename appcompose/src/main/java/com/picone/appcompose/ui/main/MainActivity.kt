package com.picone.appcompose.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.picone.appcompose.ui.component.manager.action.nav.NavActionManager
import com.picone.appcompose.ui.component.screen.*
import com.picone.appcompose.ui.component.manager.action.nav.NavigationDirections
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.util.Constants.KEY_TASK
import com.picone.core.util.Constants.UnknownTask
import com.picone.newArchitectureViewModels.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val homeScreenViewModel: HomeScreenViewModel by viewModels()
    lateinit var navActionManager: NavActionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeScreenViewModel.getAllTasks()
        homeScreenViewModel.getAllProjects()

        setContent {
            TaskManagerTheme {
                val navController = rememberNavController()
                navActionManager = NavActionManager(navController)
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
                            onAddItemSelected = { selectedAddItem -> navActionManager.navigateToAdd() },
                            mainContent = {
                                MainContentNavigation(
                                    bottomNavSelectedItemState,
                                    allTasks,
                                    allProjects,
                                    onTaskSelected = { selectedTask ->
                                        navActionManager.navigateToDetail(
                                            selectedTask
                                        )
                                    }
                                )
                            }
                        )
                    }
                    composable(
                        route = "${NavigationDirections.Detail.destination}/{${KEY_TASK}}",
                        arguments = NavigationDirections.Detail.arguments
                    ) { backStackEntry ->
                        DetailScreen(task = getTaskOrNull(backStackEntry)?: UnknownTask)

                    }
                    composable(NavigationDirections.Add.destination)
                    {
                        AddScreen()
                    }

                }

            }
        }
    }

    @Composable
    private fun getTaskOrNull(backStackEntry: NavBackStackEntry): Task? {
        var task:Task? = null
        backStackEntry.arguments?.getString(KEY_TASK)?.let { json ->
             task = Gson().fromJson(json, Task::class.java)
        }
        return task
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
