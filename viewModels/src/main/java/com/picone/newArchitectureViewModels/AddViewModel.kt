package com.picone.newArchitectureViewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import com.picone.core.domain.interactor.project.AddNewProjectInteractor
import com.picone.core.domain.interactor.project.DeleteProjectInteractor
import com.picone.core.domain.interactor.project.UpdateProjectInteractor
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
    private val mDeleteProjectInteractor: DeleteProjectInteractor,
    private val mUpdateProjectInteractor: UpdateProjectInteractor
) : BaseViewModel() {

    val mNewTaskSelectedDeadLine: MutableState<String> = mutableStateOf("")
    val mAllCategories: MutableState<List<Category>> = mutableStateOf(listOf())
    val mNewTaskImportance: MutableState<String?> = mutableStateOf(null)
    val mNewItemCategory: MutableState<String?> = mutableStateOf(null)
    val mNewItemName: MutableState<String> = mutableStateOf("")
    val mNewItemDescription: MutableState<String> = mutableStateOf("")
    val mIsOkButtonEnable: MutableState<Boolean> = mutableStateOf(false)
    private val editedItemRelatedCategory: MutableLiveData<Category> = MutableLiveData()
    public override var completionState: MutableLiveData<CompletionState> =
        MutableLiveData(CompletionState.ON_START)

    fun onStart(selectedTask: Task?, selectedProject: Project?) =
        dispatchEvent(AddActions.OnAddCreated(selectedTask, selectedProject))


    fun dispatchEvent(addAction: AddAction) {
        when (addAction) {
            is AddActions.OnDatePickerIconClickedOnDateSelected -> mNewTaskSelectedDeadLine.value =
                addAction.selectedDate

            is AddActions.OnAddCreated -> initUi(addAction)

            is AddActions.OnAddScreenImportanceSelected -> mNewTaskImportance.value =
                addAction.importance

            is AddActions.OnAddScreenCategorySelected -> {
                updateSelectedCategory(addAction)
                Log.i("TAG", "dispatchEvent: "+mNewItemCategory.value+" "+mNewItemDescription.value+" "+mNewItemName.value)
                mIsOkButtonEnable.value = isOkButtonEnable()
            }

            is AddActions.AddScreenOnNameChange -> {
                Log.i("TAG", "dispatchEvent: "+mNewItemCategory.value+" "+mNewItemDescription.value+" "+mNewItemName.value)
                mNewItemName.value = addAction.name
                mIsOkButtonEnable.value = isOkButtonEnable()
            }

            is AddActions.AddScreenOnDescriptionChange -> {
                Log.i("TAG", "dispatchEvent: "+mNewItemCategory.value+" "+mNewItemDescription.value+" "+mNewItemName.value)
                mNewItemDescription.value = addAction.description
                mIsOkButtonEnable.value = isOkButtonEnable()
            }

            is AddActions.AddScreenAddNewItemOnOkButtonClicked -> {
                if (addAction.editedTask != null && addAction.editedTask.id != UnknownTask.id && addAction.selectedItemType != PASS_TO_TASK)
                    updateEditedTask(addAction.editedTask)
                else {
                    when (addAction.selectedItemType) {
                        TASK -> addNewTask()
                        PROJECT -> addNewProject()
                        EDIT -> updateProject(addAction)
                        PASS_TO_TASK -> {
                            getProjectValueIfNoValueChange(addAction)
                            addNewTask(addAction.editedProject)
                        }
                    }
                }
            }

            is AddActions.NavigateToDetailOnAddTaskComplete -> navToDetail(addAction)

            is AddActions.NavigateToProjectOnProjectComplete ->
                addAction.androidNavActionManager.navigate(AndroidNavObjects.Project)

            is AddActions.NavigateToHomeOnUpdateTaskComplete ->
                addAction.androidNavActionManager.navigate(AndroidNavObjects.Home)

            is AddActions.DeleteProjectOnProjectPassInTaskComplete ->
                launchCoroutine(CompletionState.ADD_TASK_ON_COMPLETE) {
                    mDeleteProjectInteractor.deleteProject(addAction.project)
                }
        }
    }

    //NAV ACTIONS---------------------------------------------------------------------------------------------------------------
    private fun navToDetail(addAction: AddActions.NavigateToDetailOnAddTaskComplete) {
        jobListAddCollector[JobList.COLLECT_TASKS_ON_ADD] = launchCoroutine {
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

    // FLOW COLLECTORS--------------------------------------------------------------------------------------------------------
    private fun initUi(addAction: AddActions.OnAddCreated) {
        jobListAddCollector[JobList.COLLECT_CATEGORIES_ON_ADD] = launchCoroutine {
            mGetAllCategoriesInteractor.allCategoriesFlow.collect { allCategories ->
                initEditedItemCategory(addAction, allCategories)
                mAllCategories.value = allCategories
                updateUiWithEditedItemValue(addAction)
            }
        }
    }

    //COROUTINES ONE SHOT DELETE OR WRITE--------------------------------------------------------------------------------------------
    private fun updateProject(addAction: AddActions.AddScreenAddNewItemOnOkButtonClicked) {
        launchCoroutine(CompletionState.UPDATE_PROJECT_ON_COMPLETE) {
            mUpdateProjectInteractor.updateProject(updatedProject(addAction))
        }
    }

    private fun updateEditedTask(editedTask: Task) {
        launchCoroutine(CompletionState.UPDATE_TASK_ON_COMPLETE) {
            mUpdateTaskInteractor.updateTask(updatedTask(editedTask))
        }
    }

    private fun addNewProject() {
        launchCoroutine(CompletionState.ADD_PROJECT_ON_COMPLETE) {
            mAddNewProjectInteractor.addNewProject(newProject)
        }
    }

    private fun addNewTask(projectToDelete: Project? = null) {
        launchCoroutine {
            mAddNewTaskInteractor.addNewTask(newTask)
            if (projectToDelete == null) completionState.value =
                CompletionState.ADD_TASK_ON_COMPLETE
            else dispatchEvent(AddActions.DeleteProjectOnProjectPassInTaskComplete(projectToDelete))
        }
    }

    //HELPERS----------------------------------------------------------------------------------------------------------------------
    private fun updatedProject(addAction: AddActions.AddScreenAddNewItemOnOkButtonClicked) =
        Project(
            id = addAction.editedProject?.id ?: -1,
            categoryId = if (editedItemRelatedCategory.value != null) editedItemRelatedCategory.value?.id
                ?: 0 else addAction.editedProject?.categoryId ?: 0,
            name = mNewItemName.value,
            description = mNewItemDescription.value
        )

    private fun updatedTask(editedTask: Task) = Task(
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

    private val newProject = Project(
        categoryId = editedItemRelatedCategory.value?.id ?: 0,
        name = mNewItemName.value,
        description = mNewItemDescription.value
    )

    private val newTask = Task(
        categoryId = editedItemRelatedCategory.value?.id ?: 0,
        close = null,
        creation = Calendar.getInstance().time,
        importance = IMPORTANCE_LIST.indexOf(mNewTaskImportance.value ?: 0),
        deadLine = getSelectedDeadLineOrNull(),
        description = mNewItemDescription.value,
        name = mNewItemName.value,
        start = null
    )

    private fun getProjectValueIfNoValueChange(addAction: AddActions.AddScreenAddNewItemOnOkButtonClicked) {
        if (mNewItemDescription.value.trim().isEmpty())
            mNewItemDescription.value = addAction.editedTask?.description ?: ""
        if (mNewItemName.value.trim().isEmpty()) mNewItemName.value =
            addAction.editedTask?.name ?: ""
        if (editedItemRelatedCategory.value == null) editedItemRelatedCategory.value =
            mAllCategories.value.filter { it.id == addAction.editedTask?.id ?: 0 }[FIRST_ELEMENT]
    }

    private fun updateSelectedCategory(addAction: AddActions.OnAddScreenCategorySelected) {
        mNewItemCategory.value = addAction.category
        editedItemRelatedCategory.value = mAllCategories.value.filter {
            it.name == addAction.category
        }[FIRST_ELEMENT]
    }

    private fun updateUiWithEditedItemValue(addAction: AddActions.OnAddCreated) {
        if (addAction.selectedTask != null) updateTaskUiValue(addAction.selectedTask)
        else if (addAction.selectedProject != null) updateProjectUiValue(addAction.selectedProject)
    }

    private fun initEditedItemCategory(
        addAction: AddActions.OnAddCreated,
        allCategories: List<Category>
    ) {
        if (addAction.selectedProject != null || addAction.selectedTask != null) {
            editedItemRelatedCategory.value = editedItemCategory(allCategories, addAction)
            mNewItemCategory.value = editedItemRelatedCategory.value?.name
        }
    }

    private fun editedItemCategory(
        allCategories: List<Category>,
        addAction: AddActions.OnAddCreated
    ) = allCategories.filter {
        it.id == addAction.selectedTask?.categoryId ?: addAction.selectedProject?.categoryId
    }[FIRST_ELEMENT]

    private fun updateTaskUiValue(selectedTask: Task) {
        mNewTaskSelectedDeadLine.value =
            if (selectedTask.deadLine != null) {
                SimpleDateFormat("dd/MM/yyy", Locale.FRANCE).format(selectedTask.deadLine!!)
            } else ""
        mNewTaskImportance.value =
            if (selectedTask.importance >= 0) IMPORTANCE_LIST[selectedTask.importance] else IMPORTANCE_LIST[0]
        mNewItemName.value = selectedTask.name
        mNewItemDescription.value = selectedTask.description
    }

    private fun updateProjectUiValue(selectedProject: Project) {
        mNewItemName.value = selectedProject.name
        mNewItemDescription.value = selectedProject.description
    }

    private fun isOkButtonEnable(): Boolean =
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

    public override fun resetStates() {
        super.resetStates()
        completionState.value = CompletionState.ON_START
        mNewTaskSelectedDeadLine.value = ""
        mNewTaskImportance.value = "Importance"
        mNewItemCategory.value = CATEGORY
        mNewItemName.value = ""
        mNewItemDescription.value = ""
        mIsOkButtonEnable.value = false
    }
}