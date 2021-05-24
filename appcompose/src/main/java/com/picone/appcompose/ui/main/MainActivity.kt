package com.picone.appcompose.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.*
import com.google.gson.Gson
import com.picone.appcompose.ui.main.navAction.NavActionManager
import com.picone.appcompose.ui.main.navAction.NavObjects
import com.picone.appcompose.ui.main.screen.*
import com.picone.appcompose.ui.main.screen.home.homeProject.HomeProjectScreen
import com.picone.appcompose.ui.main.screen.home.homeTask.screens.HomeTaskScreen
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.core.domain.entity.Task
import com.picone.core.util.Constants.CATEGORY
import com.picone.core.util.Constants.KEY_TASK
import com.picone.core.util.Constants.PROJECT
import com.picone.core.util.Constants.TASK
import com.picone.core.util.Constants.UnknownTask
import com.picone.newArchitectureViewModels.DetailScreenViewModel
import com.picone.newArchitectureViewModels.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val homeScreenViewModel: HomeScreenViewModel by viewModels()
    private val detailScreenViewModel: DetailScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeScreenViewModel.getAllTasks()
        homeScreenViewModel.getAllProjects()

        setContent {
            TaskManagerTheme {
                val navController = rememberNavController()
                val navActionManager = NavActionManager(navController)
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                NavHost(
                    navController = navController,
                    startDestination = NavObjects.Home.destination
                ) {
                    composable(NavObjects.Home.getRoute()) {
                        HomeTaskScreen(
                            state_allTasks = homeScreenViewModel.mAllTasksMutableLD.observeAsState(listOf()).value,
                            event_taskRecyclerViewOnTaskSelected = { selectedTask -> navActionManager.navigate(NavObjects.Detail,selectedTask) },
                            state_topBarAddMenuItems = listOf(CATEGORY, PROJECT, TASK),
                            event_topBarOnMenuItemSelected = { selectedAddItem -> navActionManager.navigate(NavObjects.Add) },
                            event_bottomNavBarOnNavItemSelected = { selectedNavItem ->
                                homeScreenViewModel.updateBottomNavSelectedItem(selectedNavItem)
                                navActionManager.onBottomNavItemSelected(selectedNavItem)
                            },
                            state_currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                        )
                    }
                    composable(NavObjects.Project.getRoute()) {
                        HomeProjectScreen(
                            state_allProjects = homeScreenViewModel.mAllProjectMutableLD.observeAsState(
                                listOf()
                            ).value,
                            state_topBarAddMenuItems = listOf(CATEGORY, PROJECT, TASK),
                            event_topBarOnMenuItemSelected = { selectedAddItem -> navActionManager.navigate(NavObjects.Add) },
                            event_bottomNavBarOnNavItemSelected = { selectedNavItem ->
                                homeScreenViewModel.updateBottomNavSelectedItem(selectedNavItem)
                                navActionManager.onBottomNavItemSelected(selectedNavItem)
                            },
                            state_currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                        )
                    }
                    composable(
                        route = NavObjects.Detail.getRoute(),
                        arguments = NavObjects.Detail.arguments
                    ) { backStackEntry ->
                        detailScreenViewModel.getAllUnderStainForTask(
                            getTaskOrNull(backStackEntry) ?: UnknownTask
                        )
                        DetailScreen(
                            state_Task = getTaskOrNull(backStackEntry) ?: UnknownTask,
                            state_allUnderStainsForTask = detailScreenViewModel.allUnderStainsForTaskMutableLD.observeAsState(
                                listOf()
                            ).value,
                            state_isAddUnderStainComponentVisible = false,
                            state_isOkButtonEnable = false,
                            state_addUnderStainItemName = "",
                            state_addUnderStainItemDescription = "",
                            state_datePickerIconDateText = "",
                            event_onAddUnderStainButtonClick = {},
                            event_AddUnderStainButtonOnOkButtonClicked = {},
                            event_AddUnderStainButtonOnCancelButtonClicked = {},
                            event_baseEditTextOnTextChange = {},
                            event_onDatePickerIconClicked = {}
                        )

                    }
                    composable(NavObjects.Add.getRoute())
                    {
                        AddScreen()
                    }

                }

            }
        }
    }

    @Composable
    private fun getTaskOrNull(backStackEntry: NavBackStackEntry): Task? {
        var task: Task? = null
        backStackEntry.arguments?.getString(KEY_TASK)?.let { json ->
            task = Gson().fromJson(json, Task::class.java)
        }
        return task
    }
}

