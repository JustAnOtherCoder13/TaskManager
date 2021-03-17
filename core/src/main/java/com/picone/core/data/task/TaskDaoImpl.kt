package com.picone.core.data.task

import com.picone.core.data.TaskDatabase
import com.picone.core.domain.entity.Task
import javax.inject.Inject

class TaskDaoImpl @Inject constructor(taskDatabase: TaskDatabase) {

    private val taskDao = taskDatabase.taskDao()

    suspend fun getAllTasks():List<Task>{
        return taskDao.getAllTasks()
    }

    suspend fun getTaskForId(id:Int):Task{
        return taskDao.getTaskForId(id)
    }

    suspend fun getAllTasksForCategoryId(categoryId:Int):List<Task>{
        return taskDao.getAllTasksForCategoryId(categoryId)
    }

    suspend fun addNewTask(task: Task){
        taskDao.addNewTask(task)
    }
}