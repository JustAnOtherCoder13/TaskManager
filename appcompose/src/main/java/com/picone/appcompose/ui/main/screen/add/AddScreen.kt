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
import com.picone.core.util.Constants.FIRST_ELEMENT
import com.picone.viewmodels.BaseViewModel


@Composable
fun AddScreen(
    state_nullableProjectToPassInTask: Project?,
    /*itemType: String,*/
    state_addScreenDeadlineSelectedDate: String,
    event_onAddScreenImportanceSelected: (String) -> Unit,
    event_onAddScreenCategorySelected: (String) -> Unit,
    event_showDatePicker: (isVisible: Boolean) -> Unit,
    /*taskId: Int,
    projectId: Int,*/
    state_addScreenAllCategories: List<Category>,
    state_isOkButtonEnabled : Boolean,
    event_addScreenOnNameChange : (name : String)->Unit,
    event_addScreenOnDescriptionChange : (description : String) -> Unit,
    event_addScreenAddNewItemOnOkButtonClicked : ()->Unit,
    /*addNewProjectOnOkButtonClicked: (project: Project) -> Unit,
    addNewTaskOnOkButtonClicked: (task: Task) -> Unit*/
) {

    val categoriesToStringList : MutableList<String> = mutableListOf()


    BaseViewModel.completionStateMutableLD.value =
        BaseViewModel.Companion.CompletionState.START_STATE

   /* var categoryState by remember { mutableStateOf("") }
    var nameState by remember { mutableStateOf("") }
    var descriptionState by remember { mutableStateOf("") }*/
    //var isOkButtonEnabledState by remember { mutableStateOf(false) }

    val categoryStr: String? =
        if (state_nullableProjectToPassInTask != null)
            state_addScreenAllCategories.filter { category -> category.id == state_nullableProjectToPassInTask.categoryId }[FIRST_ELEMENT].name
        else null

    state_addScreenAllCategories.forEachIndexed { _, category -> categoriesToStringList.add(category.name!!) }

    /*isOkButtonEnabledState = nameState.trim().isNotEmpty() && descriptionState.trim()
        .isNotEmpty() && categoryState.trim().isNotEmpty()
*/
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        item {
            AddScreenHeader(
                state_addScreenCategoryDropDownMenuItemList = categoriesToStringList,
                state_addScreenIsDatePickerClickableIconVisible = false,
                state_addScreenNullableCategoryPreselectedItem =categoryStr,
                state_addScreenDeadlineSelectedDate = state_addScreenDeadlineSelectedDate,
                event_onAddScreenImportanceSelected = event_onAddScreenImportanceSelected,
                event_onAddScreenCategorySelected = event_onAddScreenCategorySelected,
                event_showDatePicker = event_showDatePicker
            )
        }
        item { Spacer(modifier = Modifier.height(10.dp)) }
        item {
            Body(
                state_nullableProjectToPassInTask = state_nullableProjectToPassInTask,
                event_nameEditTextOnTextChange = event_addScreenOnNameChange,
                event_descriptionEditTextOnTextChange = event_addScreenOnDescriptionChange
            )
        }

        item {
            OkButton(
                state_isOkButtonEnabled = state_isOkButtonEnabled,
                event_addNewItemOnOkButtonClicked = event_addScreenAddNewItemOnOkButtonClicked
            ) /*{
                 val importance = listOf("Unimportant", "Normal", "Important")
                var importanceState by remember { mutableStateOf("") }

                when (itemType) {
                    "Project" ->
                        addNewProjectOnOkButtonClicked(
                            Project(
                                projectId,
                                categoriesToStringList.indexOf(categoryState) + 1,
                                nameState,
                                descriptionState
                            )
                        )
                    "Task" ->
                        addNewTaskOnOkButtonClicked(
                            Task(
                                taskId,
                                categoriesToStringList.indexOf(categoryState) + 1,
                                nameState,
                                descriptionState,
                                importance.indexOf(importanceState) + 1,
                                Calendar.getInstance().time,
                                null,
                                if (state_addScreenDeadlineSelectedDate.trim().isNotEmpty()) SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.FRANCE
                                ).parse(state_addScreenDeadlineSelectedDate) else null,
                                null
                            )
                        )
                }
            }*/
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
    state_nullableProjectToPassInTask: Project?,
    event_nameEditTextOnTextChange: (String) -> Unit,
    event_descriptionEditTextOnTextChange: (String) -> Unit
) {
    var innerStateName by remember { mutableStateOf(state_nullableProjectToPassInTask?.name ?: "") }
    var innerStateDescription by remember {
        mutableStateOf(
            state_nullableProjectToPassInTask?.description ?: ""
        )
    }

    event_nameEditTextOnTextChange(innerStateName)
    event_descriptionEditTextOnTextChange(innerStateDescription)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.secondary)
    ) {
        BaseEditText(
            state_title = "Name",
            state_textColor = MaterialTheme.colors.onSecondary,
            state_text = state_nullableProjectToPassInTask?.name ?: "",
            event_baseEditTextOnTextChange = { innerStateName = it })
        Spacer(modifier = Modifier.height(10.dp))
        BaseEditText(
            state_title = "Description",
            state_textColor = MaterialTheme.colors.onSecondary,
            state_text = state_nullableProjectToPassInTask?.description ?: "",
            event_baseEditTextOnTextChange = { innerStateDescription = it }
        )
    }
}

@Composable
private fun AddScreenHeader(
    state_addScreenCategoryDropDownMenuItemList: List<String>,
    state_addScreenIsDatePickerClickableIconVisible: Boolean,
    state_addScreenNullableCategoryPreselectedItem: String?,
    state_addScreenDeadlineSelectedDate: String,
    event_onAddScreenImportanceSelected: (String) -> Unit,
    event_onAddScreenCategorySelected: (String) -> Unit,
    event_showDatePicker: (isVisible: Boolean) -> Unit
) {
    var innerStateIsDatePickerVisible by remember { mutableStateOf(false) }
    var innerStateImportance by remember { mutableStateOf("") }
    var innerStateCategory by remember { mutableStateOf("") }

    event_showDatePicker(innerStateIsDatePickerVisible)
    event_onAddScreenImportanceSelected(innerStateImportance)
    event_onAddScreenCategorySelected(innerStateCategory)

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
            state_nullablePreselectedItem = state_addScreenNullableCategoryPreselectedItem,
            state_nullableErrorItem = "Category",
            event_onItemSelected = { innerStateCategory = it }
        )

        if (state_addScreenIsDatePickerClickableIconVisible) {
            BaseDatePickerClickableIcon(
                state_datePickerIconDateText = state_addScreenDeadlineSelectedDate,
                event_onDatePickerIconClicked = {
                    innerStateIsDatePickerVisible = !innerStateIsDatePickerVisible
                }
            )
            BaseSpinner(
                state_BaseSpinnerItemList = listOf("Unimportant", "Normal", "Important"),//todo pass with external list
                state_baseSpinnerHint = "Importance",
                state_nullablePreselectedItem = null,
                state_nullableErrorItem = null,
                event_onItemSelected = { innerStateImportance = it }
            )
        }
    }
}


/*
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
}*/
