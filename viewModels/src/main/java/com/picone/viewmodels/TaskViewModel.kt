package com.picone.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Task
import com.picone.core.domain.interactor.task.AddNewTaskInteractor
import com.picone.core.domain.interactor.task.UpdateTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import com.picone.core.domain.interactor.task.DeleteTaskInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val mGetAllTasksInteractor: GetAllTasksInteractor,
    private val mDeleteTaskInteractor: DeleteTaskInteractor,
    private val mUpdateTaskInteractor: UpdateTaskInteractor,
    private val mAddNewTaskInteractor: AddNewTaskInteractor
) : BaseViewModel() {

    val mAllTasksMutableLD: MutableLiveData<MutableList<Task>> = MutableLiveData()
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

   /* fun getTaskForId(id: Int) {
        viewModelScope.launch {
            mTaskForIdMutableLD.value = mDeleteTaskInteractor.deleteTask(id)
        }
    }


    fun getTasksForCategory(category: Category) {
        viewModelScope.launch {
            mUpdateTaskInteractor.getAllTasksForCategoryId(category.id)
                .collect {
                    mTasksForCategoryMutableLD.value = it.toMutableList()
                }
        }
    }*/

    fun addNewTask(task: Task) =
        try {
            viewModelScope.launch {
                mAddNewTaskInteractor.addNewTask(task)
            }
            completionStateMutableLD.value = Companion.CompletionState.TASK_ON_COMPLETE
        }catch (e:Exception){e.printStackTrace()}
}