package com.picone.core.data.task

import com.picone.core.data.TaskDatabase
import com.picone.core.domain.entity.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskDaoImpl @Inject constructor(taskDatabase: TaskDatabase) {

    private val taskDao = taskDatabase.taskDao()

    fun getAllTasks(): Flow<List<Task>>{
        return taskDao.getAllTasks()
    }

    suspend fun addNewTask(task: Task) {
        taskDao.addNewTask(task)
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }
}