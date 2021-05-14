package com.picone.appcompose.ui.component.screen

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.android.material.datepicker.MaterialDatePicker
import com.picone.appcompose.ui.component.baseComponent.BaseEditText
import com.picone.appcompose.ui.component.baseComponent.BaseSpinner
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.util.Constants.FIRST_ELEMENT
import com.picone.viewmodels.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun AddScreen(
    projectToPassInTask: Project?,
    itemType: String,
    requireActivity: AppCompatActivity,
    taskId: Int,
    projectId: Int,
    allCategories: List<Category>,
    addNewProjectOnOkButtonClicked: (project: Project) -> Unit,
    addNewTaskOnOkButtonClicked: (task: Task) -> Unit
) {
    BaseViewModel.completionStateMutableLD.value =
        BaseViewModel.Companion.CompletionState.START_STATE
    val knownCategories: MutableList<String> = mutableListOf()
    val importance = listOf("Unimportant", "Normal", "Important")
    var deadlineState by remember { mutableStateOf("") }
    var importanceState by remember { mutableStateOf("") }
    var categoryState by remember { mutableStateOf("") }
    var nameState by remember { mutableStateOf("") }
    var descriptionState by remember { mutableStateOf("") }
    var isOkButtonEnabledState by remember { mutableStateOf(false) }

    val categoryStr: String? =
        if (projectToPassInTask != null)
            allCategories.filter { category -> category.id == projectToPassInTask.categoryId }[FIRST_ELEMENT].name
        else null

    allCategories.forEachIndexed { _, category -> knownCategories.add(category.name!!) }

    isOkButtonEnabledState = nameState.trim().isNotEmpty() && descriptionState.trim()
        .isNotEmpty() && categoryState.trim().isNotEmpty()

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        item {
            Header(knownCategories, requireActivity, importance, itemType,categoryStr,
                deadLine = { deadlineState = it },
                importance = { importanceState = it },
                category = { categoryState = it })
        }
        item { Spacer(modifier = Modifier.height(10.dp)) }
        item {
            Body(
                projectToPassInTask = projectToPassInTask,
                name = { nameState = it },
                description = { descriptionState = it })
        }

        item {
            OkButton(isOkButtonEnabled = isOkButtonEnabledState) {
                when (itemType) {
                    "Project" ->
                        addNewProjectOnOkButtonClicked(
                            Project(
                                projectId,
                                knownCategories.indexOf(categoryState) + 1,
                                nameState,
                                descriptionState
                            )
                        )
                    "Task" ->
                        addNewTaskOnOkButtonClicked(
                        Task(
                            taskId,
                            knownCategories.indexOf(categoryState) + 1,
                            nameState,
                            descriptionState,
                            importance.indexOf(importanceState) + 1,
                            Calendar.getInstance().time,
                            null,
                            if (deadlineState.trim().isNotEmpty()) SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.FRANCE
                            ).parse(deadlineState) else null,
                            null
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun OkButton(
    isOkButtonEnabled: Boolean,
    addNewItemOnOkButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { addNewItemOnOkButtonClicked() },
            enabled = isOkButtonEnabled
        ) { Text(text = "OK") }
    }
}

@Composable
private fun Body(projectToPassInTask: Project?,name: (String) -> Unit, description: (String) -> Unit) {
    var nameState by remember { mutableStateOf(projectToPassInTask?.name?:"") }
    var descriptionState by remember { mutableStateOf(projectToPassInTask?.description?:"") }

    name(nameState)
    description(descriptionState)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.secondary)
    ) {
        BaseEditText("Name", MaterialTheme.colors.onSecondary,projectToPassInTask?.name) { nameState =  it }
        Spacer(modifier = Modifier.height(10.dp))
        BaseEditText(title = "Description", MaterialTheme.colors.onSecondary,projectToPassInTask?.description) {
            descriptionState = it
        }
    }
}

@Composable
private fun Header(
    knownCategories: List<String>,
    requireActivity: AppCompatActivity,
    importanceList: List<String>,
    itemType: String,
    categoryIfProjectToPass : String?,
    deadLine: (String) -> Unit,
    importance: (String) -> Unit,
    category: (String) -> Unit
) {
    var showDatePickerState by remember { mutableStateOf(false) }
    var selectedDateState by remember { mutableStateOf("") }
    var importanceState by remember { mutableStateOf("") }
    var categoryState by remember { mutableStateOf("") }


    deadLine(selectedDateState)
    importance(importanceState)
    category(categoryState)

    if (showDatePickerState) showDatePicker(
        requireActivity = requireActivity,
        onDismiss = { showDatePickerState = false }) { selectedDate_ ->
        selectedDateState = selectedDate_
    }
    Row(
        modifier = Modifier
            .animateContentSize()
            .padding(start = 5.dp, end = 5.dp, top = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.secondaryVariant, RoundedCornerShape(10.dp)),
        horizontalArrangement = if (itemType != "Project") Arrangement.SpaceBetween else Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BaseSpinner(knownCategories, "Category",categoryIfProjectToPass) { categoryState = it }
        if (itemType != "Project") {
            DatePickerClickableIcon(selectedDateState) { showDatePickerState = !showDatePickerState }
            BaseSpinner(importanceList, "Importance") { importanceState = it }
        }
    }
}

@Composable
fun DatePickerClickableIcon(date: String, showDatePicker: () -> Unit) {
    Column(
        modifier = Modifier
            .animateContentSize()
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.surface)
            .clickable { showDatePicker() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Dead line",
            modifier = Modifier.padding(2.dp),
            style = MaterialTheme.typography.subtitle2
        )
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            modifier = Modifier
                .padding(5.dp)
        )
        if (date.trim().isNotEmpty()) {
            Text(
                text = date,
                modifier = Modifier.padding(5.dp),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

fun showDatePicker(
    requireActivity: AppCompatActivity,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val picker = MaterialDatePicker.Builder.datePicker().build()
    picker.show(requireActivity.supportFragmentManager, picker.toString())

    picker.addOnPositiveButtonClickListener {
        onDateSelected(SimpleDateFormat("dd/MM/yyy", Locale.FRANCE).format(it))
    }
    picker.addOnDismissListener {
        onDismiss()
    }
}