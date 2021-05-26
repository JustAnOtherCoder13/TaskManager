package com.picone.newArchitectureViewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picone.core.compose.AddAction
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Task
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import com.picone.core.domain.interactor.task.AddNewTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import com.picone.core.domain.navAction.NavObjects
import com.picone.core.util.Constants.CATEGORY
import com.picone.core.util.Constants.FIRST_ELEMENT
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
    private val mGetAllTasksInteractor: GetAllTasksInteractor
) : ViewModel() {

    var mNewTaskSelectedDeadLine: MutableState<String> = mutableStateOf("")
    var mAllCategories: MutableState<List<Category>> = mutableStateOf(mutableListOf())
    private var mNewTaskImportance: MutableState<String> = mutableStateOf("")
    private var mNewTaskCategory: MutableState<String> = mutableStateOf("")
    private var mNewTaskName: MutableState<String> = mutableStateOf("")
    private var mNewTaskDescription: MutableState<String> = mutableStateOf("")
    var mIsOkButtonEnable: MutableState<Boolean> = mutableStateOf(false)
    var mNewTaskId: MutableState<Int> = mutableStateOf(0)

    fun onStart() { Log.i("TAG", "onStart: ") }

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
                mNewTaskCategory.value = addAction.category
                mIsOkButtonEnable.value = isEnable()
            }

            is AddActions.AddScreenOnNameChange -> {
                mNewTaskName.value = addAction.name
                mIsOkButtonEnable.value = isEnable()
            }

            is AddActions.AddScreenOnDescriptionChange -> {
                mNewTaskDescription.value = addAction.description
                mIsOkButtonEnable.value = isEnable()
            }

            is AddActions.AddScreenAddNewItemOnOkButtonClicked -> {
                updateNewTaskId()
                addNewTask()
            }

            is AddActions.NavigateToHomeOnAddTaskComplete ->
                addAction.navActionManager.navigate(NavObjects.Home)

        }
    }

    private fun addNewTask() {
        viewModelScope.launch {
            try {
                mAddNewTaskInteractor.addNewTask(
                    Task(
                        id = mNewTaskId.value,
                        categoryId = mAllCategories.value.filter {
                            it.name == mNewTaskCategory.value
                        }[FIRST_ELEMENT].id,
                        close = null,
                        creation = Calendar.getInstance().time,
                        importance = 0,
                        deadLine = getSelectedDeadLineOrNull(),
                        description = mNewTaskDescription.value,
                        name = mNewTaskName.value,
                        start = null
                    )
                )
                completionState.value = CompletionState.ON_COMPLETE
            } catch (e: Exception) {
                Log.e(this::class.java.simpleName, "dispatchEvent: ", e)
                completionState.value = CompletionState.ON_ERROR

            }
        }
    }

    var completionState: MutableState<CompletionState> =
        mutableStateOf(CompletionState.ON_START)

    enum class CompletionState {
        ON_START,
        ON_COMPLETE,
        ON_ERROR
    }

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
        mNewTaskDescription.value.trim().isNotEmpty()
                && mNewTaskName.value.trim().isNotEmpty()
                && mNewTaskCategory.value != CATEGORY

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
        mNewTaskCategory.value = ""
        mNewTaskName.value = ""
        mNewTaskDescription.value = ""
        mIsOkButtonEnable.value = false
        mNewTaskId.value = 0
    }
}