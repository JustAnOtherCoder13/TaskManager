package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.*
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Task
import com.picone.core.domain.interactor.task.AddNewTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksForCategoryIdInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import com.picone.core.domain.interactor.task.GetTaskForIdInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    getAllTasksInteractor: GetAllTasksInteractor,
    private val getTaskForIdInteractor: GetTaskForIdInteractor,
    private val getAllTasksForCategoryIdInteractor: GetAllTasksForCategoryIdInteractor,
    private val addNewTaskInteractor: AddNewTaskInteractor
) : BaseViewModel() {


    val allTasks: LiveData<List<Task>> =
        getAllTasksInteractor.getAllTasks().asLiveData()

    fun taskForId(id: Int): LiveData<Task> =
        getTaskForIdInteractor.getTaskForId(id).asLiveData()

    fun tasksForCategory(category: Category): LiveData<List<Task>> =
        getAllTasksForCategoryIdInteractor.getAllTasksForCategoryId(category.id).asLiveData()

    fun addNewTask(task: Task) =
        viewModelScope.launch {
            addNewTaskInteractor.addNewTask(task)
        }
}