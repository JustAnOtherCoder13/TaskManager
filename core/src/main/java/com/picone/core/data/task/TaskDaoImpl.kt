package com.picone.core.data.task

import com.picone.core.data.TaskDatabase
import com.picone.core.domain.entity.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskDaoImpl @Inject constructor(taskDatabase: TaskDatabase) {

    private val taskDao = taskDatabase.taskDao()

    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

    fun getTaskForId(id: Int): Flow<Task> {
        return taskDao.getTaskForId(id)
    }

    fun getAllTasksForCategoryId(categoryId: Int): Flow<List<Task>> {
        return taskDao.getAllTasksForCategoryId(categoryId)
    }

    suspend fun addNewTask(task: Task) {
        taskDao.addNewTask(task)
    }
}