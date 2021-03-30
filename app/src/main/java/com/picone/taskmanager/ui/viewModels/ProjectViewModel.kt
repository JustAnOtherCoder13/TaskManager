package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.interactor.project.AddNewProjectInteractor
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.project.GetProjectForIdInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val getAllProjectInteractor: GetAllProjectInteractor,
    private val getProjectForIdInteractor: GetProjectForIdInteractor,
    private val addNewProjectInteractor: AddNewProjectInteractor
) : BaseViewModel() {

    var allProjectsMutableLD: MutableLiveData<MutableList<Project>> = MutableLiveData()

    fun getAllProject() {
        viewModelScope.launch {
            getAllProjectInteractor.allProjectsFlow
                .collect {
                    allProjectsMutableLD.value = it.toMutableList()
                }
        }
    }

    fun projectForId(id: Int): LiveData<Project> =
        getProjectForIdInteractor.getProjectForId(id).asLiveData()

    fun addNewProject(project: Project) =
        viewModelScope.launch {
            addNewProjectInteractor.addNewProject(project)
        }

}