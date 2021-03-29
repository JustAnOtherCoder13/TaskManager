package com.picone.core.domain.interactor.project

import com.picone.core.data.project.ProjectRepository
import com.picone.core.domain.entity.Project
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProjectInteractor @Inject constructor(private val projectRepository: ProjectRepository) {

    fun getAllProjects(): Flow<List<Project>> {
        return projectRepository.getAllProjects()
    }
}