package com.picone.newArchitectureViewModels

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

    enum class JobList {
        COLLECT_TASKS,
        COLLECT_PROJECTS,
        COLLECT_CATEGORIES,
        FILTER_TASKS,
        COLLECT_UNDER_STAIN_FOR_TASK
    }

    private var collectTasks: Job? = null
    private var collectProjects: Job? = null
    private var collectCategories: Job? = null
    private var filterTasks: Job? = null
    private var collectAllUnderStainsForTask: Job? = null

    val jobListCollector: MutableMap<JobList, Job?> = mutableMapOf(
        JobList.COLLECT_TASKS to collectTasks,
        JobList.COLLECT_PROJECTS to collectProjects,
        JobList.COLLECT_CATEGORIES to collectCategories,
        JobList.FILTER_TASKS to filterTasks,
        JobList.COLLECT_UNDER_STAIN_FOR_TASK to collectAllUnderStainsForTask
    )
    protected open fun resetStates(){
        jobListCollector.forEach{ it.value?.cancel()}
    }

    protected fun launchCoroutine(block: suspend CoroutineScope.() -> Unit) :Job?{
        return viewModelScope.launch {
            completionState.value = CompletionState.ON_LOADING
            try { block() }
            catch (e:java.lang.Exception){
                handleException(e)
            }
        }
    }

    protected fun launchCoroutine( onComplete : CompletionState, block: suspend CoroutineScope.() -> Unit){
        viewModelScope.launch {
            completionState.value = CompletionState.ON_LOADING
            try {
                block()
                completionState.value = onComplete
            }
            catch (e:java.lang.Exception){
                handleException(e)
            }
        }
    }

    private fun CoroutineScope.handleException(e: Exception) {
        Log.e(this::class.java.simpleName, "dispatchEvent: ", e)
        completionState.value = CompletionState.ON_ERROR
    }

}