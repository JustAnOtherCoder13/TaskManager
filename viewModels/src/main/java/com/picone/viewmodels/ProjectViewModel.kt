package com.picone.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.picone.core.domain.entity.Project
import com.picone.core.domain.interactor.project.AddNewProjectInteractor
import com.picone.core.domain.interactor.project.DeleteProjectInteractor
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.project.GetProjectForIdInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val mGetAllProjectInteractor: GetAllProjectInteractor,
    private val mGetProjectForIdInteractor: GetProjectForIdInteractor,
    private val mAddNewProjectInteractor: AddNewProjectInteractor,
    private val mDeleteProjectInteractor: DeleteProjectInteractor
) : BaseViewModel() {

    var mAllProjectsMutableLD: MutableLiveData<MutableList<Project>> = MutableLiveData()
    var mProjectForIdMutableLD: MutableLiveData<Project> = MutableLiveData()

    fun getAllProject() {
        viewModelScope.launch {
            mGetAllProjectInteractor.allProjectsFlow
                .collect {
                    mAllProjectsMutableLD.value = it.toMutableList()
                }
        }
    }

    fun getProjectForId(id: Int) {
        viewModelScope.launch {
            mProjectForIdMutableLD.value = mGetProjectForIdInteractor.getProjectForId(id)
        }
    }


    fun addNewProject(project: Project) =
        try {
            viewModelScope.launch {
                mAddNewProjectInteractor.addNewProject(project)
            }
            completionStateMutableLD.value = Companion.CompletionState.PROJECT_ON_COMPLETE
        } catch (e: Exception) {
            e.printStackTrace()
        }

    fun deleteProject(project: Project) =
        try {
            viewModelScope.launch {
                mDeleteProjectInteractor.deleteProject(project)
            }
            completionStateMutableLD.value = Companion.CompletionState.DELETE_PROJECT_ON_COMPLETE
        } catch (e: Exception) {
            e.printStackTrace()
        }

}