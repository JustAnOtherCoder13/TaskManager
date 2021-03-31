package com.picone.core.domain.interactor.task

import com.picone.core.data.task.TaskRepository
import com.picone.core.domain.entity.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTasksForCategoryIdInteractor @Inject constructor(private val taskRepository: TaskRepository) {

    fun getAllTasksForCategoryId(categoryId: Int): Flow<List<Task>> {
        return taskRepository.getAllTasksForCategoryId(categoryId)
    }
}