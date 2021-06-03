package com.picone.newArchitectureViewModels

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

    val mAllUnderStainsForTaskState: MutableState<List<State<UnderStain>>> =
        mutableStateOf(listOf())
    val mIsAddUnderStainComponentVisibleState: MutableState<Boolean> = mutableStateOf(false)
    val mNewUnderStainSelectedDeadLineState: MutableState<String> = mutableStateOf("")
    val mEditedUnderStainNameState: MutableState<String> = mutableStateOf("")
    val mEditedUnderStainDescriptionState: MutableState<String> = mutableStateOf("")
    private val mEditedUnderStainState: MutableState<UnderStain?> = mutableStateOf(null)
    private val mNewUnderStainSelectedTaskIdState: MutableState<Int> = mutableStateOf(0)
    private val mNewUnderStainIdState: MutableState<Int> = mutableStateOf(0)

    fun onStart(selectedTask: Task) = dispatchEvent(DetailActions.OnDetailCreated(selectedTask))

    fun dispatchEvent(detailAction: DetailAction) {
        when (detailAction) {

            is DetailActions.OnDetailCreated -> getAllUnderStainsForTask(detailAction.task.id)

            is DetailActions.OnAddUnderStainButtonClick -> isAddUnderStainComponentVisible(true)

            is DetailActions.AddUnderStainButtonOnCancelButtonClicked -> resetAddUnderStainComponent()

            is DetailActions.OnDatePickerIconClickedOnDateSelected -> mNewUnderStainSelectedDeadLineState.value =
                detailAction.selectedDate

            is DetailActions.NameEditTextOnTextChange -> mEditedUnderStainNameState.value =
                detailAction.underStainName

            is DetailActions.DescriptionEditTextOnTextChange -> mEditedUnderStainDescriptionState.value =
                detailAction.underStainDescription

            is DetailActions.AddUnderStainButtonOnOkButtonClicked -> updateOrAddUnderStain()

            is DetailActions.OnUnderStainMenuItemSelected -> {
                when (detailAction.selectedItem) {
                    EDIT -> updateAddUnderStainComponentValue(detailAction)
                    DELETE -> deleteUnderStain(detailAction)
                    START -> triggerStartUnderStainEvent(detailAction)
                    CLOSE -> closeUnderStain(detailAction)
                    //todo if all under stains closed pop up "would you like to close this task?"
                }
            }
        }
    }

    // FLOW COLLECTORS-----------------------------------------------------------------------------------------------------------
    private fun getAllUnderStainsForTask(taskId: Int) {
        jobListCollector[JobList.COLLECT_UNDER_STAIN_FOR_TASK] =
            viewModelScope.launch {
                mGetAllUnderStainForTaskIdInteractor.getAllUnderStainForTaskId(taskId)
                    .collect { mAllUnderStainsForTaskState.value = it }
            }
        mNewUnderStainSelectedTaskIdState.value = taskId
    }

    //COROUTINES ONE SHOT DELETE OR WRITE--------------------------------------------------------------------------------------------
    private fun deleteUnderStain(detailAction: DetailActions.OnUnderStainMenuItemSelected) =
        launchCoroutine { mDeleteUnderStainInteractor.deleteUnderStain(detailAction.underStain) }

    private fun updateEditedUnderStain() =
        launchCoroutine { mUpdateUnderStainInteractor.updateUnderStain(editedUnderStain) }

    private fun addNewUnderStain() {
        launchCoroutine { mAddNewUnderStainInteractor.addNewUnderStain(newUnderStain) }
        resetAddUnderStainComponent()
    }

    private fun closeUnderStain(detailAction: DetailActions.OnUnderStainMenuItemSelected) {
        detailAction.underStain.close = Calendar.getInstance().time
        launchCoroutine { mUpdateUnderStainInteractor.updateUnderStain(detailAction.underStain) }
    }

    private fun startTask(
        detailAction: DetailActions.OnUnderStainMenuItemSelected,
        startDate: Date
    ) {
        detailAction.selectedTask.start = startDate
        launchCoroutine { mUpdateTaskInteractor.updateTask(detailAction.selectedTask) }
    }

    private fun startUnderStain(
        detailAction: DetailActions.OnUnderStainMenuItemSelected,
        startDate: Date
    ) {
        detailAction.underStain.start = startDate
        launchCoroutine { mUpdateUnderStainInteractor.updateUnderStain(detailAction.underStain) }
    }

    //HELPERS----------------------------------------------------------------------------------------------------------------------
    private val newUnderStain = UnderStain(
        taskId = mNewUnderStainSelectedTaskIdState.value,
        name = mEditedUnderStainNameState.value,
        description = mEditedUnderStainDescriptionState.value,
        start = null,
        deadLine = getSelectedDeadLineOrNull(),
        close = null
    )

    private val editedUnderStain = UnderStain(
        id = mEditedUnderStainState.value?.id?:-1,
        taskId = mEditedUnderStainState.value?.taskId?:-1,
        name = mEditedUnderStainNameState.value,
        description = mEditedUnderStainDescriptionState.value,
        start = mEditedUnderStainState.value?.start,
        deadLine = getSelectedDeadLineOrNull(),
        close = mEditedUnderStainState.value?.close
    )

    private fun triggerStartUnderStainEvent(detailAction: DetailActions.OnUnderStainMenuItemSelected) {
        val startDate = Calendar.getInstance().time
        startUnderStain(detailAction, startDate)
        if (detailAction.selectedTask.start == null) startTask(detailAction, startDate)
    }

    private fun updateOrAddUnderStain() {
        if (mEditedUnderStainState.value != null) launchCoroutine { updateEditedUnderStain() }
        else addNewUnderStain()
        resetAddUnderStainComponent()
    }

    private fun getSelectedDeadLineOrNull() =
        if (mNewUnderStainSelectedDeadLineState.value.trim().isNotEmpty())
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.FRANCE
            ).parse(mNewUnderStainSelectedDeadLineState.value)
        else null

    private fun isAddUnderStainComponentVisible(isVisible: Boolean) {
        mIsAddUnderStainComponentVisibleState.value = isVisible
    }

    private fun updateAddUnderStainComponentValue(detailAction: DetailActions.OnUnderStainMenuItemSelected) {
        mEditedUnderStainState.value = detailAction.underStain
        isAddUnderStainComponentVisible(true)
        mNewUnderStainSelectedDeadLineState.value =
            if (detailAction.underStain.deadLine != null) SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.FRANCE
            ).format(detailAction.underStain.deadLine!!) else ""
        mEditedUnderStainDescriptionState.value = detailAction.underStain.description
        mEditedUnderStainNameState.value = detailAction.underStain.name
    }

    private fun resetAddUnderStainComponent() {
        isAddUnderStainComponentVisible(false)
        mEditedUnderStainState.value = null
        mNewUnderStainSelectedDeadLineState.value = ""
        mEditedUnderStainNameState.value = ""
        mEditedUnderStainDescriptionState.value = ""
    }

    public override fun resetStates() {
        super.resetStates()
        resetAddUnderStainComponent()
        mIsAddUnderStainComponentVisibleState.value = false
        mNewUnderStainSelectedTaskIdState.value = 0
        mNewUnderStainIdState.value = 0
    }
}