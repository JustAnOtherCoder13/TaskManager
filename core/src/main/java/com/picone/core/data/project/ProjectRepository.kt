package com.picone.core.data.project

import com.picone.core.domain.entity.Project
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val projectDaoImpl: ProjectDaoImpl
){
    fun getAllProjects():List<Project>{
        return projectDaoImpl.getAllProjects()
    }

    fun getProjectForId(projectId:Int):Project{
        return projectDaoImpl.getProjectForId(projectId)
    }

    suspend fun addNewProject(project: Project){
        projectDaoImpl.addNewProject(project)
    }

}