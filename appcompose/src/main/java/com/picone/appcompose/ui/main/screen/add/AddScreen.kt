package com.picone.appcompose.ui.main.screen.add

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picone.appcompose.ui.main.baseComponent.BaseDatePickerClickableIcon
import com.picone.appcompose.ui.main.baseComponent.BaseEditText
import com.picone.appcompose.ui.main.baseComponent.BaseSpinner
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.util.Constants.FIRST_ELEMENT
import com.picone.core.util.Constants.IMPORTANCE_LIST
import com.picone.core.util.Constants.IMPORTANT
import com.picone.core.util.Constants.NORMAL
import com.picone.core.util.Constants.UNIMPORTANT


@Composable
fun AddScreen(
    state_addScreenDeadlineSelectedDate: String,
    state_addScreenIsDatePickerClickableIconVisible:Boolean,
    event_onAddScreenImportanceSelected: (String) -> Unit,
    event_onAddScreenCategorySelected: (String) -> Unit,
    state_addScreenAllCategories: List<Category>,
    state_isOkButtonEnabled : Boolean,
    event_addScreenOnNameChange : (name : String)->Unit,
    event_addScreenOnDescriptionChange : (description : String) -> Unit,
    event_addScreenAddNewItemOnOkButtonClicked : ()->Unit,
    event_onDatePickerIconClicked : () -> Unit,
    state_name : String,
    state_description : String,
    state_category : String?,
    state_importance : String?
) {

    val categoriesToStringList : MutableList<String> = mutableListOf()


    state_addScreenAllCategories.forEachIndexed { _, category -> categoriesToStringList.add(category.name!!) }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        item {
            AddScreenHeader(
                state_addScreenCategoryDropDownMenuItemList = categoriesToStringList,
                state_addScreenIsDatePickerClickableIconVisible = state_addScreenIsDatePickerClickableIconVisible,
                state_addScreenDeadlineSelectedDate = state_addScreenDeadlineSelectedDate,
                event_onAddScreenImportanceSelected = event_onAddScreenImportanceSelected,
                event_onAddScreenCategorySelected = event_onAddScreenCategorySelected,
                event_onDatePickerIconClicked = event_onDatePickerIconClicked,
                state_importance = state_importance,
                state_category = state_category
            )
        }
        item { Spacer(modifier = Modifier.height(10.dp)) }
        item {
            Body(
                event_nameEditTextOnTextChange = event_addScreenOnNameChange,
                event_descriptionEditTextOnTextChange = event_addScreenOnDescriptionChange,
                state_name = state_name,
                state_description = state_description
            )
        }

        item {
            OkButton(
                state_isOkButtonEnabled = state_isOkButtonEnabled,
                event_addNewItemOnOkButtonClicked = event_addScreenAddNewItemOnOkButtonClicked
            )
        }
    }
}

@Composable
private fun OkButton(
    state_isOkButtonEnabled: Boolean,
    event_addNewItemOnOkButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { event_addNewItemOnOkButtonClicked() },
            enabled = state_isOkButtonEnabled
        ) { Text(text = "OK") }
    }
}

@Composable
private fun Body(
    event_nameEditTextOnTextChange: (String) -> Unit,
    event_descriptionEditTextOnTextChange: (String) -> Unit,
    state_name : String,
    state_description : String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.secondary)
    ) {
        BaseEditText(
            state_title = "Name",
            state_textColor = MaterialTheme.colors.onSecondary,
            state_text = state_name,
            event_baseEditTextOnTextChange =  event_nameEditTextOnTextChange)
        Spacer(modifier = Modifier.height(10.dp))
        BaseEditText(
            state_title = "Description",
            state_textColor = MaterialTheme.colors.onSecondary,
            state_text = state_description,
            event_baseEditTextOnTextChange = event_descriptionEditTextOnTextChange
        )
    }
}

@Composable
private fun AddScreenHeader(
    state_addScreenCategoryDropDownMenuItemList: List<String>,
    state_addScreenIsDatePickerClickableIconVisible: Boolean,
    state_addScreenDeadlineSelectedDate: String,
    event_onAddScreenImportanceSelected: (String) -> Unit,
    event_onAddScreenCategorySelected: (String) -> Unit,
    event_onDatePickerIconClicked : () -> Unit,
    state_importance : String?,
    state_category : String?
) {
    Row(
        modifier = Modifier
            .animateContentSize()
            .padding(start = 5.dp, end = 5.dp, top = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.secondaryVariant, RoundedCornerShape(10.dp)),
        horizontalArrangement = if (state_addScreenIsDatePickerClickableIconVisible) Arrangement.SpaceBetween else Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BaseSpinner(
            state_BaseSpinnerItemList = state_addScreenCategoryDropDownMenuItemList,
            state_baseSpinnerHint = "Category",
            state_nullablePreselectedItem =  state_category,
            state_nullableErrorItem = "Category",
            event_onItemSelected = { event_onAddScreenCategorySelected(it)}
        )

        if (state_addScreenIsDatePickerClickableIconVisible) {
            BaseDatePickerClickableIcon(
                state_datePickerIconDateText = state_addScreenDeadlineSelectedDate,
                event_onDatePickerIconClicked = event_onDatePickerIconClicked
            )
            BaseSpinner(
                state_BaseSpinnerItemList = IMPORTANCE_LIST,
                state_baseSpinnerHint = "Importance",
                state_nullablePreselectedItem = state_importance,
                state_nullableErrorItem = null,
                event_onItemSelected = { event_onAddScreenImportanceSelected(it) }
            )
        }
    }
}
