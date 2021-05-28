package com.picone.newArchitectureViewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.picone.newArchitectureViewModels.androidUiManager.HomeAction
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.task.DeleteTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import com.picone.core.util.Constants.CATEGORY
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.HomeActions
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mGetAllTasksInteractor: GetAllTasksInteractor,
    private val mDeleteTaskInteractor: DeleteTaskInteractor,
    private val mGetAllProjectInteractor: GetAllProjectInteractor

) : ViewModel() {

    val mAllTasksState: MutableState<MutableList<Task>> = mutableStateOf(mutableListOf())
    val mAllProjectState: MutableState<MutableList<Project>> = mutableStateOf(mutableListOf())

    fun onStart(destination: String) {
        when (destination) {
            AndroidNavObjects.Home.destination -> dispatchEvent(HomeActions.OnHomeCreated)
            AndroidNavObjects.Project.destination -> dispatchEvent(HomeActions.OnProjectCreated)
        }
    }

    fun onStop() = resetStates()

    fun dispatchEvent(homeAction: HomeAction) {
        when (homeAction) {
            is HomeActions.OnHomeCreated ->
                getAllTasks()

            is HomeActions.OnProjectCreated ->
                getAllProjects()

            is HomeActions.BottomNavBarOnNavItemSelected ->
                homeAction.androidNavActionManager.onBottomNavItemSelected(homeAction.selectedNavItem)

            is HomeActions.TopBarOnMenuItemSelected ->
                if (homeAction.selectedItem != CATEGORY) {
                    homeAction.androidNavActionManager.navigate(
                        AndroidNavObjects.Add,
                        homeAction.selectedItem,
                        Gson().toJson(homeAction.selectedTask)
                    )
                }
                else Log.i("TAG", "dispatchEvent: category click")

            is HomeActions.TaskRecyclerViewOnTaskSelected ->
                homeAction.androidNavActionManager.navigate(
                    AndroidNavObjects.Detail,
                    Gson().toJson(homeAction.selectedTask)
                )

            is HomeActions.OnDeleteTaskSelected ->{
                deleteTask(homeAction.task)
            }

            is HomeActions.OnEditTaskSelected ->{
                homeAction.androidNavActionManager.navigate(
                    AndroidNavObjects.Add,
                    homeAction.selectedItem,
                    Gson().toJson(homeAction.task)
                )
            }
        }
    }

    private fun getAllProjects() {
        viewModelScope.launch {
            mGetAllProjectInteractor.allProjectsFlow
                .collect { mAllProjectState.value = it.toMutableList() }
        }
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            mGetAllTasksInteractor.allTasksFlow
                .collect {
                    mAllTasksState.value = it.toMutableList()

                }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            mDeleteTaskInteractor.deleteTask(task)
        }
    }

    private fun resetStates() {
        mAllTasksState.value = mutableListOf()
        mAllProjectState.value = mutableListOf()
    }

}