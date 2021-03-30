package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.*
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import com.picone.core.domain.interactor.underStain.AddNewUnderStainInteractor
import com.picone.core.domain.interactor.underStain.GetAllUnderStainForTaskIdInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnderStainViewModel @Inject constructor(
    private val getAllUnderStainForTaskIdInteractor: GetAllUnderStainForTaskIdInteractor,
    private val addNewUnderStainInteractor: AddNewUnderStainInteractor
) : BaseViewModel() {
    lateinit var underStain: MutableLiveData<List<UnderStain>>

    fun allUnderStainForTask(task: Task) =
        viewModelScope.launch {
            getAllUnderStainForTaskIdInteractor.getAllUnderStainForTaskId(task.id)
                .collect {
                    underStain.value = it
                }
        }


    fun addNewUnderStain(underStain: UnderStain) =
        viewModelScope.launch {
            addNewUnderStainInteractor.addNewUnderStain(underStain)
        }
}