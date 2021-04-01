package com.picone.core.data.task

import androidx.room.*
import com.picone.core.domain.entity.CompleteTask
import com.picone.core.domain.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Transaction
    @Query("SELECT*FROM task_table")
    fun getAllTasks(): Flow<List<CompleteTask>>

    @Query("SELECT*FROM task_table WHERE task_table.id= :id")
    suspend fun getTaskForId(id: Int): Task

    @Query("SELECT*FROM task_table WHERE task_table.categoryId= :categoryId")
    fun getAllTasksForCategoryId(categoryId: Int): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewTask(task: Task)

}