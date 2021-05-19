package com.picone.newArchitectureViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.task.DeleteTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel  @Inject constructor(
    private val mGetAllTasksInteractor: GetAllTasksInteractor,
    private val mDeleteTaskInteractor: DeleteTaskInteractor,
    private val mGetAllProjectInteractor: GetAllProjectInteractor

):ViewModel() {

    val mAllTasksMutableLD: MutableLiveData<MutableList<Task>> = MutableLiveData()
    val mAllProjectMutableLD : MutableLiveData<MutableList<Project>> = MutableLiveData()
    val mPopUpStateMutableLD : MutableLiveData<Boolean> = MutableLiveData(false)
    val mBottomNavSelectedItem : MutableLiveData<String> = MutableLiveData("home")

    fun getAllTasks() {
        viewModelScope.launch {
            mGetAllTasksInteractor.allTasksFlow
                .collect {
                    mAllTasksMutableLD.value = it.toMutableList()
                }
        }
    }

    fun deleteTask(task:Task){
        viewModelScope.launch {
            mDeleteTaskInteractor.deleteTask(task)
        }
    }

    fun getAllProjects() {
        viewModelScope.launch {
            mGetAllProjectInteractor.allProjectsFlow
                .collect {
                    mAllProjectMutableLD.value = it.toMutableList()
                }
        }
    }

    fun topAppBarAddItemButtonOpenPopUp(){
        mPopUpStateMutableLD.value = true
    }

    fun topAppBarAddItemButtonClosePopUp(){
        mPopUpStateMutableLD.value = false
    }

    fun updateBottomNavSelectedItem(selectedItem: String){
        mBottomNavSelectedItem.value = selectedItem
    }
}