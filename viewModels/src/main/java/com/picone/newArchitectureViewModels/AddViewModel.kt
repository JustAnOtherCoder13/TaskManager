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
import com.picone.core.domain.interactor.project.DeleteProjectInteractor
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
import kotlinx.coroutines.Job
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
    private val mUpdateTaskInteractor: UpdateTaskInteractor,
    private val mDeleteProjectInteractor: DeleteProjectInteractor
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

    private var collectTasks: Job? = null
    private var collectAllCategories: Job? = null

    private var editedItemRelatedCategory: MutableLiveData<Category> = MutableLiveData()

    fun onStart(selectedTask: Task?, selectedProject: Project?) {
        dispatchEvent(AddActions.OnAddCreated(selectedTask, selectedProject))
    }

    fun onStop() = resetStates()

    fun dispatchEvent(addAction: AddAction) {
        when (addAction) {
            is AddActions.OnDatePickerIconClickedOnDateSelected ->
                updateSelectedDeadline(addAction)

            is AddActions.OnAddCreated -> {
                collectAllCategories = viewModelScope.launch {
                    mGetAllCategoriesInteractor.allCategoriesFlow.collect { allCategories ->
                        if (addAction.selectedProject != null || addAction.selectedTask != null) {
                            editedItemRelatedCategory.value = allCategories.filter {
                                it.id == addAction.selectedTask?.categoryId ?: addAction.selectedProject?.categoryId
                            }[FIRST_ELEMENT]
                            mNewItemCategory.value = editedItemRelatedCategory.value?.name
                        }

                        mAllCategories.value = allCategories

                        if (addAction.selectedTask != null) {
                            updateTaskUiValue(addAction.selectedTask)
                        } else if (addAction.selectedProject != null) {
                            updateProjectUiValue(addAction.selectedProject)
                        }
                    }
                }
            }

            is AddActions.OnAddScreenImportanceSelected ->
                mNewTaskImportance.value = addAction.importance

            is AddActions.OnAddScreenCategorySelected -> {
                mNewItemCategory.value = addAction.category
                editedItemRelatedCategory.value = mAllCategories.value.filter {
                    it.name == addAction.category
                }[FIRST_ELEMENT]
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
                if (addAction.editedTask != null && addAction.editedTask.id != UnknownTask.id && addAction.selectedItemType != PASS_TO_TASK)
                    updateEditedTask(addAction.editedTask)
                else {
                    when (addAction.selectedItemType) {
                        TASK -> addNewTask()
                        PROJECT -> addNewProject()
                        EDIT -> Log.i("TAG", "dispatchEvent: edit")
                        PASS_TO_TASK -> {
                            if (mNewItemDescription.value.trim()
                                    .isEmpty()
                            ) mNewItemDescription.value = addAction.editedTask?.description!!
                            if (mNewItemName.value.trim().isEmpty()) mNewItemName.value =
                                addAction.editedTask?.name!!
                            if (editedItemRelatedCategory.value == null) editedItemRelatedCategory.value =
                                mAllCategories.value.filter {
                                    it.id == addAction.editedTask?.id!!
                                }[FIRST_ELEMENT]
                            addNewTask(addAction.editedProject)
                        }
                    }
                }
            }

            is AddActions.NavigateToDetailOnAddTaskComplete -> {
                collectTasks = viewModelScope.launch {
                    mGetAllTasksInteractor.allTasksFlow.collect {
                        val taskToJson = Gson().toJson(it.filter { task ->
                            task.name == mNewItemName.value && task.description == mNewItemDescription.value
                        }[FIRST_ELEMENT])

                        addAction.androidNavActionManager.navigate(
                            AndroidNavObjects.Detail,
                            taskToJson
                        )
                    }
                }
            }

            is AddActions.NavigateToProjectOnAddProjectComplete ->
                addAction.androidNavActionManager.navigate(AndroidNavObjects.Project)

            is AddActions.NavigateToHomeOnUpdateTaskComplete ->
                addAction.androidNavActionManager.navigate(AndroidNavObjects.Home)

            is AddActions.DeleteProjectOnProjectPassInTaskComplete ->
                viewModelScope.launch {
                    completionState.value = CompletionState.ON_LOADING
                    try {
                        mDeleteProjectInteractor.deleteProject(addAction.project)
                        completionState.value = CompletionState.ADD_TASK_ON_COMPLETE
                    } catch (e: Exception) {
                        Log.e(this::class.java.simpleName, "dispatchEvent: ", e)
                        completionState.value = CompletionState.ON_ERROR
                    }
                }

        }
    }

    private fun updateEditedTask(editedTask: Task) {
        viewModelScope.launch {
            completionState.value = CompletionState.ON_LOADING
            try {
                mUpdateTaskInteractor.updateTask(
                    Task(
                        id = editedTask.id,
                        categoryId = editedItemRelatedCategory.value?.id!!,
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
                        categoryId = editedItemRelatedCategory.value?.id!!,
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

    private fun addNewTask(projectToDelete : Project? = null) {
        viewModelScope.launch {
            completionState.value = CompletionState.ON_LOADING
            try {
                mAddNewTaskInteractor.addNewTask(
                    Task(
                        categoryId = editedItemRelatedCategory.value?.id!!,
                        close = null,
                        creation = Calendar.getInstance().time,
                        importance = IMPORTANCE_LIST.indexOf(mNewTaskImportance.value ?: 0),
                        deadLine = getSelectedDeadLineOrNull(),
                        description = mNewItemDescription.value,
                        name = mNewItemName.value,
                        start = null
                    )
                )
                if (projectToDelete == null)
                    completionState.value = CompletionState.ADD_TASK_ON_COMPLETE
                else
                    dispatchEvent(AddActions.DeleteProjectOnProjectPassInTaskComplete(projectToDelete))

            } catch (e: Exception) {
                Log.e(this::class.java.simpleName, "dispatchEvent: ", e)
                completionState.value = CompletionState.ON_ERROR
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
        mNewTaskImportance.value = "Importance"
        mNewItemCategory.value = CATEGORY
        mNewItemName.value = ""
        mNewItemDescription.value = ""
        mIsOkButtonEnable.value = false
        collectTasks?.cancel()
        collectAllCategories?.cancel()
    }

    private fun updateProjectUiValue(selectedProject: Project) {
        mNewItemName.value = selectedProject.name
        mNewItemDescription.value = selectedProject.description
    }

}