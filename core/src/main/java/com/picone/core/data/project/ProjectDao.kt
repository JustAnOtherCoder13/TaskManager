package com.picone.core.data.project

import androidx.room.*
import com.picone.core.domain.entity.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT*FROM project_table")
    fun getAllProjects():Flow<List<Project>>

    @Insert
    suspend fun addNewProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    @Update
    suspend fun updateProject(project : Project)
}