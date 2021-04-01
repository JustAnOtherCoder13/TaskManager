package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.CompleteTask
import com.picone.core.domain.entity.Task
import com.picone.core.domain.interactor.task.AddNewTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksForCategoryIdInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import com.picone.core.domain.interactor.task.GetTaskForIdInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val mGetAllTasksInteractor: GetAllTasksInteractor,
    private val mGetTaskForIdInteractor: GetTaskForIdInteractor,
    private val mGetAllTasksForCategoryIdInteractor: GetAllTasksForCategoryIdInteractor,
    private val mAddNewTaskInteractor: AddNewTaskInteractor
) : BaseViewModel() {

    val mAllTasksMutableLD: MutableLiveData<MutableList<CompleteTask>> = MutableLiveData()
    val mTaskForIdMutableLD: MutableLiveData<Task> = MutableLiveData()
    val mTasksForCategoryMutableLD: MutableLiveData<MutableList<Task>> = MutableLiveData()

    fun getAllTasks() {
        viewModelScope.launch {
            mGetAllTasksInteractor.allTasksFlow
                .collect {
                    mAllTasksMutableLD.value = it.toMutableList()
                }
        }
    }

    fun getTaskForId(id: Int) {
        viewModelScope.launch {
            mTaskForIdMutableLD.value = mGetTaskForIdInteractor.getTaskForId(id)
        }
    }


    fun getTasksForCategory(category: Category) {
        viewModelScope.launch {
            mGetAllTasksForCategoryIdInteractor.getAllTasksForCategoryId(category.id)
                .collect {
                    mTasksForCategoryMutableLD.value = it.toMutableList()
                }
        }
    }

    fun addNewTask(task: Task) =
        viewModelScope.launch {
            mAddNewTaskInteractor.addNewTask(task)
        }
}