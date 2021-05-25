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
import java.lang.Exception
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
    var mNewTaskImportance: MutableState<String> = mutableStateOf("")
    var mNewTaskCategory: MutableState<String> = mutableStateOf("")
    var mNewTaskName: MutableState<String> = mutableStateOf("")
    var mNewTaskDescription: MutableState<String> = mutableStateOf("")
    var mIsOkButtonEnable: MutableState<Boolean> = mutableStateOf(false)
    var mNewTaskId : MutableState<Int> = mutableStateOf(0)


    fun dispatchEvent(addAction: AddAction) {
        when (addAction) {
            is AddActions.OnDatePickerIconClickedOnDateSelected ->
                updateSelectedDeadline(addAction)
            is AddActions.OnAddCreated ->
                getAllCategories()
            is AddActions.OnAddScreenImportanceSelected -> {
                mNewTaskImportance.value = addAction.importance
            }
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
                viewModelScope.launch {
                    mGetAllTasksInteractor.allTasksFlow.collect {
                        mNewTaskId.value = it.size + 1
                    }
                }
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
                        addAction.navActionManager.navigate(NavObjects.Home)
                    }catch (e:Exception){
                        Log.e(this::class.java.simpleName, "dispatchEvent: ",e )
                    }

                }
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


}