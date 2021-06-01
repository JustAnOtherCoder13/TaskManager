package com.picone.newArchitectureViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    enum class CompletionState {
        ON_START,
        ON_LOADING,
        ON_ERROR,
        ADD_TASK_ON_COMPLETE,
        ADD_PROJECT_ON_COMPLETE,
        UPDATE_TASK_ON_COMPLETE,
        ADD_CATEGORY_ON_COMPLETE,
    }

}