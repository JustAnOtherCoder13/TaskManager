package com.picone.core.data.project

import com.picone.core.data.TaskDatabase
import com.picone.core.domain.entity.Project
import javax.inject.Inject

class ProjectDaoImpl @Inject constructor(taskDatabase: TaskDatabase) {
    private val projectDao = taskDatabase.projectDao()

    suspend fun getAllProjects():List<Project>{
        return projectDao.getAllProjects()
    }

    suspend fun getProjectForId(projectId:Int):Project{
        return projectDao.getProjectForId(projectId)
    }

    suspend fun addNewProject(project: Project){
        projectDao.addNewProject(project)
    }


}