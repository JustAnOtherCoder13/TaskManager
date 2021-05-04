package com.picone.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import com.picone.core.domain.interactor.underStain.AddNewUnderStainInteractor
import com.picone.core.domain.interactor.underStain.GetAllUnderStainForTaskIdInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UnderStainViewModel @Inject constructor(
    private val mGetAllUnderStainForTaskIdInteractor: GetAllUnderStainForTaskIdInteractor,
    private val mAddNewUnderStainInteractor: AddNewUnderStainInteractor
) : BaseViewModel() {

    val mAllUnderStainsForTaskMutableLD: MutableLiveData<MutableList<UnderStain>> =
        MutableLiveData(mutableListOf())



    fun getAllUnderStainsForTask(task: Task) {
        viewModelScope.launch {
            mGetAllUnderStainForTaskIdInteractor.getAllUnderStainForTaskId(task.id)
                .collect {
                    mAllUnderStainsForTaskMutableLD.value = it.toMutableList()
                }
        }
    }

    fun addNewUnderStain(underStain: UnderStain) {
        try {
            viewModelScope.launch {
                mAddNewUnderStainInteractor.addNewUnderStain(underStain)
            }
            completionStateMutableLD.value=Companion.CompletionState.UNDER_STAIN_ON_COMPLETE
        }catch (e:Exception){e.printStackTrace()}

    }
}