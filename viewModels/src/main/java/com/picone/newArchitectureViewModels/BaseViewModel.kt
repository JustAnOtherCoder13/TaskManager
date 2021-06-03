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

    enum class JobList(name: String) {
        COLLECT_TASKS("collectTasks"),
        COLLECT_PROJECTS("collectProjects"),
        COLLECT_CATEGORIES("collectCategories"),
        FILTER_TASKS("filterTasks")
    }

    private var collectTasks: Job? = null
    private var collectProjects: Job? = null
    private var collectCategories: Job? = null
    private var filterTasks: Job? = null

    val jobListCollector: MutableMap<JobList, Job?> = mutableMapOf(
        JobList.COLLECT_TASKS to collectTasks,
        JobList.COLLECT_PROJECTS to collectProjects,
        JobList.COLLECT_CATEGORIES to collectCategories,
        JobList.FILTER_TASKS to filterTasks
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