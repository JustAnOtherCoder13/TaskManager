package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.picone.core.domain.entity.Project
import com.picone.core.domain.interactor.project.AddNewProjectInteractor
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.project.GetProjectForIdInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    getAllProjectInteractor: GetAllProjectInteractor,
    private val getProjectForIdInteractor: GetProjectForIdInteractor,
    private val addNewProjectInteractor: AddNewProjectInteractor
) : BaseViewModel() {

    val allProjects: LiveData<List<Project>> =
        getAllProjectInteractor.getAllProjects().asLiveData()

    fun projectForId(id: Int): LiveData<Project> =
        getProjectForIdInteractor.getProjectForId(id).asLiveData()

    fun addNewProject(project: Project) =
        ioScope.launch {
            addNewProjectInteractor.addNewProject(project)
        }

}