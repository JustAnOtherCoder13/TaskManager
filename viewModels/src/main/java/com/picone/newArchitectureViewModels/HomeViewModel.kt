package com.picone.newArchitectureViewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import com.picone.core.util.Constants.CATEGORY
import com.picone.core.util.Constants.DELETE
import com.picone.core.util.Constants.EDIT
import com.picone.core.util.Constants.PASS_TO_TASK
import com.picone.core.util.Constants.UnknownProject
import com.picone.core.util.Constants.UnknownTask
import com.picone.newArchitectureViewModels.androidUiManager.HomeAction
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.HomeActions
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

    val mAllTasksState: MutableState<MutableList<Task>> = mutableStateOf(mutableListOf())
    val mAllProjectState: MutableState<MutableList<Project>> = mutableStateOf(mutableListOf())
    val mAllCategoriesState: MutableState<MutableList<Category>> = mutableStateOf(mutableListOf())
    val mIsAddCategoryPopUpExpanded: MutableState<Boolean> = mutableStateOf(false)
    private val mNewCategorySelectedColor: MutableState<Long> = mutableStateOf(0)
    private val mNewCategoryName: MutableState<String> = mutableStateOf("")
    var completionState: MutableLiveData<BaseViewModel.CompletionState> =
        MutableLiveData(BaseViewModel.CompletionState.ON_START)

    fun onStart(destination: String) {
        when (destination) {
            AndroidNavObjects.Home.destination -> {
                currentDestinationMutableLD.value = AndroidNavObjects.Home.destination
                dispatchEvent(HomeActions.OnHomeCreated)
            }
            AndroidNavObjects.Project.destination -> {
                currentDestinationMutableLD.value = AndroidNavObjects.Project.destination
                dispatchEvent(HomeActions.OnProjectCreated)
            }
        }
    }

    fun onStop() = resetStates()

    fun dispatchEvent(homeAction: HomeAction) {
        when (homeAction) {
            is HomeActions.OnHomeCreated -> {
                getAllTasks()
                getAllCategories()
            }

            is HomeActions.OnProjectCreated ->
                getAllProjects()

            is HomeActions.BottomNavBarOnNavItemSelected ->
                homeAction.androidNavActionManager.onBottomNavItemSelected(homeAction.selectedNavItem)

            is HomeActions.TopBarOnMenuItemSelected ->
                if (homeAction.selectedItem != CATEGORY) {
                    homeAction.androidNavActionManager.navigate(
                        AndroidNavObjects.Add,
                        homeAction.selectedItem,
                        Gson().toJson(homeAction.selectedTask),
                        Gson().toJson(UnknownProject)
                    )
                } else if (homeAction.selectedItem == CATEGORY) {
                    setAddCategoryPopUpMenuExpanded(true)
                }

            is HomeActions.TaskRecyclerViewOnTaskSelected ->
                homeAction.androidNavActionManager.navigate(
                    AndroidNavObjects.Detail,
                    Gson().toJson(homeAction.selectedTask)
                )

            is HomeActions.OnDeleteTaskSelected -> {
                Log.i("TAG", "dispatchEvent: home"+currentDestinationMutableLD.value)
                deleteTask(homeAction.task)
            }

            is HomeActions.OnEditTaskSelected -> {
                homeAction.androidNavActionManager.navigate(
                    AndroidNavObjects.Add,
                    homeAction.selectedItem,
                    Gson().toJson(homeAction.task),
                    Gson().toJson(UnknownProject)
                )
            }

            is HomeActions.CloseCategoryPopUp ->
                setAddCategoryPopUpMenuExpanded(false)

            is HomeActions.AddCategoryOnColorSelected ->
                mNewCategorySelectedColor.value = homeAction.color

            is HomeActions.AddCategoryOnTextChange ->
                mNewCategoryName.value = homeAction.name

            is HomeActions.AddCategoryOnOkButtonClicked -> {
                addNewCategory()
                setAddCategoryPopUpMenuExpanded(false)
            }
            //todo json error when delete project before edit following
            is HomeActions.ProjectRecyclerViewOnMenuItemSelected -> {
                when (homeAction.selectedItem) {
                    EDIT -> homeAction.androidNavActionManager.navigate(
                        AndroidNavObjects.Add,
                        homeAction.selectedItem,
                        Gson().toJson(UnknownTask),
                        Gson().toJson(homeAction.project)
                    )
                    //todo show date picker icon
                    PASS_TO_TASK-> homeAction.androidNavActionManager.navigate(
                        AndroidNavObjects.Add,
                        homeAction.selectedItem,
                        Gson().toJson(UnknownTask),
                        Gson().toJson(homeAction.project)
                    )
                    DELETE -> viewModelScope.launch {
                        mDeleteProjectInteractor.deleteProject(homeAction.project)
                    }
                }

            }


        }
    }

    private fun addNewCategory() {
        viewModelScope.launch {
            completionState.value = BaseViewModel.CompletionState.ON_LOADING
            try {
                mAddNewCategoryInteractor.addNewCategory(
                    Category(
                        color = mNewCategorySelectedColor.value,
                        name = mNewCategoryName.value
                    )
                )
                completionState.value = BaseViewModel.CompletionState.ADD_CATEGORY_ON_COMPLETE
            } catch (e: Exception) {
                Log.e(this::class.java.simpleName, "dispatchEvent: ", e)
                completionState.value = BaseViewModel.CompletionState.ON_ERROR
            }
        }
    }

    private fun getAllCategories() {
        viewModelScope.launch {
            mGetAllCategoriesInteractor.allCategoriesFlow.collect {
                mAllCategoriesState.value = it.toMutableList()
            }
        }
    }

    private fun setAddCategoryPopUpMenuExpanded(isExpanded: Boolean) {
        mIsAddCategoryPopUpExpanded.value = isExpanded
    }

    private fun getAllProjects() {
        viewModelScope.launch {
            mGetAllProjectInteractor.allProjectsFlow
                .collect { mAllProjectState.value = it.toMutableList() }
        }
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            mGetAllTasksInteractor.allTasksFlow
                .collect {
                    mAllTasksState.value = it.toMutableList()

                }
        }
    }

    private fun deleteTask(task: Task) {
        viewModelScope.launch {
            mDeleteTaskInteractor.deleteTask(task)
        }
    }

    private fun resetStates() {
        mAllTasksState.value = mutableListOf()
        mAllProjectState.value = mutableListOf()
        completionState.value = BaseViewModel.CompletionState.ON_START
        mNewCategoryName.value = ""
        mNewCategorySelectedColor.value = 0
        mIsAddCategoryPopUpExpanded.value = false
    }

}