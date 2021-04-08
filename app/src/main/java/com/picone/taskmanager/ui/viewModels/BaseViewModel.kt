package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    companion object{
        enum class CompletionState{
            START_STATE,
            ON_COMPLETE,
            ON_ERROR
        }
    }

}