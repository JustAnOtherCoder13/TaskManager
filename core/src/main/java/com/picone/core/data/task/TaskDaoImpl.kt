package com.picone.core.data.task

import com.picone.core.data.TaskDatabase
import com.picone.core.domain.entity.Task
import javax.inject.Inject

class TaskDaoImpl @Inject constructor(private val taskDatabase: TaskDatabase) {

    private val taskDao = taskDatabase.taskDao()

    fun getAllTasks():List<Task>{
        return taskDao.getAllTasks()
    }

    fun getTaskForId(id:Int):Task{
        return taskDao.getTaskForId(id)
    }

    fun getAllTasksForCategoryId(categoryId:Int):List<Task>{
        return taskDao.getAllTasksForCategoryId(categoryId)
    }

    suspend fun addNewTask(task: Task){
        taskDao.addNewTask(task)
    }
}