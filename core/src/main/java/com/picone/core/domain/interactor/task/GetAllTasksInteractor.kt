package com.picone.core.domain.interactor.task

import com.picone.core.data.task.TaskRepository
import com.picone.core.domain.entity.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTasksInteractor @Inject constructor(private val taskRepository: TaskRepository) {

    val allTasksFlow: Flow<List<Task>>
        get() = taskRepository.getAllTasks()

}