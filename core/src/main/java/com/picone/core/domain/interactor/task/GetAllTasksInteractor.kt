package com.picone.core.domain.interactor.task

import com.picone.core.data.task.TaskRepository
import com.picone.core.domain.entity.Task
import javax.inject.Inject

class GetAllTasksInteractor @Inject constructor(private val taskRepository: TaskRepository) {

    suspend fun getAllTasks():List<Task>{
        return taskRepository.getAllTasks()
    }
}