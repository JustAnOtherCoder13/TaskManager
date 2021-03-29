package com.picone.core.data.task

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.picone.core.domain.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT*FROM task_table")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT*FROM task_table WHERE task_table.id= :id")
    fun getTaskForId(id: Int): Flow<Task>

    @Query("SELECT*FROM task_table WHERE task_table.categoryId= :categoryId")
    fun getAllTasksForCategoryId(categoryId: Int): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewTask(task: Task)

}