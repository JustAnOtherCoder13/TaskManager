package com.picone.core.data.project

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.picone.core.domain.entity.Project

@Dao
interface ProjectDao {

    @Query("SELECT*FROM project_table")
    suspend fun getAllProjects():List<Project>

    @Query("SELECT*FROM project_table WHERE project_table.id= :projectId")
    suspend fun getProjectForId(projectId:Int):Project

    @Insert
    suspend fun addNewProject(project: Project)
}