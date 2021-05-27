package com.picone.viewModels.androidUiManager.androidActions

import com.picone.viewModels.androidUiManager.AddAction
import com.picone.viewModels.androidUiManager.AddNavAction
import com.picone.viewModels.androidUiManager.androidNavActions.AndroidNavActionManager

object AddActions : AddAction {

    data class OnDatePickerIconClickedOnDateSelected(val selectedDate: String) : AddAction
    object OnAddCreated : AddAction
    data class OnAddScreenImportanceSelected(val importance: String) : AddAction
    data class OnAddScreenCategorySelected(val category: String) : AddAction
    data class AddScreenOnNameChange(val name: String) : AddAction
    data class AddScreenOnDescriptionChange(val description: String) : AddAction
    object AddScreenAddNewItemOnOkButtonClicked : AddAction
    data class NavigateToHomeOnAddTaskComplete(override val androidNavActionManager: AndroidNavActionManager):
        AddNavAction

}