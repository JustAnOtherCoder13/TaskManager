package com.picone.core.data.project

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.picone.core.domain.entity.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT*FROM project_table")
    fun getAllProjects():Flow<List<Project>>

    @Query("SELECT*FROM project_table WHERE project_table.id= :projectId")
    fun getProjectForId(projectId:Int):Flow<Project>

    @Insert
    suspend fun addNewProject(project: Project)
}