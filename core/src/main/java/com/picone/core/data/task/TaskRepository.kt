package com.picone.core.data.task

import com.picone.core.domain.entity.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDaoImpl: TaskDaoImpl) {

    fun getAllTasks(): Flow<List<Task>>{
        return taskDaoImpl.getAllTasks()
    }

    suspend fun addNewTask(task: Task) {
        taskDaoImpl.addNewTask(task)
    }

    suspend fun updateTask(task: Task){
        taskDaoImpl.updateTask(task)
    }
    suspend fun deleteTask(task: Task){
        taskDaoImpl.deleteTask(task)
    }

}