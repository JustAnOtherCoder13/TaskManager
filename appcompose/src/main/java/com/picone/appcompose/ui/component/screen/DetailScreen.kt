package com.picone.appcompose.ui.component.screen

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
import com.picone.appcompose.ui.component.baseComponent.*
import com.picone.appcompose.ui.values.TopLeftCornerCut
import com.picone.appcompose.ui.values.TopRightCornerCut
import com.picone.appcompose.ui.values.TopRoundedCorner
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetailScreen(
    task: Task,
    allUnderStainsForTask: List<UnderStain>,
    onAddUnderStainEvent: (nameState: String, descriptionState: String) -> Unit,
) {
    var showAddUnderStainItem by remember {
        mutableStateOf(false)
    }
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(TopRoundedCorner)
            .background(MaterialTheme.colors.secondary)
    ) {
        item { DetailHeader(task, allUnderStainsForTask) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        if (!showAddUnderStainItem) {
            items(items = allUnderStainsForTask) { underStain ->
                BaseExpandableItem(
                    underStain.description,
                ) {
                    BaseExpandableItemTitle(
                        itemName = underStain.name,
                        optionIcon = {  },
                        onTaskSelected = {  }
                    )
                }
            }
            item {
                Button(
                    onClick = { showAddUnderStainItem = !showAddUnderStainItem },
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                ) {
                    Text(text = "Add Under Stain")
                }
            }
        }
        if (showAddUnderStainItem) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    AddUnderStainItem(
                        onAddUnderStainEvent = { name, description ->
                            onAddUnderStainEvent(
                                name,
                                description
                            )
                        },
                    ) {
                        showAddUnderStainItem = it
                    }
                }
            }
        }
    }
}

@Composable
private fun AddUnderStainItem(
    onAddUnderStainEvent: (nameState: String, descriptionState: String) -> Unit,
    showAddUnderStainItem: (isShown: Boolean) -> Unit,
) {

    var nameState by remember { mutableStateOf("") }
    var descriptionState by remember { mutableStateOf("") }
    var showDatePickerState by remember { mutableStateOf(false) }
    var selectedDateState by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 10.dp)
            .fillMaxHeight()
    ) {
        DatePickerClickableIcon(date = selectedDateState) {
            showDatePickerState = !showDatePickerState
        }
        BaseEditText(
            title = "Name",
            textColor = MaterialTheme.colors.onSurface,
            text = null
        ) {
            nameState = it
        }
        BaseEditText(
            title = "Description",
            textColor = MaterialTheme.colors.onSurface,
            text = null
        ) {
            descriptionState = it
        }
        AddUnderStainButtons(
            onAddUnderStainEvent = { onAddUnderStainEvent(nameState, descriptionState) },
            isOkButtonEnabled = nameState.trim().isNotEmpty() && descriptionState.trim()
                .isNotEmpty(),
            showAddUnderStainItem
        )
    }
}

@Composable
private fun AddUnderStainButtons(
    onAddUnderStainEvent: () -> Unit,
    isOkButtonEnabled: Boolean,
    showAddUnderStainItem: (isShown: Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { onAddUnderStainEvent() },
            enabled = isOkButtonEnabled
        )
        {
            Text(text = "OK")
        }
        Button(onClick = { showAddUnderStainItem(false) }) {
            Text(text = "Cancel")
        }
    }
}

@Composable
private fun DetailHeader(task: Task, allUnderStainsForTask: List<UnderStain>) {
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
            TaskInformation(task)
        }
        Row(
            modifier = Modifier
                .weight(3f)
        ) {
            UnderStainInformation(allUnderStainsForTask)
        }
    }
}

@Composable
private fun UnderStainInformation(allUnderStainsForTask: List<UnderStain>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .clip(TopLeftCornerCut)
            .background(MaterialTheme.colors.surface)
    ) {
        TitleInformationText(text = "Under Stain ")
        Spacer(modifier = Modifier.height(10.dp))
        InformationText(text = "Total = " + allUnderStainsForTask.size)
        InformationText(text = "Done = " + getCompletedUnderStainsForTask(allUnderStainsForTask).size)
    }
}

@Composable
private fun TaskInformation(task: Task) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .clip(TopRightCornerCut)
            .background(MaterialTheme.colors.surface)

    ) {
        TitleInformationText(text = task.name)
        Spacer(modifier = Modifier.height(10.dp))
        InformationText(
            text = "Create on : " + SimpleDateFormat(
                "dd/MM/yyy",
                Locale.FRANCE
            ).format(task.creation)
        )
        InformationText(
            text = "Deadline on : " +
                    if (task.deadLine == null) "none"
                    else SimpleDateFormat("dd/MM/yyy", Locale.FRANCE)
                        .format(task.deadLine!!)
        )
    }
}

@Composable
private fun getCompletedUnderStainsForTask(allUnderStainsForTask: List<UnderStain>) =
    allUnderStainsForTask.filter { underStain ->
        underStain.close != null
    }