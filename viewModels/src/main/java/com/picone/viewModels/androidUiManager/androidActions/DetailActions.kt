package com.picone.viewModels.androidUiManager.androidActions

import com.picone.viewModels.androidUiManager.DetailAction
import com.picone.core.domain.entity.Task


object DetailActions : DetailAction {

    data class OnDetailCreated(val task: Task) : DetailAction
    object OnAddUnderStainButtonClick : DetailAction
    object AddUnderStainButtonOnCancelButtonClicked : DetailAction
    data class OnDatePickerIconClickedOnDateSelected(val selectedDate : String): DetailAction
    data class NameEditTextOnTextChange(val underStainName : String) : DetailAction
    data class DescriptionEditTextOnTextChange(val underStainDescription : String): DetailAction
    object AddUnderStainButtonOnOkButtonClicked : DetailAction

}