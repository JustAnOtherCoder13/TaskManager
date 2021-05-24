package com.picone.appcompose.ui.main.screen.home

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
import com.picone.appcompose.ui.SetProgressDrawable
import com.picone.appcompose.ui.main.baseComponent.*
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task


@Composable
fun HomeScreen(
    state_topBarAddMenuItems: List<String>,
    event_topBarOnMenuItemSelected: (itemTypeToAdd: String) -> Unit,
    event_bottomNavBarOnNavItemSelected: (item: String) -> Unit,
    state_currentRoute : String?,
    mainContent :  @Composable () -> Unit
) {
    Scaffold(
        topBar = { TaskManagerTopAppBar (
            state_topBarAddMenuItems,
            event_topBarOnMenuItemSelected,
                ) },
        content = { mainContent() },
        bottomBar = {
            BottomNavBar(
                event_bottomNavBarOnNavItemSelected = event_bottomNavBarOnNavItemSelected,
                state_currentRoute = state_currentRoute?:""
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
        items = allTasks ,
        tableHeaderView = { TaskImportanceSelector(importance = importance) }
    ) { task ->
        TaskExpandableItem(
            task,
            onTaskSelected = { selectedTask -> taskRecyclerViewOnTaskSelected(selectedTask) }
        )
    }
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
    BaseRecyclerView(
        items = allProjects,
        tableHeaderView = null,
        itemView = { project ->
            BaseExpandableItem(itemDescription = project.description) {
                BaseExpandableItemTitle(
                    itemName = project.name,
                    optionIcon = { },
                    onTaskSelected = { }
                )
            }
        }
    )
}
