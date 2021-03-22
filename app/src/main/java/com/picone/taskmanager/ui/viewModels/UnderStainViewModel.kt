package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.picone.core.domain.interactor.underStain.AddNewUnderStainInteractor
import com.picone.core.domain.interactor.underStain.GetAllUnderStainForTaskIdInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UnderStainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllUnderStainForTaskIdInteractor: GetAllUnderStainForTaskIdInteractor,
    private val addNewUnderStainInteractor: AddNewUnderStainInteractor
):ViewModel() {
    

}