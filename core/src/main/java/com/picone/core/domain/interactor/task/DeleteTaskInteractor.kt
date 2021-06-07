package com.picone.core.domain.interactor.task

import com.picone.core.data.task.TaskRepository
import com.picone.core.domain.entity.Task
import javax.inject.Inject

class DeleteTaskInteractor @Inject constructor(private val taskRepository: TaskRepository) {

    suspend fun deleteTask(task: Task){
        taskRepository.deleteTask(task)
    }
}