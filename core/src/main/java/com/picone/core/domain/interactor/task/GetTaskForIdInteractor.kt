package com.picone.core.domain.interactor.task

import com.picone.core.data.task.TaskRepository
import com.picone.core.domain.entity.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskForIdInteractor @Inject constructor(private val taskRepository: TaskRepository) {

    suspend fun getTaskForId(id: Int): Task {
        return taskRepository.getTaskForId(id)
    }
}