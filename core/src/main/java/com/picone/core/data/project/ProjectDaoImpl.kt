package com.picone.core.data.project

import com.picone.core.data.TaskDatabase
import com.picone.core.domain.entity.Project
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProjectDaoImpl @Inject constructor(taskDatabase: TaskDatabase) {
    private val projectDao = taskDatabase.projectDao()

    fun getAllProjects():Flow<List<Project>>{
        return projectDao.getAllProjects()
    }

    suspend fun addNewProject(project: Project){
        projectDao.addNewProject(project)
    }

    suspend fun deleteProject(project: Project){
        projectDao.deleteProject(project)
    }

    suspend fun updateProject(project : Project){
        projectDao.updateProject(project)
    }
}