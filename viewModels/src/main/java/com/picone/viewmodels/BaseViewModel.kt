package com.picone.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {



    companion object{
        enum class CompletionState{
            START_STATE,
            UNDER_STAIN_ON_COMPLETE,
            TASK_ON_COMPLETE,
            PROJECT_ON_COMPLETE,
            DELETE_PROJECT_ON_COMPLETE,
            ON_ERROR
        }
        var completionStateMutableLD:MutableLiveData<CompletionState> = MutableLiveData()
    }

}