package com.picone.newArchitectureViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.domain.interactor.category.AddNewCategoryInteractor
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import com.picone.core.domain.interactor.project.DeleteProjectInteractor
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.task.DeleteTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import com.picone.core.util.Constants.ALL
import com.picone.core.util.Constants.CATEGORY
import com.picone.core.util.Constants.DELETE
import com.picone.core.util.Constants.EDIT
import com.picone.core.util.Constants.FIRST_ELEMENT
import com.picone.core.util.Constants.IMPORTANCE_LIST
import com.picone.core.util.Constants.IMPORTANT
import com.picone.core.util.Constants.NORMAL
import com.picone.core.util.Constants.PASS_TO_TASK
import com.picone.core.util.Constants.UNIMPORTANT
import com.picone.core.util.Constants.UnknownProject
import com.picone.core.util.Constants.UnknownTask
import com.picone.newArchitectureViewModels.androidUiManager.HomeAction
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.HomeActions
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mGetAllTasksInteractor: GetAllTasksInteractor,
    private val mDeleteTaskInteractor: DeleteTaskInteractor,
    private val mGetAllProjectInteractor: GetAllProjectInteractor,
    private val mGetAllCategoriesInteractor: GetAllCategoriesInteractor,
    private val mAddNewCategoryInteractor: AddNewCategoryInteractor,
    private val mDeleteProjectInteractor: DeleteProjectInteractor
) : BaseViewModel() {

    var mAllTasksState: MutableState<List<Task>> = mutableStateOf(listOf())
    var mAllProjectState: MutableState<List<Project>> = mutableStateOf(listOf())
    var mAllCategoriesState: MutableState<List<Category>> = mutableStateOf(listOf())
    var mIsAddCategoryPopUpExpandedState: MutableState<Boolean> = mutableStateOf(false)
    private var mNewCategorySelectedColorState: MutableState<Long> = mutableStateOf(0)
    private var mNewCategoryNameState: MutableState<String> = mutableStateOf("")
    public override var completionState: MutableLiveData<CompletionState> =
        MutableLiveData(CompletionState.ON_START)

    fun onStart(destination: String) {
        jobListHomeCollector[JobList.COLLECT_CATEGORIES] = launchCoroutine(this) {
            mGetAllCategoriesInteractor.allCategoriesFlow.collect { mAllCategoriesState.value = it }
        }

        when (destination) {
            AndroidNavDirections.Home.destination -> dispatchEvent(HomeActions.OnHomeCreated)
            AndroidNavDirections.Project.destination -> dispatchEvent(HomeActions.OnProjectCreated)
        }
    }

    fun dispatchEvent(homeAction: HomeAction) {
        when (homeAction) {
            is HomeActions.OnHomeCreated -> getAllTasks()

            is HomeActions.OnProjectCreated -> getAllProjects()

            is HomeActions.BottomNavBarOnNavItemSelected ->
                homeAction.androidNavActionManager.onBottomNavItemSelected(homeAction.selectedNavItem)

            is HomeActions.TopBarOnMenuItemSelected -> triggerSelectedItemAction(homeAction)

            is HomeActions.TaskRecyclerViewOnTaskSelected -> navToDetail(homeAction)

            is HomeActions.CloseCategoryPopUp -> setAddCategoryPopUpMenuExpanded(false)

            is HomeActions.AddCategoryOnColorSelected -> mNewCategorySelectedColorState.value =
                homeAction.color

            is HomeActions.AddCategoryOnTextChange -> mNewCategoryNameState.value = homeAction.name

            is HomeActions.AddCategoryOnOkButtonClicked -> {
                addNewCategory()
                setAddCategoryPopUpMenuExpanded(false)
            }

            is HomeActions.TaskRecyclerViewOnMenuItemSelected -> {
                when (homeAction.selectedItem) {
                    DELETE -> deleteTask(homeAction.task)
                    //todo pop up to confirm delete
                    EDIT -> navToAddToEditTask(homeAction)
                }
            }

            is HomeActions.ProjectRecyclerViewOnMenuItemSelected -> {
                when (homeAction.selectedItem) {
                    EDIT -> navToAddToEditProject(homeAction)
                    PASS_TO_TASK -> navToAddToPassProjectToTask(homeAction)
                    DELETE -> deleteProject(homeAction.project)
                }
            }

            is HomeActions.OnFilterItemSelected -> {
                when (homeAction.selectedItem) {
                    ALL -> getAllTasks()
                    IMPORTANT -> triggerImportanceFilter(IMPORTANT)
                    NORMAL -> triggerImportanceFilter(NORMAL)
                    UNIMPORTANT -> triggerImportanceFilter(UNIMPORTANT)
                    selectedCategory(homeAction) -> triggerCategoryFilter(homeAction.selectedItem)
                }
            }
        }
    }

    //NAV ACTIONS---------------------------------------------------------------------------------------------------------------
    private fun triggerSelectedItemAction(homeAction: HomeActions.TopBarOnMenuItemSelected) {
        if (homeAction.selectedItem != CATEGORY) {
            homeAction.androidNavActionManager.navigate(
                AndroidNavDirections.Add,
                homeAction.selectedItem,
                Gson().toJson(UnknownTask),
                Gson().toJson(UnknownProject)
            )
        } else if (homeAction.selectedItem == CATEGORY) {
            setAddCategoryPopUpMenuExpanded(true)
        }
    }

    private fun navToDetail(homeAction: HomeActions.TaskRecyclerViewOnTaskSelected) {
        homeAction.androidNavActionManager.navigate(
            AndroidNavDirections.Detail,
            Gson().toJson(homeAction.selectedTask)
        )
    }

    private fun navToAddToEditTask(homeAction: HomeActions.TaskRecyclerViewOnMenuItemSelected) {
        homeAction.androidNavActionManager.navigate(
            AndroidNavDirections.Add,
            homeAction.selectedItem,
            Gson().toJson(homeAction.task),
            Gson().toJson(UnknownProject)
        )
    }

    private fun navToAddToPassProjectToTask(homeAction: HomeActions.ProjectRecyclerViewOnMenuItemSelected) {
        homeAction.androidNavActionManager.navigate(
            AndroidNavDirections.Add,
            homeAction.selectedItem,
            Gson().toJson(projectToTask(homeAction)),
            Gson().toJson(homeAction.project)
        )
    }

    private fun navToAddToEditProject(homeAction: HomeActions.ProjectRecyclerViewOnMenuItemSelected) {
        homeAction.androidNavActionManager.navigate(
            AndroidNavDirections.Add,
            homeAction.selectedItem,
            Gson().toJson(UnknownTask),
            Gson().toJson(homeAction.project)
        )
    }

    // FLOW COLLECTORS--------------------------------------------------------------------------------------------------------
    private fun triggerCategoryFilter(selectedCategoryName: String) {
        jobListHomeCollector[JobList.FILTER_TASKS] =
            launchCoroutine(this) {
                mGetAllTasksInteractor.allTasksFlow.collect { allTasks ->
                    mAllTasksState.value =
                        allTasks.filter { task ->                                           //filter all tasks
                            mAllCategoriesState.value.filter { category ->
                                category.name == selectedCategoryName                       //get selected category
                            }[FIRST_ELEMENT].id == task.categoryId                          //get task if selected category
                            // id is equal to task category id
                        }
                }
            }
    }

    private fun triggerImportanceFilter(selectedImportance: String) {
        jobListHomeCollector[JobList.FILTER_TASKS] = viewModelScope.launch {
            completionState.value = CompletionState.ON_START

            mGetAllTasksInteractor.allTasksFlow
                .catch { e -> handleException(e) }
                .collect { allTasks ->                       //filter all tasks
                    mAllTasksState.value = allTasks.filter { task ->
                        if (task.importance >= 0)                                               //if task importance is set
                            IMPORTANCE_LIST[task.importance] == selectedImportance              //get task if importance is equal
                        else false                                                              //to selected importance
                    }
                    completionState.value = CompletionState.ON_START
                }
        }
    }

    private fun getAllProjects() {
        jobListHomeCollector[JobList.COLLECT_PROJECTS] =
            viewModelScope.launch {
                mGetAllProjectInteractor.allProjectsFlow
                    .catch { e -> handleException(e) }
                    .collect {
                        mAllProjectState.value = it
                        completionState.value = CompletionState.ON_START
                    }
            }
    }

    private fun getAllTasks() {
        jobListHomeCollector[JobList.COLLECT_TASKS] =
            viewModelScope.launch {
                mGetAllTasksInteractor.allTasksFlow
                    .catch { e -> handleException(e) }
                    .collect {
                        mAllTasksState.value = it
                        completionState.value = CompletionState.ON_START
                    }
            }
    }

    //COROUTINES ONE SHOT DELETE OR WRITE--------------------------------------------------------------------------------------------
    private fun addNewCategory() {
        launchCoroutine(CompletionState.ADD_CATEGORY_ON_COMPLETE, this) {
            mAddNewCategoryInteractor.addNewCategory(newCategory())
        }
    }

    private fun deleteTask(task: Task) =
        launchCoroutine(this) { mDeleteTaskInteractor.deleteTask(task) }

    private fun deleteProject(project: Project) =
        launchCoroutine(this) { mDeleteProjectInteractor.deleteProject(project) }

    //HELPERS----------------------------------------------------------------------------------------------------------------------
    private fun newCategory() = Category(
        color = mNewCategorySelectedColorState.value,
        name = mNewCategoryNameState.value
    )

    private fun projectToTask(homeAction: HomeActions.ProjectRecyclerViewOnMenuItemSelected) =
        Task(
            categoryId = homeAction.project.categoryId,
            close = null,
            creation = Calendar.getInstance().time,
            importance = -1,
            deadLine = null,
            description = homeAction.project.description,
            name = homeAction.project.name,
            start = null
        )

    private fun setAddCategoryPopUpMenuExpanded(isExpanded: Boolean) {
        mIsAddCategoryPopUpExpandedState.value = isExpanded
    }

    private fun selectedCategory(homeAction: HomeActions.OnFilterItemSelected) =
        mAllCategoriesState.value.filter { it.name == homeAction.selectedItem }[FIRST_ELEMENT].name

    public override fun resetStates() {
        super.resetStates()
        mAllTasksState.value = listOf()
        mAllProjectState.value = listOf()
        completionState.value = CompletionState.ON_START
        mNewCategoryNameState.value = ""
        mNewCategorySelectedColorState.value = 0
        mIsAddCategoryPopUpExpandedState.value = false
    }
}