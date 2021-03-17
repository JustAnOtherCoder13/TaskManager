package com.picone.core.data.task

import com.picone.core.domain.entity.Task
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDaoImpl: TaskDaoImpl) {

    fun getAllTasks():List<Task>{
        return taskDaoImpl.getAllTasks()
    }

    fun getTaskForId(id:Int): Task {
        return taskDaoImpl.getTaskForId(id)
    }

    fun getAllTasksForCategoryId(categoryId:Int):List<Task>{
        return taskDaoImpl.getAllTasksForCategoryId(categoryId)
    }

    suspend fun addNewTask(task: Task){
        taskDaoImpl.addNewTask(task)
    }
}