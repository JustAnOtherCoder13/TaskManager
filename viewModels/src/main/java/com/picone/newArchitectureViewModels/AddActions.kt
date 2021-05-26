package com.picone.newArchitectureViewModels

import com.picone.core.compose.AddAction
import com.picone.core.compose.AddNavAction
import com.picone.core.domain.navAction.NavActionManager

object AddActions : AddAction {

    data class OnDatePickerIconClickedOnDateSelected(val selectedDate: String) : AddAction
    object OnAddCreated : AddAction
    data class OnAddScreenImportanceSelected(val importance: String) : AddAction
    data class OnAddScreenCategorySelected(val category: String) : AddAction
    data class AddScreenOnNameChange(val name: String) : AddAction
    data class AddScreenOnDescriptionChange(val description: String) : AddAction
    data class AddScreenAddNewItemOnOkButtonClicked(val navActionManager: NavActionManager) : AddAction
    data class NavigateToHomeOnAddTaskComplete(override val navActionManager: NavActionManager): AddNavAction

}