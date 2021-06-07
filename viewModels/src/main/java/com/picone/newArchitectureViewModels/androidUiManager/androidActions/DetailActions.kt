package com.picone.newArchitectureViewModels.androidUiManager.androidActions

import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import com.picone.newArchitectureViewModels.androidUiManager.DetailAction

object DetailActions : DetailAction {

    data class OnDetailCreated(val task: Task) : DetailAction
    object OnAddUnderStainButtonClick : DetailAction
    object AddUnderStainButtonOnCancelButtonClicked : DetailAction
    data class OnDatePickerIconClickedOnDateSelected(val selectedDate: String) : DetailAction
    data class NameEditTextOnTextChange(val underStainName: String) : DetailAction
    data class DescriptionEditTextOnTextChange(val underStainDescription: String) : DetailAction
    object AddUnderStainButtonOnOkButtonClicked : DetailAction
    data class OnUnderStainMenuItemSelected(
        val selectedItem: String,
        val underStain: UnderStain,
        val selectedTask: Task
    ) : DetailAction
}