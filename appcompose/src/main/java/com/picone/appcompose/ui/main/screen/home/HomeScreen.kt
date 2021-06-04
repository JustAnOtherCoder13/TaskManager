package com.picone.appcompose.ui.main.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.picone.appcompose.R
import com.picone.appcompose.ui.SetProgressDrawable
import com.picone.appcompose.ui.main.baseComponents.*
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.util.Constants.DELETE
import com.picone.core.util.Constants.EDIT
import com.picone.core.util.Constants.FIRST_ELEMENT
import com.picone.core.util.Constants.PASS_TO_TASK


@Composable
fun HomeScreen(
    state_topBarAddMenuItems: List<String>,
    event_topBarOnMenuItemSelected: (itemTypeToAdd: String) -> Unit,
    event_bottomNavBarOnNavItemSelected: (item: String) -> Unit,
    state_currentRoute: String?,
    mainContent: @Composable () -> Unit,
    state_topBarAddCategoryPopUpIsExpanded: Boolean,
    event_topBarAddCategoryPopUpOnDismiss: () -> Unit,
    event_addCategoryPopUpOnTextChange: (text: String) -> Unit,
    event_addCategoryOnOkButtonClicked : () -> Unit,
    event_addCategoryOnColorSelected : (color : Long) -> Unit,

) {
    Scaffold(
        topBar = {
            TaskManagerTopAppBar(
                state_topBarAddMenuItems = state_topBarAddMenuItems,
                event_topBarOnMenuItemSelected = event_topBarOnMenuItemSelected
            )
        },
        content = { mainContent() },
        bottomBar = {
            BottomNavBar(
                event_bottomNavBarOnNavItemSelected = event_bottomNavBarOnNavItemSelected,
                state_currentRoute = state_currentRoute ?: ""
            )
        }
    )
    AddCategoryPopUp(
        state_isExpanded = state_topBarAddCategoryPopUpIsExpanded,
        event_OnDismiss = event_topBarAddCategoryPopUpOnDismiss,
        event_baseEditTextOnTextChange = event_addCategoryPopUpOnTextChange,
        event_addCategoryOnColorSelected = event_addCategoryOnColorSelected,
        event_addCategoryOnOkButtonClicked = event_addCategoryOnOkButtonClicked
        )
}

@Composable
fun TaskRecyclerView(
    state_allTasks: List<Task>,
    event_onFilterItemSelected : (item : String) -> Unit,
    event_taskRecyclerViewOnTaskSelected: (item: Task) -> Unit,
    event_taskRecyclerViewOnMenuItemSelected: (String, task: Task) -> Unit,
    state_allCategories: List<Category>
) {
    BaseRecyclerView(
        state_itemList = state_allTasks,
        content_header = { TaskImportanceSelector(state_allCategories, event_onFilterItemSelected) },
        content_item = { task ->
            TaskExpandableItem(
                task,
                event_onTaskSelected = event_taskRecyclerViewOnTaskSelected,
                event_onMenuItemSelected = event_taskRecyclerViewOnMenuItemSelected,
                state_taskCategoryColor = Color(state_allCategories.filter { it.id == task.categoryId }[FIRST_ELEMENT].color),
                state_taskCategoryName = state_allCategories.filter { it.id == task.categoryId }[FIRST_ELEMENT].name
            )
        }
    )
}

@Composable
fun TaskExpandableItem(
    state_task: Task,
    event_onTaskSelected: (item: Task) -> Unit,
    event_onMenuItemSelected: (String, task: Task) -> Unit,
    state_taskCategoryName: String,
    state_taskCategoryColor: Color
) {
    BaseExpandableItem(
        state_itemDescription = state_task.description,
        content_itemHeader = {
            ExpandableTaskHeader(
                state_task = state_task,
                event_onTaskSelected = event_onTaskSelected,
                event_onMenuItemSelected = event_onMenuItemSelected,
                state_taskCategoryName = state_taskCategoryName,
                state_taskCategoryColor = state_taskCategoryColor
            )
        }
    )
}

@Composable
private fun ExpandableTaskHeader(
    state_task: Task,
    event_onTaskSelected: (item: Task) -> Unit,
    event_onMenuItemSelected: (String, task: Task) -> Unit,
    state_taskCategoryName: String,
    state_taskCategoryColor: Color
) {
    BaseExpandableItemTitle(
        state_itemName = state_task.name,
        content_optionIcon = {
            ExpandableTaskHeaderOptionIcon(
                state_task = state_task,
                event_onMenuItemSelected = { event_onMenuItemSelected(it, state_task) },
                state_taskCategoryColor = state_taskCategoryColor,
                state_taskCategoryName = state_taskCategoryName,

                )
        },
        event_onTaskSelected = { event_onTaskSelected(state_task) }
    )
}

