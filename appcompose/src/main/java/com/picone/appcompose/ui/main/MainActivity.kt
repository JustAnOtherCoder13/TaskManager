package com.picone.appcompose.ui.main

import android.os.Bundle
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
import com.picone.appcompose.ui.component.manager.action.navAction.NavActionManager
import com.picone.appcompose.ui.component.screen.*
import com.picone.appcompose.ui.component.manager.action.navAction.NavigationDirections
import com.picone.appcompose.ui.component.screen.home.HomeActionManager
import com.picone.appcompose.ui.component.screen.home.homeProject.HomeProjectScreen
import com.picone.appcompose.ui.component.screen.home.homeTask.*
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
                val homeActionManager = HomeActionManager(homeScreenViewModel, navActionManager)

                NavHost(
                    navController = navController,
                    startDestination = NavigationDirections.Home.destination
                ) {
                    composable(NavigationDirections.Home.getRoute()) {
                        HomeTaskScreen(
                            allTasks = homeScreenViewModel.mAllTasksMutableLD.observeAsState(listOf()).value,
                            taskRecyclerViewOnTaskSelected = { selectedTask -> homeActionManager.navigateToDetailOnTaskClicked(selectedTask)},
                            topAppBarAddItemButtonPopUpItems = listOf(CATEGORY, PROJECT, TASK),
                            topAppBarAddItemButtonIsPopUpMenuExpanded = homeScreenViewModel.mPopUpStateMutableLD.observeAsState(false).value,
                            topAppBarAddItemButtonOnAddButtonClick = { homeActionManager.topAppBarOpenPopUp() },
                            topAppBarAddItemButtonOnClosePopUp = { homeActionManager.topAppBarClosePopUp() },
                            topAppBarAddItemButtonOnAddItemSelected = { selectedAddItem -> homeActionManager.navigateToAddOnPopUpItemSelected() },
                            bottomNavBarSelectedNavItem = homeScreenViewModel.mBottomNavSelectedItem.observeAsState("").value,
                            bottomNavBarOnNavItemSelected = { selectedNavItem -> homeActionManager.onBottomNavItemSelected(selectedNavItem) },
                            navController = navController
                        )
                    }
                    composable(NavigationDirections.Project.getRoute()) {
                        HomeProjectScreen(
                            allProjects = homeScreenViewModel.mAllProjectMutableLD.observeAsState(listOf()).value,
                            topAppBarAddItemButtonPopUpItems = listOf(CATEGORY, PROJECT, TASK),
                            topAppBarAddItemButtonIsPopUpMenuExpanded = homeScreenViewModel.mPopUpStateMutableLD.observeAsState(false).value,
                            topAppBarAddItemButtonOnAddButtonClick = { homeActionManager.topAppBarOpenPopUp() },
                            topAppBarAddItemButtonOnClosePopUp = { homeActionManager.topAppBarClosePopUp() },
                            topAppBarAddItemButtonOnAddItemSelected = { selectedAddItem -> homeActionManager.navigateToAddOnPopUpItemSelected() },
                            bottomNavBarSelectedNavItem = homeScreenViewModel.mBottomNavSelectedItem.observeAsState(NavigationDirections.Home.destination).value,
                            bottomNavBarOnNavItemSelected = { selectedNavItem -> homeActionManager.onBottomNavItemSelected(selectedNavItem) },
                            navController = navController
                        )
                    }
                    composable(
                        route = NavigationDirections.Detail.getRoute(),
                        arguments = NavigationDirections.Detail.arguments
                    ) { backStackEntry ->
                        detailScreenViewModel.getAllUnderStainForTask(
                            getTaskOrNull(backStackEntry) ?: UnknownTask
                        )
                        DetailScreen(
                            task = getTaskOrNull(backStackEntry) ?: UnknownTask,
                            allUnderStainsForTask = detailScreenViewModel.allUnderStainsForTaskMutableLD.observeAsState(
                                listOf()
                            ).value,
                            onAddUnderStainEvent = { nameState, descriptionState -> }
                        )

                    }
                    composable(NavigationDirections.Add.getRoute())
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

