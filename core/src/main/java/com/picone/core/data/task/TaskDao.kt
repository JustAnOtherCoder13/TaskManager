package com.picone.core.data.task

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.picone.core.domain.entity.Task

@Dao
interface TaskDao {

    @Query("SELECT*FROM task_table")
    fun getAllTasks():List<Task>

    @Query("SELECT*FROM task_table WHERE task_table.id= :id")
    fun getTaskForId(id:Int):Task

    @Query("SELECT*FROM task_table WHERE task_table.categoryId= :categoryId")
    fun getAllTasksForCategoryId(categoryId:Int):List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewTask(task: Task)

}