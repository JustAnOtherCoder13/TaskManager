package com.picone.newArchitectureViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picone.core.compose.HomeAction
import com.picone.core.compose.UiAction
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.task.DeleteTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import com.picone.core.domain.navAction.NavActionManager
import com.picone.core.domain.navAction.NavObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val mGetAllTasksInteractor: GetAllTasksInteractor,
    private val mDeleteTaskInteractor: DeleteTaskInteractor,
    private val mGetAllProjectInteractor: GetAllProjectInteractor

):ViewModel() {

    fun dispatchEvent(homeAction : HomeAction){
        when(homeAction){
            is HomeActions.OnHomeCreated ->
                getAllTasks()

            is HomeActions.OnProjectCreated ->
                getAllProjects()

            is HomeActions.BottomNavBarOnNavItemSelected ->
                homeAction.navActionManager.onBottomNavItemSelected(homeAction.selectedNavItem)

            is HomeActions.TopBarOnMenuItemSelected ->
                homeAction.navActionManager.navigate(NavObjects.Add)

            is HomeActions.TaskRecyclerViewOnTaskSelected ->
                homeAction.navActionManager.navigate(NavObjects.Detail,homeAction.selectedTask)
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
                .collect { mAllTasksState.value = it.toMutableList() }
        }
    }

    val mAllTasksState: MutableState<MutableList<Task>> = mutableStateOf(mutableListOf())
    val mAllProjectState : MutableState<MutableList<Project>> = mutableStateOf(mutableListOf())


    fun deleteTask(task:Task){
        viewModelScope.launch {
            mDeleteTaskInteractor.deleteTask(task)
        }
    }

}