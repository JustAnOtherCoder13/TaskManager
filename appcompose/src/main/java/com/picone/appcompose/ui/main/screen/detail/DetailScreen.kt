package com.picone.appcompose.ui.main.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picone.appcompose.ui.main.baseComponent.*
import com.picone.appcompose.ui.main.screen.add.BaseDatePickerClickableIcon
import com.picone.appcompose.ui.values.TopLeftCornerCut
import com.picone.appcompose.ui.values.TopRightCornerCut
import com.picone.appcompose.ui.values.TopRoundedCorner
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetailScreen(
    state_Task: Task,
    state_allUnderStainsForTask: List<UnderStain>,
    state_isAddUnderStainComponentVisible: Boolean,
    state_addUnderStainItemName: String,
    event_onAddUnderStainButtonClick: () -> Unit,
    event_AddUnderStainButtonOnOkButtonClicked: () -> Unit,
    event_AddUnderStainButtonOnCancelButtonClicked: () -> Unit,
    event_nameEditTextOnTextChange: (text: String) -> Unit,
    event_descriptionEditTextOnTextChange: (text: String) -> Unit,
    event_onDatePickerIconClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(TopRoundedCorner)
            .background(MaterialTheme.colors.secondary)
    ) {
        item { DetailHeader(state_Task, state_allUnderStainsForTask) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        if (!state_isAddUnderStainComponentVisible) {
            items(items = state_allUnderStainsForTask) { underStain ->
                BaseExpandableItem(
                    underStain.description,
                ) {
                    BaseExpandableItemTitle(
                        itemName = underStain.name,
                        optionIcon = { },
                        onTaskSelected = { }
                    )
                }
            }
            item {
                Button(
                    onClick = { event_onAddUnderStainButtonClick() },
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                ) {
                    Text(text = "Add Under Stain")
                }
            }
        }
        if (state_isAddUnderStainComponentVisible) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    AddUnderStainItem(
                        state_addUnderStainItemName,
                        event_AddUnderStainButtonOnOkButtonClicked,
                        event_AddUnderStainButtonOnCancelButtonClicked,
                        event_nameEditTextOnTextChange,
                        event_descriptionEditTextOnTextChange,
                        event_onDatePickerIconClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun AddUnderStainItem(
    state_datePickerIconDateText: String,
    event_AddUnderStainButtonOnOkButtonClicked: () -> Unit,
    event_AddUnderStainButtonOnCancelButtonClicked: () -> Unit,
    event_nameEditTextOnTextChange: (text: String) -> Unit,
    event_descriptionEditTextOnTextChange: (text: String) -> Unit,
    event_onDatePickerIconClicked: () -> Unit
) {
    var innerStateName by remember { mutableStateOf( "") }
    var innerStateDescription by remember { mutableStateOf( "") }

    event_nameEditTextOnTextChange(innerStateName)
    event_descriptionEditTextOnTextChange(innerStateDescription)

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 10.dp)
            .fillMaxHeight()
    ) {
        BaseDatePickerClickableIcon(
            state_datePickerIconDateText = state_datePickerIconDateText,
            event_onDatePickerIconClicked = event_onDatePickerIconClicked
        )
        BaseEditText(
            state_title = "Name",
            state_textColor = MaterialTheme.colors.onSurface,
            state_text = innerStateName,
            event_baseEditTextOnTextChange = { text -> innerStateName = text }
        )
        BaseEditText(
            state_title = "Description",
            state_textColor = MaterialTheme.colors.onSurface,
            state_text = innerStateDescription,
            event_baseEditTextOnTextChange = { text -> innerStateDescription = text }
        )
        AddUnderStainButtons(
            event_AddUnderStainButtonOnOkButtonClicked = event_AddUnderStainButtonOnOkButtonClicked,
            event_AddUnderStainButtonOnCancelButtonClicked = event_AddUnderStainButtonOnCancelButtonClicked,
            state_isOkButtonEnable = innerStateName.trim().isNotEmpty() && innerStateDescription.trim().isNotEmpty(),
        )
    }
}

@Composable
private fun AddUnderStainButtons(
    state_isOkButtonEnable: Boolean,
    event_AddUnderStainButtonOnOkButtonClicked: () -> Unit,
    event_AddUnderStainButtonOnCancelButtonClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        //positive button
        Button(
            onClick = { event_AddUnderStainButtonOnOkButtonClicked() },
            enabled = state_isOkButtonEnable
        ) { Text(text = "OK") }

        //cancel button
        Button(onClick = { event_AddUnderStainButtonOnCancelButtonClicked() }
        ) { Text(text = "Cancel") }
    }
}

@Composable
private fun DetailHeader(
    state_task: Task,
    state_allUnderStainsForTask: List<UnderStain>) {
    Row(
        modifier = Modifier
            .clip(TopRoundedCorner)
            .background(MaterialTheme.colors.primaryVariant)
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(4f)
        ) {
            TaskInformation(state_task)
        }
        Row(
            modifier = Modifier
                .weight(3f)
        ) {
            UnderStainInformation(state_allUnderStainsForTask)
        }
    }
}

@Composable
private fun UnderStainInformation(state_allUnderStainsForTask: List<UnderStain>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .clip(TopLeftCornerCut)
            .background(MaterialTheme.colors.surface)
    ) {
        BaseTitleInformationText(titleText = "Under Stain ")
        Spacer(modifier = Modifier.height(10.dp))
        BaseInformationText(informationText = "Total = " + state_allUnderStainsForTask.size)
        BaseInformationText(informationText = "Done = " + getCompletedUnderStainsForTask(state_allUnderStainsForTask).size)
    }
}

@Composable
private fun TaskInformation(state_task: Task) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .clip(TopRightCornerCut)
            .background(MaterialTheme.colors.surface)

    ) {
        BaseTitleInformationText(titleText = state_task.name)
        Spacer(modifier = Modifier.height(10.dp))
        BaseInformationText(
            informationText = "Create on : " + SimpleDateFormat(
                "dd/MM/yyy",
                Locale.FRANCE
            ).format(state_task.creation)
        )
        BaseInformationText(
            informationText = "Deadline on : " +
                    if (state_task.deadLine == null) "none"
                    else SimpleDateFormat("dd/MM/yyy", Locale.FRANCE)
                        .format(state_task.deadLine!!)
        )
    }
}

@Composable
private fun getCompletedUnderStainsForTask(allUnderStainsForTask: List<UnderStain>) =
    allUnderStainsForTask.filter { underStain ->
        underStain.close != null
    }