package com.picone.newArchitectureViewModels

import com.picone.core.compose.DetailAction
import com.picone.core.domain.entity.Task


object DetailActions : DetailAction {

    data class OnDetailCreated(val task: Task) : DetailAction
    object OnAddUnderStainButtonClick : DetailAction
    object AddUnderStainButtonOnCancelButtonClicked : DetailAction
    data class OnDatePickerIconClickedOnDateSelected(val selectedDate : String):DetailAction
    data class NameEditTextOnTextChange(val underStainName : String) :DetailAction
    data class DescriptionEditTextOnTextChange(val underStainDescription : String): DetailAction
    object AddUnderStainButtonOnOkButtonClicked : DetailAction

}