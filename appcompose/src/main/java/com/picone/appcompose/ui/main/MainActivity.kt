package com.picone.appcompose.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.picone.appcompose.ui.main.screen.add.AddScreen
import com.picone.appcompose.ui.main.screen.detail.DetailScreen
import com.picone.appcompose.ui.main.screen.home.homeProject.HomeProjectScreen
import com.picone.appcompose.ui.main.screen.home.homeTask.screens.HomeTaskScreen
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.core.domain.entity.Task
import com.picone.core.domain.navAction.NavActionManager
import com.picone.core.domain.navAction.NavObjects
import com.picone.core.util.Constants.CATEGORY
import com.picone.core.util.Constants.KEY_TASK
import com.picone.core.util.Constants.PROJECT
import com.picone.core.util.Constants.TASK
import com.picone.core.util.Constants.UnknownTask
import com.picone.newArchitectureViewModels.*
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import java.text.SimpleDateFormat
import java.util.*

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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
                        val homeViewModel: HomeViewModel by viewModels()

                        DisposableEffect(key1 = homeViewModel) {
                            homeViewModel.onStart(NavObjects.Home.destination)
                            onDispose { homeViewModel.onStop() }
                        }

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
                        val homeViewModel: HomeViewModel by viewModels()

                        DisposableEffect(key1 = homeViewModel) {
                            homeViewModel.onStart(NavObjects.Project.destination)
                            onDispose { homeViewModel.onStop() }
                        }

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
                        val detailViewModel: DetailViewModel by viewModels()
                        val selectedTask =
                            getTaskOrNull(backStackEntry = backStackEntry) ?: UnknownTask

                        DisposableEffect(key1 = detailViewModel) {
                            detailViewModel.onStart(selectedTask = selectedTask)
                            onDispose { detailViewModel.onStop() }
                        }

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
                        val addViewModel: AddViewModel by viewModels()

                        DisposableEffect(key1 = addViewModel) {
                            addViewModel.onStart()
                            onDispose { addViewModel.onStop() }
                        }

                        addViewModel.dispatchEvent(AddActions.OnAddCreated)
                        AddScreen(
                            state_nullableProjectToPassInTask = null,
                            state_addScreenDeadlineSelectedDate = addViewModel.mNewTaskSelectedDeadLine.value,
                            state_addScreenAllCategories = addViewModel.mAllCategories.value,
                            state_isOkButtonEnabled = addViewModel.mIsOkButtonEnable.value,
                            event_onAddScreenImportanceSelected = { importance ->
                                addViewModel.dispatchEvent(
                                    AddActions.OnAddScreenImportanceSelected(
                                        importance = importance
                                    )
                                )
                            },
                            event_onAddScreenCategorySelected = { category ->
                                addViewModel.dispatchEvent(
                                    AddActions.OnAddScreenCategorySelected(
                                        category = category
                                    )
                                )
                            },
                            event_onDatePickerIconClicked = {
                                showDatePicker(
                                    onDateSelected = { selectedDate ->
                                        addViewModel.dispatchEvent(
                                            AddActions.OnDatePickerIconClickedOnDateSelected(
                                                selectedDate = selectedDate
                                            )
                                        )
                                    }
                                )
                            },
                            event_addScreenOnNameChange = { name ->
                                addViewModel.dispatchEvent(
                                    AddActions.AddScreenOnNameChange(
                                        name = name
                                    )
                                )
                            },
                            event_addScreenOnDescriptionChange = { description ->
                                addViewModel.dispatchEvent(
                                    AddActions.AddScreenOnDescriptionChange(
                                        description = description
                                    )
                                )
                            },
                            event_addScreenAddNewItemOnOkButtonClicked = {
                                addViewModel.dispatchEvent(
                                    addAction = AddActions.AddScreenAddNewItemOnOkButtonClicked(
                                        navActionManager = navActionManager
                                    )
                                )
                                if (addViewModel.completionState.value == AddViewModel.CompletionState.ON_COMPLETE)
                                    addViewModel.dispatchEvent(
                                        AddActions.NavigateToHomeOnAddTaskComplete(
                                            navActionManager = navActionManager
                                        )
                                    )
                            }
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