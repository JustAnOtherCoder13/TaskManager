package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.picone.core.domain.interactor.project.AddNewProjectInteractor
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.project.GetProjectForIdInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllProjectInteractor: GetAllProjectInteractor,
    private val getProjectForIdInteractor: GetProjectForIdInteractor,
    private val addNewProjectInteractor: AddNewProjectInteractor
):ViewModel(){


}