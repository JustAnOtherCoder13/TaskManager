package com.picone.appcompose.ui.main.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picone.appcompose.ui.SetProgressDrawable
import com.picone.appcompose.ui.main.baseComponent.*
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
    event_addCategoryOnColorSelected : (color : Long) -> Unit

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
    state_importance: String,
    event_taskRecyclerViewOnTaskSelected: (item: Task) -> Unit,
    event_taskRecyclerViewOnMenuItemSelected: (String, task: Task) -> Unit,
    state_allCategories: List<Category>
) {
    BaseRecyclerView(
        items = state_allTasks,
        tableHeaderView = { TaskImportanceSelector(importance = state_importance) },
        itemView = { task ->
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
        itemDescription = state_task.description,
        itemHeader = {
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
        itemName = state_task.name,
        optionIcon = {
            ExpandableTaskHeaderOptionIcon(
                state_task = state_task,
                event_onMenuItemSelected = { event_onMenuItemSelected(it, state_task) },
                state_taskCategoryColor = state_taskCategoryColor,
                state_taskCategoryName = state_taskCategoryName,

                )
        },
        onTaskSelected = { event_onTaskSelected(state_task) }
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
            BasePopUpMenuIcon(listOf(DELETE, EDIT), Icons.Default.MoreVert, event_onMenuItemSelected)

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
private fun TaskImportanceSelector(importance: String) {
    //TODO add selector to filter by importance
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = importance,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colors.surface)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProjectRecyclerView(
    state_allProjects: List<Project>,
    event_projectRecyclerViewOnMenuItemSelected: (selectedItem : String, project : Project) -> Unit
    ) {
    BaseRecyclerView(
        items = state_allProjects,
        tableHeaderView = null,
        itemView = { project ->
            BaseExpandableItem(itemDescription = project.description) {
                BaseExpandableItemTitle(
                    itemName = project.name,
                    optionIcon = {
                        BasePopUpMenuIcon(
                            state_menuItems = listOf(EDIT, DELETE,PASS_TO_TASK) ,
                            state_icon = Icons.Default.MoreVert,
                            event_onMenuItemSelected = {selectedItem -> event_projectRecyclerViewOnMenuItemSelected(selectedItem, project)}
                            )}
                )
            }
        }
    )
}
