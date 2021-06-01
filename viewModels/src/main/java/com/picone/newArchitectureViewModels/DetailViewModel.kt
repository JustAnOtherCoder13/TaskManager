package com.picone.newArchitectureViewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import com.picone.core.domain.interactor.underStain.AddNewUnderStainInteractor
import com.picone.core.domain.interactor.underStain.GetAllUnderStainForTaskIdInteractor
import com.picone.newArchitectureViewModels.androidUiManager.DetailAction
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.DetailActions
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mGetAllUnderStainForTaskIdInteractor: GetAllUnderStainForTaskIdInteractor,
    private val mAddNewUnderStainInteractor: AddNewUnderStainInteractor
) : BaseViewModel() {

    var mAllUnderStainsForTaskState: MutableState<List<UnderStain>> = mutableStateOf(emptyList())
    var mIsAddUnderStainComponentVisible: MutableState<Boolean> = mutableStateOf(false)
    var mNewUnderStainSelectedDeadLine: MutableState<String> = mutableStateOf("")
    private var mNewUnderStainSelectedTaskId: MutableState<Int> = mutableStateOf(0)
    private var mNewUnderStainName: MutableState<String> = mutableStateOf("")
    private var mNewUnderStainDescription: MutableState<String> = mutableStateOf("")
    var mNewUnderStainId: MutableState<Int> = mutableStateOf(0)

    fun onStart(selectedTask: Task) {
        dispatchEvent(DetailActions.OnDetailCreated(selectedTask))
    }
    fun onStop() = resetStates()

    fun dispatchEvent(action: DetailAction) {
        when (action) {
            is DetailActions.OnDetailCreated ->
                getAllUnderStainsForTask(action)

            is DetailActions.OnAddUnderStainButtonClick ->
                updateIsAddComponentVisible(true)

            is DetailActions.AddUnderStainButtonOnCancelButtonClicked -> {
                updateIsAddComponentVisible(false)
                mNewUnderStainSelectedDeadLine.value = ""
            }

            is DetailActions.OnDatePickerIconClickedOnDateSelected ->
                updateSelectedDeadline(action)

            is DetailActions.NameEditTextOnTextChange ->
                updateNewUnderStainName(action)

            is DetailActions.DescriptionEditTextOnTextChange ->
                updateNewUnderStainDescription(action)

            is DetailActions.AddUnderStainButtonOnOkButtonClicked -> {
                addNewUnderStain()
            }
        }
    }

    private fun addNewUnderStain() {
        viewModelScope.launch {
            try {
                mAddNewUnderStainInteractor.addNewUnderStain(
                    underStain = UnderStain(
                        taskId = mNewUnderStainSelectedTaskId.value,
                        name = mNewUnderStainName.value,
                        description = mNewUnderStainDescription.value,
                        start = null,
                        deadLine = getSelectedDeadLineOrNull(),
                        close = null
                    )
                )
                mIsAddUnderStainComponentVisible.value = false
                mNewUnderStainSelectedDeadLine.value = ""
            } catch (e: Exception) {
                Log.i("TAG", "addNewUnderStain: $e")
            }

        }
    }

    private fun updateNewUnderStainName(action: DetailActions.NameEditTextOnTextChange) {
        mNewUnderStainName.value = action.underStainName
    }

    private fun updateNewUnderStainDescription(action: DetailActions.DescriptionEditTextOnTextChange) {
        mNewUnderStainDescription.value = action.underStainDescription
    }

    private fun getSelectedDeadLineOrNull() =
        if (mNewUnderStainSelectedDeadLine.value.trim().isNotEmpty())
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.FRANCE
            ).parse(mNewUnderStainSelectedDeadLine.value)
        else null

    private fun getAllUnderStainsForTask(action: DetailActions.OnDetailCreated) {
        mNewUnderStainSelectedTaskId.value = action.task.id
        viewModelScope.launch {
            mGetAllUnderStainForTaskIdInteractor.getAllUnderStainForTaskId(taskId = action.task.id)
                .collect {
                    mAllUnderStainsForTaskState.value = it.toMutableList()
                }
        }
    }

    private fun updateSelectedDeadline(action: DetailActions.OnDatePickerIconClickedOnDateSelected) {
        mNewUnderStainSelectedDeadLine.value = action.selectedDate
    }

    private fun updateIsAddComponentVisible(isVisible: Boolean) {
        mIsAddUnderStainComponentVisible.value = isVisible
    }

    private fun resetStates() {
        mAllUnderStainsForTaskState.value = emptyList()
        mIsAddUnderStainComponentVisible.value = false
        mNewUnderStainSelectedDeadLine.value = ""
        mNewUnderStainSelectedTaskId.value = 0
        mNewUnderStainName.value = ""
        mNewUnderStainDescription.value = ""
        mNewUnderStainId.value = 0
    }
}