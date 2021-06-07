package com.picone.newArchitectureViewModels

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


abstract class BaseViewModel : ViewModel() {

    protected open var completionState: MutableLiveData<CompletionState> =
        MutableLiveData(CompletionState.ON_START)

    enum class CompletionState {
        ON_START,
        ON_LOADING,
        ON_ERROR,
        ADD_TASK_ON_COMPLETE,
        ADD_PROJECT_ON_COMPLETE,
        UPDATE_TASK_ON_COMPLETE,
        ADD_CATEGORY_ON_COMPLETE,
        UPDATE_PROJECT_ON_COMPLETE,
    }

    //JOB TO STOP FLOW WHEN SCREEN IS NOT SHOWN
    private var collectTasks: Job? = null
    private var collectProjects: Job? = null
    private var collectCategories: Job? = null
    private var filterTasks: Job? = null
    private var collectAllUnderStainsForTask: Job? = null

    protected enum class JobList {
        COLLECT_TASKS,
        COLLECT_PROJECTS,
        COLLECT_CATEGORIES,
        FILTER_TASKS,
        COLLECT_UNDER_STAIN_FOR_TASK,
        COLLECT_TASKS_ON_ADD,
        COLLECT_CATEGORIES_ON_ADD
    }

    protected val jobListHomeCollector: MutableMap<JobList, Job?> = mutableMapOf(
        JobList.COLLECT_TASKS to collectTasks,
        JobList.COLLECT_PROJECTS to collectProjects,
        JobList.COLLECT_CATEGORIES to collectCategories,
        JobList.FILTER_TASKS to filterTasks,
        JobList.COLLECT_UNDER_STAIN_FOR_TASK to collectAllUnderStainsForTask,
    )

    protected val jobListAddCollector: MutableMap<JobList, Job?> = mutableMapOf(
        JobList.COLLECT_TASKS_ON_ADD to collectTasks,
        JobList.COLLECT_CATEGORIES_ON_ADD to collectCategories
    )

    protected open fun resetStates() {
        jobListHomeCollector.forEach { it.value?.cancel() }
        jobListAddCollector.forEach { it.value?.cancel() }
    }

    //COROUTINES------------------------------------------------------------------------------------
    protected fun launchCoroutine(
        viewModel: ViewModel,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModel.viewModelScope.launch() {
            completionState.value = CompletionState.ON_LOADING
            try {
                block()
                completionState.value = CompletionState.ON_START
            } catch (e: SQLiteException) {
                completionState.value = CompletionState.ON_ERROR
                handleException(e)
            }
        }
    }

    protected fun launchCoroutine(
        onComplete: CompletionState,
        viewModel: ViewModel,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModel.viewModelScope.launch() {
            completionState.value = CompletionState.ON_LOADING
            try {
                block()
                completionState.value = onComplete
            } catch (e: SQLiteException) {
                completionState.value = CompletionState.ON_ERROR
                handleException(e)
            }
        }
    }

    protected fun CoroutineScope.handleException(e: Throwable) {
        Log.e(this::class.java.simpleName, "dispatchEvent: ", e)
        completionState.value = CompletionState.ON_ERROR
    }

}