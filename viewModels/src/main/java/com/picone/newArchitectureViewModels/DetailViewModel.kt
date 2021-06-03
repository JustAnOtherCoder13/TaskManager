package com.picone.newArchitectureViewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import com.picone.core.domain.interactor.task.UpdateTaskInteractor
import com.picone.core.domain.interactor.underStain.AddNewUnderStainInteractor
import com.picone.core.domain.interactor.underStain.DeleteUnderStainInteractor
import com.picone.core.domain.interactor.underStain.GetAllUnderStainForTaskIdInteractor
import com.picone.core.domain.interactor.underStain.UpdateUnderStainInteractor
import com.picone.core.util.Constants.CLOSE
import com.picone.core.util.Constants.DELETE
import com.picone.core.util.Constants.EDIT
import com.picone.core.util.Constants.START
import com.picone.newArchitectureViewModels.androidUiManager.DetailAction
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.DetailActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mGetAllUnderStainForTaskIdInteractor: GetAllUnderStainForTaskIdInteractor,
    private val mAddNewUnderStainInteractor: AddNewUnderStainInteractor,
    private val mDeleteUnderStainInteractor: DeleteUnderStainInteractor,
    private val mUpdateUnderStainInteractor: UpdateUnderStainInteractor,
    private val mUpdateTaskInteractor: UpdateTaskInteractor
) : BaseViewModel() {

    var mAllUnderStainsForTaskState: MutableState<List<State<UnderStain>>> =
        mutableStateOf(emptyList())
    var mIsAddUnderStainComponentVisible: MutableState<Boolean> = mutableStateOf(false)
    var mEditedUnderStain: MutableState<UnderStain?> = mutableStateOf(null)
    var mNewUnderStainSelectedDeadLine: MutableState<String> = mutableStateOf("")
    private var mNewUnderStainSelectedTaskId: MutableState<Int> = mutableStateOf(0)
    var mNewUnderStainId: MutableState<Int> = mutableStateOf(0)
    var mEditedUnderStainName: MutableState<String> = mutableStateOf("")
    var mEditedUnderStainDescription: MutableState<String> = mutableStateOf("")

    private var collectAllUnderStainsForTask: Job? = null

    fun onStart(selectedTask: Task) {
        dispatchEvent(DetailActions.OnDetailCreated(selectedTask))
    }

    fun onStop() = resetStates()

    fun dispatchEvent(detailAction: DetailAction) {
        when (detailAction) {
            is DetailActions.OnDetailCreated ->
                getAllUnderStainsForTask(detailAction)

            is DetailActions.OnAddUnderStainButtonClick ->
                isAddUnderStainComponentVisible(true)

            is DetailActions.AddUnderStainButtonOnCancelButtonClicked -> {
                resetAddUnderStainComponent()
            }

            is DetailActions.OnDatePickerIconClickedOnDateSelected ->
                updateSelectedDeadline(detailAction)

            is DetailActions.NameEditTextOnTextChange ->
                updateNewUnderStainName(detailAction)

            is DetailActions.DescriptionEditTextOnTextChange ->
                updateNewUnderStainDescription(detailAction)

            is DetailActions.AddUnderStainButtonOnOkButtonClicked -> {
                if (mEditedUnderStain.value != null) {
                    viewModelScope.launch {
                        updateEditedUnderStain()
                        resetAddUnderStainComponent()
                    }
                } else addNewUnderStain()

                resetAddUnderStainComponent()
            }

            is DetailActions.OnUnderStainMenuItemSelected -> {
                when (detailAction.selectedItem) {
                    EDIT -> updateAddUnderStainComponentValue(detailAction)

                    DELETE -> deleteUnderStain(detailAction)

                    START -> {
                        val startDate = Calendar.getInstance().time
                        detailAction.underStain.start = startDate
                        viewModelScope.launch {
                            mUpdateUnderStainInteractor.updateUnderStain(detailAction.underStain)
                        }
                        if (detailAction.selectedTask.start == null) {
                            detailAction.selectedTask.start = startDate
                            viewModelScope.launch {
                                mUpdateTaskInteractor.updateTask(detailAction.selectedTask)
                            }
                        }
                    }

                    CLOSE -> {
                        detailAction.underStain.close = Calendar.getInstance().time
                        viewModelScope.launch {
                            mUpdateUnderStainInteractor.updateUnderStain(detailAction.underStain)
                        }
                        //todo if all under stains closed pop up "would you like to close this task?"
                    }
                }
            }
        }
    }

    private suspend fun updateEditedUnderStain() {
        mUpdateUnderStainInteractor.updateUnderStain(
            UnderStain(
                id = mEditedUnderStain.value?.id!!,
                taskId = mEditedUnderStain.value?.taskId!!,
                name = mEditedUnderStainName.value,
                description = mEditedUnderStainDescription.value,
                start = mEditedUnderStain.value?.start,
                deadLine = getSelectedDeadLineOrNull(),
                close = mEditedUnderStain.value?.close
            )
        )
    }

    private fun updateAddUnderStainComponentValue(detailAction: DetailActions.OnUnderStainMenuItemSelected) {
        mEditedUnderStain.value = detailAction.underStain
        isAddUnderStainComponentVisible(true)
        mNewUnderStainSelectedDeadLine.value =
            if (detailAction.underStain.deadLine != null) SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.FRANCE
            ).format(detailAction.underStain.deadLine!!) else ""
        mEditedUnderStainDescription.value = detailAction.underStain.description
        mEditedUnderStainName.value = detailAction.underStain.name
    }

    private fun resetAddUnderStainComponent() {
        isAddUnderStainComponentVisible(false)
        mEditedUnderStain.value = null
        mNewUnderStainSelectedDeadLine.value = ""
        mEditedUnderStainName.value = ""
        mEditedUnderStainDescription.value = ""
    }

    private fun deleteUnderStain(detailAction: DetailActions.OnUnderStainMenuItemSelected) {
        viewModelScope.launch {
            mDeleteUnderStainInteractor.deleteUnderStain(detailAction.underStain)
        }
    }

    private fun addNewUnderStain() {
        viewModelScope.launch {
            try {
                mAddNewUnderStainInteractor.addNewUnderStain(
                    underStain = UnderStain(
                        taskId = mNewUnderStainSelectedTaskId.value,
                        name = mEditedUnderStainName.value,
                        description = mEditedUnderStainDescription.value,
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
        mEditedUnderStainName.value = action.underStainName
    }

    private fun updateNewUnderStainDescription(action: DetailActions.DescriptionEditTextOnTextChange) {
        mEditedUnderStainDescription.value = action.underStainDescription
    }

    private fun getSelectedDeadLineOrNull() =
        if (mNewUnderStainSelectedDeadLine.value.trim().isNotEmpty())
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.FRANCE
            ).parse(mNewUnderStainSelectedDeadLine.value)
        else null

    private fun getAllUnderStainsForTask(action: DetailActions.OnDetailCreated) {
        collectAllUnderStainsForTask =
            viewModelScope.launch {
                mGetAllUnderStainForTaskIdInteractor.getAllUnderStainForTaskId(taskId = action.task.id)
                    .catch { exception -> Log.e("TAG", "getAllUnderStainsForTask: ", exception) }
                    .collect { mAllUnderStainsForTaskState.value = it }
            }
        mNewUnderStainSelectedTaskId.value = action.task.id
    }

    private fun updateSelectedDeadline(action: DetailActions.OnDatePickerIconClickedOnDateSelected) {
        mNewUnderStainSelectedDeadLine.value = action.selectedDate
    }

    private fun isAddUnderStainComponentVisible(isVisible: Boolean) {
        mIsAddUnderStainComponentVisible.value = isVisible
    }

    override fun resetStates() {
        mIsAddUnderStainComponentVisible.value = false
        mEditedUnderStain.value = null
        mNewUnderStainSelectedDeadLine.value = ""
        mNewUnderStainSelectedTaskId.value = 0
        mEditedUnderStainName.value = ""
        mEditedUnderStainDescription.value = ""
        mNewUnderStainId.value = 0
        collectAllUnderStainsForTask?.cancel()
    }
}