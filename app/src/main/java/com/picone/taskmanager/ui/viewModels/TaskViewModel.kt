package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.picone.core.domain.interactor.task.AddNewTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksForCategoryIdInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import com.picone.core.domain.interactor.task.GetTaskForIdInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllTasksInteractor: GetAllTasksInteractor,
    private val getTaskForIdInteractor: GetTaskForIdInteractor,
    private val getAllTasksForCategoryIdInteractor: GetAllTasksForCategoryIdInteractor,
    private val addNewTaskInteractor: AddNewTaskInteractor
    ):ViewModel(){


}