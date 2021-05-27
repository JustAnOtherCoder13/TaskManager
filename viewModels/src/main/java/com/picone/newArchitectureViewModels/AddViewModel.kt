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
import com.picone.core.util.Constants.PROJECT
import com.picone.core.util.Constants.TASK
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
    private var mNewTaskImportance: MutableState<String> = mutableStateOf("")
    private var mNewItemCategory: MutableState<String> = mutableStateOf("")
    private var mNewItemName: MutableState<String> = mutableStateOf("")
    private var mNewItemDescription: MutableState<String> = mutableStateOf("")
    var mIsOkButtonEnable: MutableState<Boolean> = mutableStateOf(false)
    var mNewTaskId: MutableState<Int> = mutableStateOf(0)
    var mNewProjectId: MutableState<Int> = mutableStateOf(0)
    var completionState: MutableLiveData<CompletionState> = MutableLiveData(CompletionState.ON_START)

    fun onStart() {
        dispatchEvent(AddActions.OnAddCreated)
    }

    fun onStop() = resetStates()

    fun dispatchEvent(addAction: AddAction) {
        when (addAction) {
            is AddActions.OnDatePickerIconClickedOnDateSelected ->
                updateSelectedDeadline(addAction)

            is AddActions.OnAddCreated ->
                getAllCategories()

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
                        importance = 0,
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

    private fun getAllCategories() {
        viewModelScope.launch {
            mGetAllCategoriesInteractor.allCategoriesFlow.collect {
                mAllCategories.value = it
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