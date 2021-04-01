package com.picone.core.data.task

import com.picone.core.domain.entity.CompleteTask
import com.picone.core.domain.entity.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDaoImpl: TaskDaoImpl) {

    fun getAllTasks(): Flow<List<CompleteTask>> {
        return taskDaoImpl.getAllTasks()
    }

    suspend fun getTaskForId(id: Int): Task {
        return taskDaoImpl.getTaskForId(id)
    }

    fun getAllTasksForCategoryId(categoryId: Int): Flow<List<Task>> {
        return taskDaoImpl.getAllTasksForCategoryId(categoryId)
    }

    suspend fun addNewTask(task: Task) {
        taskDaoImpl.addNewTask(task)
    }
}