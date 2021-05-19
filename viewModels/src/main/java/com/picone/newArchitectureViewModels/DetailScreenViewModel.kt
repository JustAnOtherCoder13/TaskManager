package com.picone.newArchitectureViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import com.picone.core.domain.interactor.underStain.GetAllUnderStainForTaskIdInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val mGetAllUnderStainForTaskIdInteractor: GetAllUnderStainForTaskIdInteractor

): ViewModel() {

    var allUnderStainsForTaskMutableLD : MutableLiveData<List<UnderStain>> = MutableLiveData(
        emptyList())

    fun getAllUnderStainForTask(task: Task){
        viewModelScope.launch {
            mGetAllUnderStainForTaskIdInteractor.getAllUnderStainForTaskId(taskId = task.id)
                .collect {
                    allUnderStainsForTaskMutableLD.value = it.toMutableList()
                }
        }
    }

}