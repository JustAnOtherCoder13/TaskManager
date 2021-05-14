package com.picone.appcompose.ui.component.screen

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.picone.appcompose.ui.component.baseComponent.BaseEditText
import com.picone.appcompose.ui.component.baseComponent.ExpandableItem
import com.picone.appcompose.ui.component.baseComponent.InformationText
import com.picone.appcompose.ui.component.baseComponent.TitleInformationText
import com.picone.appcompose.ui.values.TopLeftCornerCut
import com.picone.appcompose.ui.values.TopRightCornerCut
import com.picone.appcompose.ui.values.TopRoundedCorner
import com.picone.core.domain.entity.CompleteTask
import com.picone.core.domain.entity.UnderStain
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetailScreen(
    task: CompleteTask,
    navController: NavController,
    requireActivity: AppCompatActivity,
    addUnderStainOnOkButtonClicked: (underStain: UnderStain) -> Unit
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
        item { Header(task) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        if(!showAddUnderStainItem) {
            items(items = task.underStainsForTask) { underStain ->
                ExpandableItem(
                    underStain,
                    navController = navController
                ){
                    addUnderStainOnOkButtonClicked(it)
                    if(task.task.start==null) {
                        task.task.start = it.start
                    }
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
        }}
        if (showAddUnderStainItem) {
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)

                ) {
                    AddUnderStainItem(
                        requireActivity,
                        addUnderStainOnOkButtonClicked,
                        task
                    ){
                        showAddUnderStainItem=it
                    }
                }
            }
        }
    }
}

@Composable
private fun AddUnderStainItem(
    requireActivity: AppCompatActivity,
    addUnderStainOnOkButtonClicked: (underStain: UnderStain) -> Unit,
    task: CompleteTask,
    showAddUnderStainItem: (isShown : Boolean)->Unit,
) {

    var nameState by remember { mutableStateOf("") }
    var descriptionState by remember { mutableStateOf("") }
    var showDatePickerState by remember { mutableStateOf(false) }
    var selectedDateState by remember { mutableStateOf("") }

    if (showDatePickerState) showDatePicker(
        requireActivity = requireActivity,
        onDismiss = { showDatePickerState = false }) { selectedDate_ ->
        selectedDateState = selectedDate_
    }

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val underStainToPass = UnderStain(
                        id = task.underStainsForTask.size,
                        taskId = task.task.id,
                        name = nameState,
                        description = descriptionState,
                        deadLine = if (selectedDateState.trim().isNotEmpty())
                            SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(
                                selectedDateState
                            )
                        else null,
                        close = null,
                        start = null
                    )
                    task.underStainsForTask.add(underStainToPass)
                    showAddUnderStainItem(false)
                    addUnderStainOnOkButtonClicked(underStainToPass)
                },
                enabled = nameState.trim().isNotEmpty() && descriptionState.trim().isNotEmpty()
            )
            {
                Text(text = "OK")
            }
            Button(onClick = { showAddUnderStainItem (false )}) {
                Text(text = "Cancel")
            }
        }
    }
}

@Composable
private fun Header(task: CompleteTask) {
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
            UnderStainInformation(task)
        }
    }
}

@Composable
private fun UnderStainInformation(task: CompleteTask) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .clip(TopLeftCornerCut)
            .background(MaterialTheme.colors.surface)
    ) {
        TitleInformationText(text = "Under Stain ")
        Spacer(modifier = Modifier.height(10.dp))
        InformationText(text = "Total = " + task.underStainsForTask.size)
        InformationText(text = "Done = " + getCompletedUnderStainsForTask(task = task).size)
    }
}

@Composable
private fun TaskInformation(task: CompleteTask) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .clip(TopRightCornerCut)
            .background(MaterialTheme.colors.surface)

    ) {
        TitleInformationText(text = task.task.name)
        Spacer(modifier = Modifier.height(10.dp))
        InformationText(
            text = "Create on : " + SimpleDateFormat(
                "dd/MM/yyy",
                Locale.FRANCE
            ).format(task.task.creation)
        )
        InformationText(
            text =
            "Deadline on : " +
                    if (task.task.deadLine == null) "none"
                    else SimpleDateFormat("dd/MM/yyy", Locale.FRANCE)
                        .format(task.task.deadLine!!)
        )
    }
}

@Composable
private fun getCompletedUnderStainsForTask(task: CompleteTask) =
    task.underStainsForTask.filter { underStain ->
        underStain.close != null
    }