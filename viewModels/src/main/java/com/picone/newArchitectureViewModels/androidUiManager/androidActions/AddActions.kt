package com.picone.newArchitectureViewModels.androidUiManager.androidActions

import com.picone.newArchitectureViewModels.androidUiManager.AddAction
import com.picone.newArchitectureViewModels.androidUiManager.AddNavAction
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavActionManager

object AddActions : AddAction {

    data class OnDatePickerIconClickedOnDateSelected(val selectedDate: String) : AddAction
    object OnAddCreated : AddAction
    data class OnAddScreenImportanceSelected(val importance: String) : AddAction
    data class OnAddScreenCategorySelected(val category: String) : AddAction
    data class AddScreenOnNameChange(val name: String) : AddAction
    data class AddScreenOnDescriptionChange(val description: String) : AddAction
    data class AddScreenAddNewItemOnOkButtonClicked(val selectedItemType : String) : AddAction
    data class NavigateToHomeOnAddTaskComplete(override val androidNavActionManager: AndroidNavActionManager):
        AddNavAction
    data class NavigateToProjectOnAddProjectComplete(override val androidNavActionManager: AndroidNavActionManager):
        AddNavAction

}