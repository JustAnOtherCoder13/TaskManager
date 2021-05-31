package com.picone.newArchitectureViewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import com.picone.core.domain.interactor.project.AddNewProjectInteractor
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.task.AddNewTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import com.picone.core.domain.interactor.task.UpdateTaskInteractor
import com.picone.core.util.Constants.CATEGORY
import com.picone.core.util.Constants.EDIT
import com.picone.core.util.Constants.FIRST_ELEMENT
import com.picone.core.util.Constants.IMPORTANCE_LIST
import com.picone.core.util.Constants.PASS_TO_TASK
import com.picone.core.util.Constants.PROJECT
import com.picone.core.util.Constants.TASK
import com.picone.core.util.Constants.UnknownTask
import com.picone.newArchitectureViewModels.androidUiManager.AddAction
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.AddActions
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class AddViewModel @Inject constructor(
    private val mGetAllCategoriesInteractor: GetAllCategoriesInteractor,
    private val mAddNewTaskInteractor: AddNewTaskInteractor,
    private val mGetAllTasksInteractor: GetAllTasksInteractor,
    private val mAddNewProjectInteractor: AddNewProjectInteractor,
    private val mUpdateTaskInteractor: UpdateTaskInteractor
) : BaseViewModel() {

    var mNewTaskSelectedDeadLine: MutableState<String> = mutableStateOf("")
    var mAllCategories: MutableState<List<Category>> = mutableStateOf(mutableListOf())
    var mNewTaskImportance: MutableState<String?> = mutableStateOf(null)
    var mNewItemCategory: MutableState<String?> = mutableStateOf(null)
    var mNewItemName: MutableState<String> = mutableStateOf("")
    var mNewItemDescription: MutableState<String> = mutableStateOf("")
    var mIsOkButtonEnable: MutableState<Boolean> = mutableStateOf(false)
    var completionState: MutableLiveData<CompletionState> =
        MutableLiveData(CompletionState.ON_START)

    fun onStart(selectedTask: Task?, selectedProject: Project?) {
        currentDestinationMutableLD.value = AndroidNavObjects.Add.destination
        getAllCategories(selectedTask, selectedProject)
    }

    fun onStop() = resetStates()

    fun dispatchEvent(addAction: AddAction) {
        when (addAction) {
            is AddActions.OnDatePickerIconClickedOnDateSelected ->
                updateSelectedDeadline(addAction)

            is AddActions.OnAddCreated -> {
                if (addAction.selectedTask != null) updateTaskUiValue(addAction.selectedTask)
                if (addAction.selectedProject != null) updateProjectUiValue(addAction.selectedProject)
            }

            is AddActions.OnAddScreenImportanceSelected ->
                mNewTaskImportance.value = addAction.importance

            is AddActions.OnAddScreenCategorySelected -> {
                mNewItemCategory.value = addAction.category
                mIsOkButtonEnable.value = isEnable()
            }

            is AddActions.AddScreenOnNameChange -> {
                mNewItemName.value = addAction.name
                mIsOkButtonEnable.value = isEnable()
            }

            is AddActions.AddScreenOnDescriptionChange -> {
                mNewItemDescription.value = addAction.description
                mIsOkButtonEnable.value = isEnable()
            }

            is AddActions.AddScreenAddNewItemOnOkButtonClicked -> {
                if (addAction.editedTask != null && addAction.editedTask.name != UnknownTask.name)
                    updateEditedTask(addAction.editedTask)
                else {
                    when (addAction.selectedItemType) {
                        TASK -> {
                            Log.i("TAG", "dispatchEvent: add task ")
                            //updateNewTaskId()
                            addNewTask()
                        }
                        PROJECT -> {
                            //updateNewProjectId()
                            addNewProject()
                        }
                        EDIT -> Log.i("TAG", "dispatchEvent: edit")
                        PASS_TO_TASK -> Log.i("TAG", "dispatchEvent: pass to task")
                    }
                }
            }

            is AddActions.NavigateToDetailOnAddTaskComplete -> {
                viewModelScope.launch {
                    Log.e("TAG", "dispatchEvent: nav to detail", )
                    mGetAllTasksInteractor.allTasksFlow.collect {
                        val taskToJson =
                            Gson().toJson(it.filter { task -> task.name == getNewTask().name && task.description == getNewTask().description }[FIRST_ELEMENT])
                        addAction.androidNavActionManager.navigate(
                            AndroidNavObjects.Detail,
                            taskToJson
                        )
                    }
                }
            }

            is AddActions.NavigateToProjectOnAddProjectComplete ->
                addAction.androidNavActionManager.navigate(AndroidNavObjects.Project)

        }
    }

    private fun updateEditedTask(editedTask: Task) {
        viewModelScope.launch {
            completionState.value = CompletionState.ON_LOADING
            try {
                mUpdateTaskInteractor.updateTask(
                    Task(
                        categoryId = getCategoryId(),
                        close = editedTask.close,
                        creation = editedTask.creation,
                        importance = IMPORTANCE_LIST.indexOf(mNewTaskImportance.value),
                        deadLine = getSelectedDeadLineOrNull(),
                        description = mNewItemDescription.value,
                        name = mNewItemName.value,
                        start = editedTask.start
                    )
                )
                completionState.value = CompletionState.UPDATE_TASK_ON_COMPLETE
            } catch (e: Exception) {
                Log.e(this::class.java.simpleName, "dispatchEvent: ", e)
                completionState.value = CompletionState.ON_ERROR
            }

        }
    }

    private fun updateTaskUiValue(selectedTask: Task) {
        mNewTaskSelectedDeadLine.value =
            if (selectedTask.deadLine != null) {
                SimpleDateFormat("dd/MM/yyy", Locale.FRANCE).format(selectedTask.deadLine!!)
            } else ""
        mNewTaskImportance.value = getSelectedImportanceOrSetUnimportant(selectedTask)
        mNewItemCategory.value =
            mAllCategories.value.filter { selectedTask.categoryId == it.id }[FIRST_ELEMENT].name
        mNewItemName.value = selectedTask.name
        mNewItemDescription.value = selectedTask.description
    }

    private fun getSelectedImportanceOrSetUnimportant(selectedTask: Task) =
        if (selectedTask.importance >= 0) IMPORTANCE_LIST[selectedTask.importance] else IMPORTANCE_LIST[0]

    private fun addNewProject() {
        viewModelScope.launch {
            completionState.value = CompletionState.ON_LOADING
            try {
                mAddNewProjectInteractor.addNewProject(
                    Project(
                        categoryId =  getCategoryId(),
                        name = mNewItemName.value,
                        description = mNewItemDescription.value
                    )
                )
                completionState.value = CompletionState.ADD_PROJECT_ON_COMPLETE
            } catch (e: Exception) {
                Log.e(this::class.java.simpleName, "dispatchEvent: ", e)
                completionState.value = CompletionState.ON_ERROR
            }
        }
    }

    private fun addNewTask() {
        viewModelScope.launch {
            completionState.value = CompletionState.ON_LOADING
            try {
                mAddNewTaskInteractor.addNewTask(getNewTask())
                completionState.value = CompletionState.ADD_TASK_ON_COMPLETE
            } catch (e: Exception) {
                Log.e(this::class.java.simpleName, "dispatchEvent: ", e)
                completionState.value = CompletionState.ON_ERROR
            }
        }
    }

    private fun getNewTask() : Task {
        Log.i("TAG", "getNewTask: "+mAllCategories.value+" "+currentDestinationMutableLD.value)
        return  Task(
            categoryId = getCategoryId(),
            close = null,
            creation = Calendar.getInstance().time,
            importance = IMPORTANCE_LIST.indexOf(mNewTaskImportance.value),
            deadLine = getSelectedDeadLineOrNull(),
            description = mNewItemDescription.value,
            name = mNewItemName.value,
            start = null
        )
    }

    private fun getCategoryId()= if( mAllCategories.value.isNotEmpty()) mAllCategories.value.filter {
        it.name == mNewItemCategory.value
    }[FIRST_ELEMENT].id else 0


    private fun getAllCategories(selectedTask: Task?, selectedProject: Project?) {
        viewModelScope.launch {
            mGetAllCategoriesInteractor.allCategoriesFlow.collect {
                mAllCategories.value = it
                dispatchEvent(AddActions.OnAddCreated(selectedTask, selectedProject))
            }
        }
    }


    private fun updateSelectedDeadline(action: AddActions.OnDatePickerIconClickedOnDateSelected) {
        mNewTaskSelectedDeadLine.value = action.selectedDate
    }

    private fun isEnable(): Boolean =
        mNewItemDescription.value.trim().isNotEmpty()
                && mNewItemName.value.trim().isNotEmpty()
                && mNewItemCategory.value != CATEGORY
                && completionState.value != CompletionState.ON_LOADING

    private fun getSelectedDeadLineOrNull() =
        if (mNewTaskSelectedDeadLine.value.trim().isNotEmpty())
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.FRANCE
            ).parse(mNewTaskSelectedDeadLine.value)
        else null

    private fun resetStates() {
        completionState.value = CompletionState.ON_START
        mNewTaskSelectedDeadLine.value = ""
        mAllCategories.value = mutableListOf()
        mNewTaskImportance.value = ""
        mNewItemCategory.value = ""
        mNewItemName.value = ""
        mNewItemDescription.value = ""
        mIsOkButtonEnable.value = false
        
    }

    private fun updateProjectUiValue(selectedProject: Project) {
        mNewItemCategory.value =
            mAllCategories.value.filter { selectedProject.categoryId == it.id }[FIRST_ELEMENT].name
        mNewItemName.value = selectedProject.name
        mNewItemDescription.value = selectedProject.description
    }

}