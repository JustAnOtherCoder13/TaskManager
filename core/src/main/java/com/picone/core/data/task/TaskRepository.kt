package com.picone.core.data.task

import com.picone.core.domain.entity.Task
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDaoImpl: TaskDaoImpl) {

    suspend fun getAllTasks():List<Task>{
        return taskDaoImpl.getAllTasks()
    }

    suspend fun getTaskForId(id:Int): Task {
        return taskDaoImpl.getTaskForId(id)
    }

    suspend fun getAllTasksForCategoryId(categoryId:Int):List<Task>{
        return taskDaoImpl.getAllTasksForCategoryId(categoryId)
    }

    suspend fun addNewTask(task: Task){
        taskDaoImpl.addNewTask(task)
    }
}