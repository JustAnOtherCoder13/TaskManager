package com.picone.appcompose.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.lifecycle.Observer
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.picone.appcompose.R
import com.picone.appcompose.ui.main.screen.EnterAnimation
import com.picone.appcompose.ui.main.screen.HorizontalAnimationLeftToRight
import com.picone.appcompose.ui.main.screen.HorizontalAnimationRightToLeft
import com.picone.appcompose.ui.main.screen.add.AddScreen
import com.picone.appcompose.ui.main.screen.detail.DetailScreen
import com.picone.appcompose.ui.main.screen.home.homeProject.HomeProjectScreen
import com.picone.appcompose.ui.main.screen.home.homeTask.HomeTaskScreen
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.util.Constants.KEY_EDIT_PROJECT
import com.picone.core.util.Constants.KEY_EDIT_TASK
import com.picone.core.util.Constants.KEY_ITEM
import com.picone.core.util.Constants.KEY_TASK
import com.picone.core.util.Constants.TASK
import com.picone.core.util.Constants.UnknownProject
import com.picone.core.util.Constants.UnknownTask
import com.picone.newArchitectureViewModels.*
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.AddActions
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.DetailActions
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.HomeActions
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavActionManager
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavDirections
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import java.text.SimpleDateFormat
import java.util.*

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @ExperimentalAnimationApi
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

                val completionStateObserver = Observer<BaseViewModel.CompletionState> {
                    when (it) {
                        BaseViewModel.CompletionState.ADD_TASK_ON_COMPLETE -> addViewModel.dispatchEvent(
                            AddActions.NavigateToDetailOnAddTaskComplete(
                                androidNavActionManager = androidNavActionManager
                            )
                        )
                        BaseViewModel.CompletionState.ADD_PROJECT_ON_COMPLETE -> addViewModel.dispatchEvent(
                            AddActions.NavigateToProjectOnProjectComplete(
                                androidNavActionManager = androidNavActionManager
                            )
                        )
                        BaseViewModel.CompletionState.UPDATE_TASK_ON_COMPLETE -> addViewModel.dispatchEvent(
                            AddActions.NavigateToHomeOnUpdateTaskComplete(
                                androidNavActionManager = androidNavActionManager
                            )
                        )
                        BaseViewModel.CompletionState.ADD_CATEGORY_ON_COMPLETE ->
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.category_added_message),
                                Toast.LENGTH_SHORT
                            ).show()

                        BaseViewModel.CompletionState.UPDATE_PROJECT_ON_COMPLETE -> {
                            addViewModel.dispatchEvent(
                                AddActions.NavigateToProjectOnProjectComplete(
                                    androidNavActionManager = androidNavActionManager
                                )
                            )
                        }

                        else -> {
                        }
                    }
                }

                homeViewModel.completionState.observe({ lifecycle }, completionStateObserver)
                addViewModel.completionState.observe({ lifecycle }, completionStateObserver)

                NavHost(
                    navController = navController,
                    startDestination = AndroidNavDirections.Home.destination
                ) {
                    //------------------------------------------------------------------------HOME
                    composable(AndroidNavDirections.Home.getRoute()) {

                        DisposableEffect(key1 = homeViewModel) {
                            homeViewModel.onStart(AndroidNavDirections.Home.destination)
                            onDispose {
                                homeViewModel.completionState.removeObserver(completionStateObserver)
                                homeViewModel.resetStates()
                            }
                        }
                        HorizontalAnimationLeftToRight {
                            HomeTaskScreen(
                                homeViewModel = homeViewModel,                          // pass view model and navManager
                                androidNavActionManager = androidNavActionManager,      //cause homeScreen share common state and event
                                state_allTasks = homeViewModel.mAllTasksState.value,    // for home and project
                                state_currentRoute = navBackStackEntry?.destination?.route,
                                state_allCategories = homeViewModel.mAllCategoriesState.value,
                                event_taskRecyclerViewOnTaskSelected = { selectedTask ->
                                    homeViewModel.dispatchEvent(
                                        HomeActions.TaskRecyclerViewOnTaskSelected(
                                            androidNavActionManager = androidNavActionManager,
                                            selectedTask = selectedTask
                                        )
                                    )
                                },
                                event_taskRecyclerViewOnMenuItemSelected = { menuItem, task ->
                                    homeViewModel.dispatchEvent(
                                        HomeActions.TaskRecyclerViewOnMenuItemSelected(
                                            menuItem,
                                            task,
                                            androidNavActionManager
                                        )
                                    )
                                },
                                event_onFilterItemSelected = {
                                    homeViewModel.dispatchEvent(
                                        HomeActions.OnFilterItemSelected(it)
                                    )
                                }
                            )
                        }
                    }

                    //------------------------------------------------------------------------PROJECT

                    composable(AndroidNavDirections.Project.getRoute()) {

                        DisposableEffect(key1 = homeViewModel) {
                            homeViewModel.onStart(AndroidNavDirections.Project.destination)
                            onDispose { homeViewModel.resetStates() }
                        }
                        HorizontalAnimationRightToLeft {
                            HomeProjectScreen(
                                homeViewModel = homeViewModel,
                                androidNavActionManager = androidNavActionManager,
                                state_allProjects = homeViewModel.mAllProjectState.value,
                                state_currentRoute = navBackStackEntry?.destination?.route,
                                state_allCategories = homeViewModel.mAllCategoriesState.value,
                                event_projectRecyclerViewOnMenuItemSelected = { selectedItem, project ->
                                    homeViewModel.dispatchEvent(
                                        HomeActions.ProjectRecyclerViewOnMenuItemSelected(
                                            androidNavActionManager = androidNavActionManager,
                                            selectedItem = selectedItem,
                                            project = project
                                        )
                                    )
                                }
                            )
                        }
                    }

                    //------------------------------------------------------------------------DETAIL

                    composable(
                        route = AndroidNavDirections.Detail.getRoute(),
                        arguments = AndroidNavDirections.Detail.arguments
                    ) { backStackEntry ->
                        val selectedTask =
                            getTaskOrNull(backStackEntry = backStackEntry) ?: UnknownTask

                        DisposableEffect(key1 = detailViewModel) {
                            detailViewModel.onStart(selectedTask)
                            onDispose { detailViewModel.resetStates() }
                        }

                        EnterAnimation {
                            DetailScreen(
                                state_Task = getTaskOrNull(backStackEntry) ?: UnknownTask,
                                state_allUnderStainsForTask = detailViewModel.mAllUnderStainsForTaskState.value,
                                state_isAddUnderStainComponentVisible = detailViewModel.mIsAddUnderStainComponentVisibleState.value,
                                state_datePickerIconDateText = detailViewModel.mNewUnderStainSelectedDeadLineState.value,
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
                                },
                                event_onUnderStainMenuItemSelected = { selectedItem, underStain ->
                                    detailViewModel.dispatchEvent(
                                        DetailActions.OnUnderStainMenuItemSelected(
                                            selectedItem = selectedItem,
                                            underStain = underStain,
                                            selectedTask = selectedTask
                                        )
                                    )
                                    //todo pop up to confirm delete
                                },
                                state_underStainName = detailViewModel.mEditedUnderStainNameState.value,
                                state_underStainDescription = detailViewModel.mEditedUnderStainDescriptionState.value
                            )
                        }
                    }

                    //------------------------------------------------------------------------ADD

                    composable(
                        route = "${AndroidNavDirections.Add.destination}/{${KEY_ITEM}}/{${KEY_EDIT_TASK}}/{${KEY_EDIT_PROJECT}}",
                        arguments = AndroidNavDirections.Add.arguments,
                    )
                    { backStackEntry ->

                        val selectedItemType =
                            backStackEntry.arguments?.getString(AndroidNavDirections.Add.KEY) ?: ""

                        var selectedTask =
                            backStackEntry.arguments?.getString(KEY_EDIT_TASK)?.let { json ->
                                Gson().fromJson(json, Task::class.java)
                            }

                        var selectedProject =
                            backStackEntry.arguments?.getString(KEY_EDIT_PROJECT)?.let { json ->
                                Gson().fromJson(json, Project::class.java)
                            }

                        if (selectedTask?.id == UnknownTask.id) selectedTask = null
                        if (selectedProject?.id == UnknownProject.id) selectedProject = null

                        DisposableEffect(key1 = addViewModel) {
                            addViewModel.onStart(
                                selectedTask = selectedTask,
                                selectedProject = selectedProject
                            )
                            onDispose {
                                addViewModel.completionState.removeObservers(this@MainActivity)
                                addViewModel.resetStates()
                            }
                        }

                        EnterAnimation {
                            AddScreen(
                                state_name = addViewModel.mNewItemName.value,
                                state_description = addViewModel.mNewItemDescription.value,
                                state_category = addViewModel.mNewItemCategory.value,
                                state_importance = addViewModel.mNewTaskImportance.value,
                                state_addScreenDeadlineSelectedDate = addViewModel.mNewTaskSelectedDeadLine.value,
                                state_addScreenAllCategories = addViewModel.mAllCategories.value,
                                state_isOkButtonEnabled = addViewModel.mIsOkButtonEnable.value,
                                state_addScreenIsDatePickerClickableIconVisible = selectedItemType == TASK || selectedTask != null,
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
                                            selectedItemType = selectedItemType,
                                            editedTask = selectedTask,
                                            editedProject = selectedProject
                                        )
                                    )
                                }
                            )
                        }
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