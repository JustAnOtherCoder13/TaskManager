package com.picone.core.data.task

import androidx.room.*
import com.picone.core.domain.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT*FROM task_table")
    fun getAllTasks(): Flow<List<Task>>

    @Insert
    suspend fun addNewTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

}