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
import com.picone.appcompose.ui.component.navAction.NavActionManager
import com.picone.appcompose.ui.component.navAction.NavObjects
import com.picone.appcompose.ui.main.screen.*
import com.picone.appcompose.ui.main.screen.home.homeTask.actions.HomeActionManager
import com.picone.appcompose.ui.main.screen.home.homeTask.states.HomeTaskUiStates
import com.picone.appcompose.ui.main.screen.home.homeProject.HomeProjectScreen
import com.picone.appcompose.ui.main.screen.home.homeTask.*
import com.picone.appcompose.ui.main.screen.home.homeTask.events.HomeEvents
import com.picone.appcompose.ui.main.screen.home.homeTask.events.HomeTaskEventManager
import com.picone.appcompose.ui.main.screen.home.homeTask.states.HomeStateManager
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
                val homeActionManager = HomeActionManager(navActionManager)
                val homeStateManager = HomeStateManager(homeScreenViewModel)
                val homeEventManager = HomeTaskEventManager(homeStateManager)
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                NavHost(
                    navController = navController,
                    startDestination = NavObjects.Home.destination
                ) {
                    composable(NavObjects.Home.getRoute()) {

                        homeEventManager.TriggerEvent(event = HomeEvents.onScreenCreated)

                        //homeStateManager.updateState(event = HomeTaskEvents.GET_INITIAL_STATE).state

                        HomeTaskScreen(
                            allTasks = homeStateManager.getState(HomeTaskUiStates.ALL_TASKS),
                            taskRecyclerViewOnTaskSelected = { selectedTask ->
                                homeActionManager.navigateToDetailOnTaskClicked(
                                    selectedTask
                                )
                            },
                            topAppBarAddItemButtonPopUpItems = listOf(CATEGORY, PROJECT, TASK),
                            topAppBarAddItemButtonIsPopUpMenuExpanded = homeScreenViewModel.mPopUpStateMutableLD.observeAsState(
                                false
                            ).value,
                            topAppBarAddItemButtonOnAddButtonClick = { homeActionManager.topAppBarOpenPopUp(homeScreenViewModel) },
                            topAppBarAddItemButtonOnClosePopUp = { homeActionManager.topAppBarClosePopUp(homeScreenViewModel) },
                            topAppBarAddItemButtonOnAddItemSelected = { selectedAddItem -> homeActionManager.navigateToAddOnPopUpItemSelected() },
                            bottomNavBarSelectedNavItem = homeScreenViewModel.mBottomNavSelectedItem.observeAsState(
                                ""
                            ).value,
                            bottomNavBarOnNavItemSelected = { selectedNavItem ->
                                homeActionManager.onBottomNavItemSelected(
                                    selectedNavItem,
                                    homeScreenViewModel
                                )
                            },
                            currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                        )
                    }
                    composable(NavObjects.Project.getRoute()) {
                        HomeProjectScreen(
                            allProjects = homeScreenViewModel.mAllProjectMutableLD.observeAsState(
                                listOf()
                            ).value,
                            topAppBarAddItemButtonPopUpItems = listOf(CATEGORY, PROJECT, TASK),
                            topAppBarAddItemButtonIsPopUpMenuExpanded = homeScreenViewModel.mPopUpStateMutableLD.observeAsState(
                                false
                            ).value,
                            topAppBarAddItemButtonOnAddButtonClick = { homeActionManager.topAppBarOpenPopUp(homeScreenViewModel) },
                            topAppBarAddItemButtonOnClosePopUp = { homeActionManager.topAppBarClosePopUp(homeScreenViewModel) },
                            topAppBarAddItemButtonOnAddItemSelected = { selectedAddItem -> homeActionManager.navigateToAddOnPopUpItemSelected() },
                            bottomNavBarSelectedNavItem = homeScreenViewModel.mBottomNavSelectedItem.observeAsState(
                                NavObjects.Home.destination
                            ).value,
                            bottomNavBarOnNavItemSelected = { selectedNavItem ->
                                homeActionManager.onBottomNavItemSelected(
                                    selectedNavItem,
                                    homeScreenViewModel
                                )
                            },
                            currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
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
                            state_allUnderStainsForTask = detailScreenViewModel.allUnderStainsForTaskMutableLD.observeAsState(listOf()).value,
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

