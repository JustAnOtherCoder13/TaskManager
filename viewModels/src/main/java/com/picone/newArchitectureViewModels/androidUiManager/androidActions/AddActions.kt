package com.picone.newArchitectureViewModels.androidUiManager.androidActions

import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.newArchitectureViewModels.androidUiManager.AddAction
import com.picone.newArchitectureViewModels.androidUiManager.AddNavAction
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavActionManager


object AddActions  {

    data class OnDatePickerIconClickedOnDateSelected(val selectedDate: String) : AddAction
    data class OnAddCreated(val selectedTask : Task?,val selectedProject : Project?) : AddAction
    data class OnAddScreenImportanceSelected(val importance: String) : AddAction
    data class OnAddScreenCategorySelected(val category: String) : AddAction
    data class AddScreenOnNameChange(val name: String) : AddAction
    data class AddScreenOnDescriptionChange(val description: String) : AddAction
    data class AddScreenAddNewItemOnOkButtonClicked(val selectedItemType : String, val editedTask : Task?, val editedProject : Project?) : AddAction
    data class NavigateToDetailOnAddTaskComplete(override val androidNavActionManager: AndroidNavActionManager):
        AddNavAction
    data class NavigateToProjectOnAddProjectComplete(override val androidNavActionManager: AndroidNavActionManager):
        AddNavAction
    data class NavigateToHomeOnUpdateTaskComplete(override val androidNavActionManager: AndroidNavActionManager) : AddNavAction
    data class DeleteProjectOnProjectPassInTaskComplete(val project: Project) : AddAction
}