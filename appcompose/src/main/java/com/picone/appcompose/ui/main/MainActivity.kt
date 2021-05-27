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
import com.picone.core.util.Constants.CATEGORY
import com.picone.core.util.Constants.KEY_TASK
import com.picone.core.util.Constants.PROJECT
import com.picone.core.util.Constants.TASK
import com.picone.core.util.Constants.UnknownTask
import com.picone.newArchitectureViewModels.*
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.AddActions
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.DetailActions
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.HomeActions
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavActionManager
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavObjects
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import java.text.SimpleDateFormat
import java.util.*

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val addViewModel: AddViewModel by viewModels()
        val homeViewModel: HomeViewModel by viewModels()
        val detailViewModel: DetailViewModel by viewModels()

        setContent {
            TaskManagerTheme {
                val navController = rememberNavController()
                val androidNavActionManager = AndroidNavActionManager(navController)
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                NavHost(
                    navController = navController,
                    startDestination = AndroidNavObjects.Home.destination
                ) {
                    composable(AndroidNavObjects.Home.getRoute()) {

                        DisposableEffect(key1 = homeViewModel) {
                            homeViewModel.onStart(AndroidNavObjects.Home.destination)
                            onDispose { homeViewModel.onStop() }
                        }

                        HomeTaskScreen(
                            state_allTasks = homeViewModel.mAllTasksState.value,
                            state_topBarAddMenuItems = topBarrAddMenuItems(),
                            state_currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE),

                            event_taskRecyclerViewOnTaskSelected = { selectedTask ->
                                homeViewModel.dispatchEvent(
                                    HomeActions.TaskRecyclerViewOnTaskSelected(
                                        androidNavActionManager = androidNavActionManager,
                                        selectedTask = selectedTask
                                    )
                                )
                            },
                            event_topBarOnMenuItemSelected = { selectedAddItem ->
                                homeViewModel.dispatchEvent(
                                    HomeActions.TopBarOnMenuItemSelected(
                                        androidNavActionManager = androidNavActionManager,
                                        selectedItem = selectedAddItem
                                    )
                                )
                            },
                            event_bottomNavBarOnNavItemSelected = { selectedNavItem ->
                                homeViewModel.dispatchEvent(
                                    HomeActions.BottomNavBarOnNavItemSelected(
                                        androidNavActionManager = androidNavActionManager,
                                        selectedNavItem = selectedNavItem
                                    )
                                )
                            }
                        )
                    }
                    composable(AndroidNavObjects.Project.getRoute()) {

                        DisposableEffect(key1 = homeViewModel) {
                            homeViewModel.onStart(AndroidNavObjects.Project.destination)
                            onDispose { homeViewModel.onStop() }
                        }

                        HomeProjectScreen(
                            state_allProjects = homeViewModel.mAllProjectState.value,
                            state_topBarAddMenuItems = topBarrAddMenuItems(),
                            event_topBarOnMenuItemSelected = { selectedAddItem ->
                                homeViewModel.dispatchEvent(
                                    HomeActions.TopBarOnMenuItemSelected(
                                        androidNavActionManager = androidNavActionManager,
                                        selectedItem = selectedAddItem
                                    )
                                )
                            },
                            event_bottomNavBarOnNavItemSelected = { selectedNavItem ->
                                homeViewModel.dispatchEvent(
                                    HomeActions.BottomNavBarOnNavItemSelected(
                                        androidNavActionManager = androidNavActionManager,
                                        selectedNavItem = selectedNavItem
                                    )
                                )
                            },
                            state_currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                        )
                    }
                    composable(
                        route = AndroidNavObjects.Detail.getRoute(),
                        arguments = AndroidNavObjects.Detail.arguments
                    ) { backStackEntry ->
                        val selectedTask = getTaskOrNull(backStackEntry = backStackEntry) ?: UnknownTask

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
                    composable(
                        route = AndroidNavObjects.Add.getRoute(),
                        arguments = AndroidNavObjects.Add.arguments
                        )
                    { backStackEntry ->
                        val selectedItemType = backStackEntry.arguments?.getString(AndroidNavObjects.Add.KEY)?:""
                        DisposableEffect(key1 = addViewModel) {
                            addViewModel.onStart()
                            onDispose {
                                addViewModel.completionState.removeObservers(this@MainActivity)
                                addViewModel.onStop() }
                        }

                        ObserveCompletionStateToDoNavAction(addViewModel, androidNavActionManager)

                        AddScreen(
                            state_nullableProjectToPassInTask = null,
                            state_addScreenDeadlineSelectedDate = addViewModel.mNewTaskSelectedDeadLine.value,
                            state_addScreenAllCategories = addViewModel.mAllCategories.value,
                            state_isOkButtonEnabled = addViewModel.mIsOkButtonEnable.value,
                            state_addScreenIsDatePickerClickableIconVisible = selectedItemType == TASK,
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
                                    addAction = AddActions.AddScreenAddNewItemOnOkButtonClicked(selectedItemType)
                                )
                            }
                        )
                    }

                }

            }
        }
    }

    @Composable
    private fun ObserveCompletionStateToDoNavAction(
        addViewModel: AddViewModel,
        androidNavActionManager: AndroidNavActionManager
    ) {
        addViewModel.completionState.observe(this@MainActivity) {
            when (it) {
                BaseViewModel.CompletionState.ADD_TASK_ON_COMPLETE -> addViewModel.dispatchEvent(
                    AddActions.NavigateToHomeOnAddTaskComplete(
                        androidNavActionManager = androidNavActionManager
                    )
                )
                BaseViewModel.CompletionState.ADD_PROJECT_ON_COMPLETE -> addViewModel.dispatchEvent(
                    AddActions.NavigateToProjectOnAddProjectComplete(
                        androidNavActionManager = androidNavActionManager
                    )
                )
                else -> {
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