package com.picone.appcompose.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.picone.core.domain.navAction.NavActionManager
import com.picone.core.domain.navAction.NavObjects
import com.picone.appcompose.ui.main.screen.add.AddScreen
import com.picone.appcompose.ui.main.screen.detail.DetailScreen
import com.picone.appcompose.ui.main.screen.home.homeProject.HomeProjectScreen
import com.picone.appcompose.ui.main.screen.home.homeTask.screens.HomeTaskScreen
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.core.domain.entity.Task
import com.picone.core.util.Constants.CATEGORY
import com.picone.core.util.Constants.KEY_TASK
import com.picone.core.util.Constants.PROJECT
import com.picone.core.util.Constants.TASK
import com.picone.core.util.Constants.UnknownTask
import com.picone.newArchitectureViewModels.DetailActions
import com.picone.newArchitectureViewModels.DetailViewModel
import com.picone.newArchitectureViewModels.HomeActions
import com.picone.newArchitectureViewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import java.text.SimpleDateFormat
import java.util.*

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                        homeViewModel.dispatchEvent(HomeActions.OnHomeCreated)
                        HomeTaskScreen(
                            state_allTasks = homeViewModel.mAllTasksState.value,
                            event_taskRecyclerViewOnTaskSelected = { selectedTask ->
                                homeViewModel.dispatchEvent(
                                    HomeActions.TaskRecyclerViewOnTaskSelected(
                                        navActionManager = navActionManager,
                                        selectedTask = selectedTask
                                    )
                                )
                            },
                            state_topBarAddMenuItems = topBarrAddMenuItems(),
                            event_topBarOnMenuItemSelected = { selectedAddItem ->
                                homeViewModel.dispatchEvent(
                                    HomeActions.TopBarOnMenuItemSelected(
                                        navActionManager = navActionManager,
                                        selectedItem = selectedAddItem
                                    )
                                )
                            },
                            event_bottomNavBarOnNavItemSelected = { selectedNavItem ->
                                homeViewModel.dispatchEvent(
                                    HomeActions.BottomNavBarOnNavItemSelected(
                                        navActionManager = navActionManager,
                                        selectedNavItem = selectedNavItem
                                    )
                                )
                            },
                            state_currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                        )
                    }
                    composable(NavObjects.Project.getRoute()) {
                        homeViewModel.dispatchEvent(HomeActions.OnProjectCreated)
                        HomeProjectScreen(
                            state_allProjects = homeViewModel.mAllProjectState.value,
                            state_topBarAddMenuItems = topBarrAddMenuItems(),
                            event_topBarOnMenuItemSelected = { selectedAddItem ->
                                homeViewModel.dispatchEvent(
                                    HomeActions.TopBarOnMenuItemSelected(
                                        navActionManager = navActionManager,
                                        selectedItem = selectedAddItem
                                    )
                                )
                            },
                            event_bottomNavBarOnNavItemSelected = { selectedNavItem ->
                                homeViewModel.dispatchEvent(
                                    HomeActions.BottomNavBarOnNavItemSelected(
                                        navActionManager = navActionManager,
                                        selectedNavItem = selectedNavItem
                                    )
                                )
                            },
                            state_currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                        )
                    }
                    composable(
                        route = NavObjects.Detail.getRoute(),
                        arguments = NavObjects.Detail.arguments
                    ) { backStackEntry ->
                        detailViewModel.dispatchEvent(
                            DetailActions.OnDetailCreated(
                                getTaskOrNull(backStackEntry) ?: UnknownTask
                            )
                        )
                        DetailScreen(
                            state_Task = getTaskOrNull(backStackEntry) ?: UnknownTask,
                            state_allUnderStainsForTask = detailViewModel.mAllUnderStainsForTaskState.value,
                            state_isAddUnderStainComponentVisible = detailViewModel.mIsAddUnderStainComponentVisible.value,
                            state_datePickerIconDateText = detailViewModel.mNewUnderStainSelectedDeadLine.value,
                            event_onAddUnderStainButtonClick = {
                                detailViewModel.dispatchEvent(
                                    DetailActions.OnAddUnderStainButtonClick
                                )
                            },
                            event_AddUnderStainButtonOnOkButtonClicked = {
                                detailViewModel.dispatchEvent(
                                    DetailActions.AddUnderStainButtonOnOkButtonClicked
                                )
                            },
                            event_AddUnderStainButtonOnCancelButtonClicked = {
                                detailViewModel.dispatchEvent(
                                    DetailActions.AddUnderStainButtonOnCancelButtonClicked
                                )
                            },
                            event_nameEditTextOnTextChange = { name ->
                                detailViewModel.dispatchEvent(
                                    DetailActions.NameEditTextOnTextChange(name)
                                )
                            },
                            event_descriptionEditTextOnTextChange = { description ->
                                detailViewModel.dispatchEvent(
                                    DetailActions.DescriptionEditTextOnTextChange(description)
                                )
                            },
                            event_onDatePickerIconClicked = {
                                showDatePicker(
                                    onDateSelected = { selectedDate ->
                                        detailViewModel.dispatchEvent(
                                            DetailActions.OnDatePickerIconClickedOnDateSelected(
                                                selectedDate = selectedDate
                                            )
                                        )
                                    }
                                )
                            }
                        )

                    }
                    composable(NavObjects.Add.getRoute())
                    {
                        AddScreen(
                            state_nullableProjectToPassInTask = null,
                            state_addScreenDeadlineSelectedDate = "",
                            state_addScreenAllCategories = listOf(),
                            state_isOkButtonEnabled = false,
                            event_onAddScreenImportanceSelected = {},
                            event_onAddScreenCategorySelected = { },
                            event_showDatePicker = {
                                showDatePicker(
                                    onDateSelected = { selectedDate -> }
                                )
                            },
                            event_addScreenOnNameChange = {},
                            event_addScreenOnDescriptionChange = {},
                            event_addScreenAddNewItemOnOkButtonClicked = {}
                        )
                    }

                }

            }
        }
    }

    @Composable
    private fun topBarrAddMenuItems() = listOf(CATEGORY, PROJECT, TASK)

    @Composable
    private fun getTaskOrNull(backStackEntry: NavBackStackEntry): Task? {
        var task: Task? = null
        backStackEntry.arguments?.getString(KEY_TASK)?.let { json ->
            task = Gson().fromJson(json, Task::class.java)
        }
        return task
    }

    private fun showDatePicker(
        onDateSelected: (String) -> Unit
    ) {
        val picker = MaterialDatePicker.Builder.datePicker().build()
        picker.show(supportFragmentManager, picker.toString())

        picker.addOnPositiveButtonClickListener {
            onDateSelected(SimpleDateFormat("dd/MM/yyy", Locale.FRANCE).format(it))
        }
        picker.addOnDismissListener {}
    }
}

