package com.picone.appcompose.ui.main.screen.home.homeTask.screens

import androidx.compose.runtime.Composable
import com.picone.appcompose.ui.main.screen.home.HomeScreen
import com.picone.appcompose.ui.main.screen.home.TaskRecyclerView
import com.picone.core.domain.entity.Task

@Composable
fun HomeTaskScreen(
    state_allTasks: List<Task>,
    event_taskRecyclerViewOnTaskSelected: (task: Task) -> Unit,
    state_topBarAddMenuItems: List<String>,
    event_topBarOnMenuItemSelected: (itemTypeToAdd: String) -> Unit,
    event_bottomNavBarOnNavItemSelected: (item: String) -> Unit,
    state_currentRoute: String?
) {
    HomeScreen(
        state_topBarAddMenuItems,
        event_topBarOnMenuItemSelected,
        event_bottomNavBarOnNavItemSelected,
        state_currentRoute
    )
    {
        TaskRecyclerView(
            state_allTasks,
            "all",
            taskRecyclerViewOnTaskSelected = { task -> event_taskRecyclerViewOnTaskSelected(task) },
        )
    }
}