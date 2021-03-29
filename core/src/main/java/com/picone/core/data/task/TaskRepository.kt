package com.picone.core.data.task

import com.picone.core.domain.entity.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDaoImpl: TaskDaoImpl) {

    fun getAllTasks(): Flow<List<Task>> {
        return taskDaoImpl.getAllTasks()
    }

    fun getTaskForId(id: Int): Flow<Task> {
        return taskDaoImpl.getTaskForId(id)
    }

    fun getAllTasksForCategoryId(categoryId: Int): Flow<List<Task>> {
        return taskDaoImpl.getAllTasksForCategoryId(categoryId)
    }

    suspend fun addNewTask(task: Task) {
        taskDaoImpl.addNewTask(task)
    }
}