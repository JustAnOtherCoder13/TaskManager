package com.picone.core.data.project

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.picone.core.domain.entity.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT*FROM project_table")
    fun getAllProjects():Flow<List<Project>>

    @Query("SELECT*FROM project_table WHERE project_table.id= :projectId")
    suspend fun getProjectForId(projectId:Int):Project

    @Insert
    suspend fun addNewProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)
}