@Composable
private fun ExpandableTaskHeaderOptionIcon(
    state_task: Task,
    event_onMenuItemSelected: (String) -> Unit,
    state_taskCategoryName: String,
    state_taskCategoryColor: Color
) {
    Column() {
        Row() {
            SetProgressDrawable(start = state_task.start, close = state_task.close)
            BaseDropDownMenuIcon(listOf(DELETE, EDIT), Icons.Default.MoreVert, event_onMenuItemSelected)

        }
        Row() {
            Text(text = "$state_taskCategoryName : ")
            Icon(
                Icons.Default.Label,
                contentDescription = null,
                tint = state_taskCategoryColor
            )
        }
    }

}

@Composable
private fun TaskImportanceSelector( allCategories : List<Category>, onFilterItemSelected : (item : String) -> Unit) {

    var innerStateSelectedFilterItem by remember {
        mutableStateOf("All")
    }


    Column(modifier = Modifier.fillMaxWidth()) {
        val firstList = listOf(
            "All",
            "Filter by importance",
            "Filter by category",
        )
        HomeFilterDropDownMenu(
            state_BaseSpinnerItemList = firstList ,
            state_baseSpinnerHint = innerStateSelectedFilterItem,
            event_onItemSelected = { item ->
                innerStateSelectedFilterItem = item
                onFilterItemSelected(item)
                                   },
            state_allCategories = allCategories
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProjectRecyclerView(
    state_allProjects: List<Project>,
    event_projectRecyclerViewOnMenuItemSelected: (selectedItem : String, project : Project) -> Unit,
    state_allCategories: List<Category>
    ) {
    BaseRecyclerView(
        state_itemList = state_allProjects,
        content_header = null,
        content_item = { project ->
            BaseExpandableItem(state_itemDescription = project.description) {
                BaseExpandableItemTitle(
                    state_itemName = project.name,
                    content_optionIcon = {

                        Row() {
                            Text(text = "${state_allCategories.filter { it.id == project.categoryId }[FIRST_ELEMENT].name} : ")
                            Icon(
                                Icons.Default.Label,
                                contentDescription = null,
                                tint = Color(state_allCategories.filter { it.id == project.categoryId }[FIRST_ELEMENT].color),
                            )
                        }
                        Row() {
                            BaseDropDownMenuIcon(
                                state_menuItems = listOf(EDIT, DELETE,PASS_TO_TASK) ,
                                state_icon = Icons.Default.MoreVert,
                                event_onMenuItemSelected = {selectedItem -> event_projectRecyclerViewOnMenuItemSelected(selectedItem, project)}
                            )
                        }

                    }
                )
            }
        }
    )
}

@Composable
fun AddCategoryPopUp(
    state_isExpanded: Boolean,
    event_OnDismiss: () -> Unit,
    event_baseEditTextOnTextChange: (text: String) -> Unit,
    event_addCategoryOnOkButtonClicked: () -> Unit,
    event_addCategoryOnColorSelected: (color: Long) -> Unit
) {

    var innerStateSelectedColor: Long by remember {
        mutableStateOf(0L)
    }
    var innerStateName by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .padding(top = 60.dp, start = 20.dp)
    ) {
        DropdownMenu(
            expanded = state_isExpanded,
            onDismissRequest = event_OnDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BaseTitleInformationText(state_titleText = stringResource(R.string.add_category_pop_up_title))
                Spacer(modifier = Modifier.height(10.dp))
                AddCategoryPopUpBody(
                    event_addCategoryOnColorSelected = {
                        innerStateSelectedColor = it
                        event_addCategoryOnColorSelected(it)
                    },
                    event_baseEditTextOnTextChange = {
                        innerStateName = it
                        event_baseEditTextOnTextChange(it)
                    })
                Spacer(modifier = Modifier.height(5.dp))
                BaseOkAndCancelButtons(
                    event_onOkButtonClicked = event_addCategoryOnOkButtonClicked,
                    event_onCancelButtonClicked = event_OnDismiss,
                    state_isOkButtonEnable = innerStateName.trim()
                        .isNotEmpty() && innerStateSelectedColor != 0L
                )
            }
        }
    }

}

@Composable
private fun AddCategoryPopUpBody(
    event_baseEditTextOnTextChange: (text: String) -> Unit,
    event_addCategoryOnColorSelected: (color: Long) -> Unit
) {
    var innerStateSelectedColor: Long by remember {
        mutableStateOf(0L)
    }
    var innerStateName by remember { mutableStateOf("") }
    event_baseEditTextOnTextChange(innerStateName)
    event_addCategoryOnColorSelected(innerStateSelectedColor)

    BaseEditText(
        state_title = stringResource(R.string.add_category_name_edit_text_title),
        state_textColor = MaterialTheme.colors.onSurface,
        state_text = innerStateName,
        event_baseEditTextOnTextChange = { string -> innerStateName = string }
    )
    Spacer(modifier = Modifier.height(5.dp))
    Row() {
        Text(text = stringResource(R.string.add_category_color_selector_title))
        CategoryColorDropDownMenu(
            event_onMenuItemSelected = { innerStateSelectedColor = it }
        )
    }
}
