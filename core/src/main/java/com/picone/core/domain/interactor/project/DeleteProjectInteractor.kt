package com.picone.core.domain.interactor.project

import com.picone.core.data.project.ProjectRepository
import com.picone.core.domain.entity.Project
import javax.inject.Inject

class DeleteProjectInteractor @Inject constructor(private val projectRepository: ProjectRepository) {

    suspend fun deleteProject(project: Project) {
        projectRepository.deleteProject(project)
    }
}