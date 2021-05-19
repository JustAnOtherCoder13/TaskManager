package com.picone.appcompose.ui.component.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.picone.appcompose.ui.SetProgressDrawable
import com.picone.appcompose.ui.component.baseComponent.*
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task


@Composable
fun HomeScreen(
    mainContent :  @Composable () -> Unit,
    topAppBarAddItemButtonPopUpItems: List<String>,
    topAppBarAddItemButtonIsPopUpMenuExpanded: Boolean,
    topAppBarAddItemButtonOnAddItemSelected: (itemTypeToAdd: String) -> Unit,
    topAppBarAddItemButtonOnAddButtonClick: () -> Unit,
    topAppBarAddItemButtonOnClosePopUp: () -> Unit,
    bottomNavBarSelectedNavItem: String,
    bottomNavBarOnNavItemSelected: (item: String) -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = { TaskManagerTopAppBar (
            topAppBarAddItemButtonPopUpItems,
            topAppBarAddItemButtonIsPopUpMenuExpanded,
            topAppBarAddItemButtonOnAddItemSelected,
            topAppBarAddItemButtonOnAddButtonClick,
            topAppBarAddItemButtonOnClosePopUp
                ) },
        content = { mainContent() },
        bottomBar = {
            BottomNavBar(
                bottomNavBarSelectedNavItem = bottomNavBarSelectedNavItem,
                bottomNavBarOnNavItemSelected = bottomNavBarOnNavItemSelected,
                navController
            )
        }
    )
}

@Composable
fun TaskRecyclerView(
    allTasks: List<Task>,
    importance: String,
    taskRecyclerViewOnTaskSelected: (item: Task) -> Unit,
) {
    BaseRecyclerView(
        items = allTasks,
        tableHeaderView = { TaskImportanceSelector(importance = importance) },
        itemView = {task->
            TaskExpandableItem(
            task,
            onTaskSelected = { selectedTask -> taskRecyclerViewOnTaskSelected(selectedTask) }
        ) }
    )
}

@Composable
fun TaskExpandableItem(
    item: Task,
    onTaskSelected: (item: Task) -> Unit
) {
    BaseExpandableItem(
        itemDescription = item.description,
        itemHeader = {
            ExpandableTaskHeader(
                task = item,
                onTaskSelected = { selectedTask -> onTaskSelected(selectedTask) }
            )
        }
    )
}

@Composable
private fun ExpandableTaskHeader(
    task: Task,
    onTaskSelected: (item: Task) -> Unit,
) {
    BaseExpandableItemTitle(
        itemName = task.name,
        optionIcon = { SetProgressDrawable(start = task.start, close = task.close) },
        onTaskSelected = { taskSelected -> onTaskSelected(task) }
    )
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
    allProjects: List<Project>
) {
    BaseRecyclerView(items = allProjects,
        tableHeaderView = { null },
        itemView = {project ->
            BaseExpandableItem(itemDescription = project.description) {
                BaseExpandableItemTitle(
                    itemName = project.name,
                    optionIcon = { null },
                    onTaskSelected = { null }
                )
            }
        }
    )
}
