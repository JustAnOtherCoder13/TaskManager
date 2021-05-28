package com.picone.newArchitectureViewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.picone.newArchitectureViewModels.androidUiManager.AddAction
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import com.picone.core.domain.interactor.project.AddNewProjectInteractor
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.task.AddNewTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavObjects
import com.picone.core.util.Constants.CATEGORY
import com.picone.core.util.Constants.FIRST_ELEMENT
import com.picone.core.util.Constants.IMPORTANCE_LIST
import com.picone.core.util.Constants.PROJECT
import com.picone.core.util.Constants.TASK
import com.picone.core.util.Constants.UnknownTask
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.AddActions
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
    private val mGetAllProjectInteractor: GetAllProjectInteractor,
    private val mAddNewProjectInteractor: AddNewProjectInteractor
) : BaseViewModel() {


    var mNewTaskSelectedDeadLine: MutableState<String> = mutableStateOf("")
    var mAllCategories: MutableState<List<Category>> = mutableStateOf(mutableListOf())
    var mNewTaskImportance: MutableState<String?> = mutableStateOf(null)
    var mNewItemCategory: MutableState<String?> = mutableStateOf(null)
    var mNewItemName: MutableState<String> = mutableStateOf("")
    var mNewItemDescription: MutableState<String> = mutableStateOf("")
    var mIsOkButtonEnable: MutableState<Boolean> = mutableStateOf(false)
    var mNewTaskId: MutableState<Int> = mutableStateOf(0)
    var mNewProjectId: MutableState<Int> = mutableStateOf(0)
    var completionState: MutableLiveData<CompletionState> =
        MutableLiveData(CompletionState.ON_START)

    fun onStart(selectedTask: Task?) {
        getAllCategories(selectedTask)
    }

    fun onStop() = resetStates()

    fun dispatchEvent(addAction: AddAction) {
        when (addAction) {
            is AddActions.OnDatePickerIconClickedOnDateSelected ->
                updateSelectedDeadline(addAction)

            is AddActions.OnAddCreated -> {
                if (addAction.selectedTask != null && addAction.selectedTask.id != UnknownTask.id) updateUiValue(addAction.selectedTask)
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
                when (addAction.selectedItemType) {
                    TASK -> {
                        updateNewTaskId()
                        addNewTask()
                    }
                    PROJECT -> {
                        updateNewProjectId()
                        addNewProject()
                    }
                }
            }

            is AddActions.NavigateToHomeOnAddTaskComplete ->
                addAction.androidNavActionManager.navigate(AndroidNavObjects.Home)

            is AddActions.NavigateToProjectOnAddProjectComplete ->
                addAction.androidNavActionManager.navigate(AndroidNavObjects.Project)

        }
    }

    private fun updateUiValue(selectedTask: Task) {
        Log.i("TAG", "updateUiValue: "+mAllCategories.value.filter { selectedTask.categoryId == it.id }[FIRST_ELEMENT].name)
        mNewTaskSelectedDeadLine.value =
            if (selectedTask.deadLine != null) {
                SimpleDateFormat("dd/MM/yyy", Locale.FRANCE).format(selectedTask.deadLine!!)
            } else ""
        mNewTaskImportance.value = IMPORTANCE_LIST[selectedTask.importance]
        mNewItemCategory.value = mAllCategories.value.filter { selectedTask.categoryId == it.id }[FIRST_ELEMENT].name ?: ""
        mNewItemName.value = selectedTask.name
        mNewItemDescription.value = selectedTask.description
        mNewTaskId.value = selectedTask.id
    }

    private fun addNewProject() {
        viewModelScope.launch {
            completionState.value = CompletionState.ON_LOADING
            try {
                mAddNewProjectInteractor.addNewProject(
                    Project(
                        id = mNewProjectId.value,
                        categoryId = getCategoryId(),
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

    private fun updateNewProjectId() {
        viewModelScope.launch {
            mGetAllProjectInteractor.allProjectsFlow.collect {
                mNewProjectId.value = it.size + 1
            }
        }
    }

    private fun addNewTask() {
        viewModelScope.launch {
            completionState.value = CompletionState.ON_LOADING
            try {
                mAddNewTaskInteractor.addNewTask(
                    Task(
                        id = mNewTaskId.value,
                        categoryId = getCategoryId(),
                        close = null,
                        creation = Calendar.getInstance().time,
                        importance = IMPORTANCE_LIST.indexOf(mNewTaskImportance.value),
                        deadLine = getSelectedDeadLineOrNull(),
                        description = mNewItemDescription.value,
                        name = mNewItemName.value,
                        start = null
                    )
                )
                completionState.value = CompletionState.ADD_TASK_ON_COMPLETE
            } catch (e: Exception) {
                Log.e(this::class.java.simpleName, "dispatchEvent: ", e)
                completionState.value = CompletionState.ON_ERROR
            }
        }
    }

    private fun getCategoryId() = mAllCategories.value.filter {
        it.name == mNewItemCategory.value
    }[FIRST_ELEMENT].id

    private fun updateNewTaskId() {
        viewModelScope.launch {
            mGetAllTasksInteractor.allTasksFlow.collect {
                mNewTaskId.value = it.size + 1
            }
        }
    }

    private fun getAllCategories(selectedTask: Task?) {
        viewModelScope.launch {
            mGetAllCategoriesInteractor.allCategoriesFlow.collect {
                mAllCategories.value = it
                dispatchEvent(AddActions.OnAddCreated(selectedTask))
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
        mNewTaskId.value = 0
        mNewProjectId.value = 0
    }
}