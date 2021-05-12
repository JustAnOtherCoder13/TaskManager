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
import androidx.navigation.NavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.picone.appcompose.ui.component.baseComponent.BaseEditText
import com.picone.appcompose.ui.component.baseComponent.BaseSpinner
import com.picone.appcompose.ui.navigation.navigateToHomeOnAddNewItem
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.viewmodels.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddScreen(
    itemType: String,
    requireActivity: AppCompatActivity,
    navController: NavController,
    taskId: Int,
    projectId: Int,
    allCategories: List<Category>,
    addNewProjectOnOkButtonClicked: (project: Project) -> Unit,
    addNewTaskOnOkButtonClicked: (task: Task) -> Unit
) {
    BaseViewModel.completionStateMutableLD.value = BaseViewModel.Companion.CompletionState.START_STATE
    var knownCategories: MutableList<String> = mutableListOf()
    val importance = listOf("Unimportant", "Normal","Important")
    var deadline by remember { mutableStateOf("") }
    var importance_ by remember { mutableStateOf("") }
    var category_ by remember { mutableStateOf("") }
    var name_ by remember { mutableStateOf("") }
    var description_ by remember { mutableStateOf("") }
    allCategories.forEachIndexed { _, c ->
        knownCategories.add(c.name!!)
    }
    var isOkButtonEnabled by remember {
        mutableStateOf(false)
    }
    isOkButtonEnabled = name_.trim().isNotEmpty()
            && description_.trim().isNotEmpty()
            && category_.trim().isNotEmpty()

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        item {
            Header(knownCategories, requireActivity, importance, itemType,
                deadLine = { deadline = it },
                importance = { importance_ = it },
                category = { category_ = it })
        }
        item { Spacer(modifier = Modifier.height(10.dp)) }
        item {
            Body(
                name = { name_ = it },
                description = { description_ = it })
        }

        item {
            OkButton(isOkButtonEnabled = isOkButtonEnabled) {
                when (itemType) {
                    "Project" ->
                        addNewProjectOnOkButtonClicked(
                            Project(
                                projectId,
                                knownCategories.indexOf(category_) + 1,
                                name_,
                                description_
                            )
                        )
                    "Task" -> addNewTaskOnOkButtonClicked(
                        Task(
                            taskId,
                            knownCategories.indexOf(category_) + 1,
                            name_,
                            description_,
                            importance.indexOf(importance_) + 1,
                            Calendar.getInstance().time,
                            null,
                            if (deadline.trim().isNotEmpty()) SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.FRANCE
                            ).parse(deadline) else null,
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
private fun Body(name: (String) -> Unit, description: (String) -> Unit) {
    var name by remember {
        mutableStateOf("")
    }
    var description_ by remember {
        mutableStateOf("")
    }
    name(name)
    description(description_)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.secondary)

    ) {
        BaseEditText("Name", MaterialTheme.colors.onSecondary) {
            name = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        BaseEditText(title = "Description", MaterialTheme.colors.onSecondary) {
            description_ = it
        }
    }
}

@Composable
private fun Header(
    knownCategories: List<String>,
    requireActivity: AppCompatActivity,
    importanceList: List<String>,
    itemType: String,
    deadLine: (String) -> Unit,
    importance: (String) -> Unit,
    category: (String) -> Unit
) {
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var selectedDate by remember {
        mutableStateOf("")
    }
    var importance_ by remember {
        mutableStateOf("")
    }
    var category_ by remember {
        mutableStateOf("")
    }
    deadLine(selectedDate)
    importance(importance_)
    category(category_)

    if (showDatePicker) showDatePicker(
        requireActivity = requireActivity,
        onDismiss = { showDatePicker = false }) { selectedDate_ ->
        selectedDate = selectedDate_
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
        BaseSpinner(knownCategories, "Category") { category_ = it }
        if (itemType != "Project") {
            DatePickerClickableIcon(selectedDate) { showDatePicker = !showDatePicker }
            BaseSpinner(importanceList, "Importance") { importance_ = it }
        }
    }
}

@Composable
private fun DatePickerClickableIcon(date: String, showDatePicker: () -> Unit) {
    Column(
        modifier = Modifier
            .animateContentSize()
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.surface)
            .clickable {
                showDatePicker()
            },
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


private fun showDatePicker(
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

