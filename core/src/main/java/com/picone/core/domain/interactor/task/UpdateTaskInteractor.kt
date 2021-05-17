package com.picone.core.domain.interactor.task

import com.picone.core.data.task.TaskRepository
import com.picone.core.domain.entity.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateTaskInteractor @Inject constructor(private val taskRepository: TaskRepository) {

    suspend fun updateTask(task: Task){
        taskRepository.updateTask(task)
    }
}