package com.picone.appcompose.ui.main.screen.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picone.appcompose.ui.SetProgressDrawable
import com.picone.appcompose.ui.main.baseComponent.*
import com.picone.appcompose.ui.values.TopLeftCornerCut
import com.picone.appcompose.ui.values.TopRightCornerCut
import com.picone.appcompose.ui.values.TopRoundedCorner
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import com.picone.core.util.Constants
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetailScreen(
    state_Task: Task,
    state_allUnderStainsForTask: List<State<UnderStain>>,
    state_isAddUnderStainComponentVisible: Boolean,
    state_datePickerIconDateText: String,
    event_onAddUnderStainButtonClick: () -> Unit,
    event_AddUnderStainButtonOnOkButtonClicked: () -> Unit,
    event_AddUnderStainButtonOnCancelButtonClicked: () -> Unit,
    event_nameEditTextOnTextChange: (text: String) -> Unit,
    event_descriptionEditTextOnTextChange: (text: String) -> Unit,
    event_onDatePickerIconClicked: () -> Unit,
    event_onUnderStainMenuItemSelected : (selectedItem : String, underSatin : UnderStain) ->Unit,
    state_underStainName : String,
    state_underStainDescription : String
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
                BaseExpandableItem(itemDescription = underStain.value.description,) {
                    BaseExpandableItemTitle(
                        itemName = underStain.value.name,
                        optionIcon = {
                            Row() {
                                SetProgressDrawable(start = underStain.value.start, close = underStain.value.close)
                                BasePopUpMenuIcon(
                                    state_menuItems = listOf(
                                        Constants.EDIT,
                                        Constants.DELETE,
                                        if(underStain.value.start == null )Constants.START
                                        else if (underStain.value.close == null && underStain.value.start != null) Constants.CLOSE
                                        else ""
                                    ) ,
                                    state_icon = Icons.Default.MoreVert,
                                    event_onMenuItemSelected = {selectedItem -> event_onUnderStainMenuItemSelected(selectedItem,underStain.value) }
                                )
                            }
                        }
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
                        state_datePickerIconDateText = state_datePickerIconDateText,
                        event_AddUnderStainButtonOnOkButtonClicked = event_AddUnderStainButtonOnOkButtonClicked,
                        event_AddUnderStainButtonOnCancelButtonClicked = event_AddUnderStainButtonOnCancelButtonClicked,
                        event_nameEditTextOnTextChange = event_nameEditTextOnTextChange,
                        event_descriptionEditTextOnTextChange = event_descriptionEditTextOnTextChange,
                        event_onDatePickerIconClicked = event_onDatePickerIconClicked,
                        state_underStainDescription = state_underStainDescription,
                        state_underStainName = state_underStainName
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
    event_onDatePickerIconClicked: () -> Unit,
    state_underStainName : String,
    state_underStainDescription : String
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 10.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BaseDatePickerClickableIcon(
            state_datePickerIconDateText = state_datePickerIconDateText,
            event_onDatePickerIconClicked = event_onDatePickerIconClicked
        )
        BaseEditText(
            state_title = "Name",
            state_textColor = MaterialTheme.colors.onSurface,
            state_text = state_underStainName,
            event_baseEditTextOnTextChange = { text -> event_nameEditTextOnTextChange(text) }
        )
        BaseEditText(
            state_title = "Description",
            state_textColor = MaterialTheme.colors.onSurface,
            state_text = state_underStainDescription,
            event_baseEditTextOnTextChange = { text -> event_descriptionEditTextOnTextChange(text) }
        )
        BaseOkAndCancelButtons(
            event_onOkButtonClicked = event_AddUnderStainButtonOnOkButtonClicked,
            event_onCancelButtonClicked = event_AddUnderStainButtonOnCancelButtonClicked,
            state_isOkButtonEnable = state_underStainName.trim().isNotEmpty() && state_underStainDescription.trim().isNotEmpty(),
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}


@Composable
private fun DetailHeader(
    state_task: Task,
    state_allUnderStainsForTask: List<State<UnderStain>>) {
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
private fun UnderStainInformation(state_allUnderStainsForTask: List<State<UnderStain>>) {
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
private fun getCompletedUnderStainsForTask(allUnderStainsForTask: List<State<UnderStain>>) =
    allUnderStainsForTask.filter { underStain ->
        underStain.value.close != null
    }