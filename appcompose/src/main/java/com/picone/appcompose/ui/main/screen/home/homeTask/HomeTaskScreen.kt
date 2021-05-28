package com.picone.appcompose.ui.main.screen.home.homeTask

import androidx.compose.runtime.Composable
import com.picone.appcompose.ui.main.screen.home.HomeScreen
import com.picone.appcompose.ui.main.screen.home.TaskRecyclerView
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Task

@Composable
fun HomeTaskScreen(
    state_allTasks: List<Task>,
    state_topBarAddMenuItems: List<String>,
    state_currentRoute: String?,
    event_taskRecyclerViewOnTaskSelected: (task: Task) -> Unit,
    event_topBarOnMenuItemSelected: (itemTypeToAdd: String) -> Unit,
    event_bottomNavBarOnNavItemSelected: (item: String) -> Unit,
    event_taskRecyclerViewOnMenuItemSelected:(menuItem : String, task :Task)-> Unit,
    state_allCategories : List<Category>,
    state_topBarAddCategoryPopUpIsExpanded: Boolean,
    event_topBarAddCategoryPopUpOnDismiss: () -> Unit,
    event_addCategoryOnTextChange: (text: String) -> Unit,
    event_addCategoryOnOkButtonClicked : () -> Unit,
    event_addCategoryOnColorSelected : (color : Long) -> Unit

) {
    HomeScreen(
        state_topBarAddMenuItems = state_topBarAddMenuItems,
        state_currentRoute = state_currentRoute,
        event_topBarOnMenuItemSelected = event_topBarOnMenuItemSelected,
        event_bottomNavBarOnNavItemSelected = event_bottomNavBarOnNavItemSelected,
        state_topBarAddCategoryPopUpIsExpanded = state_topBarAddCategoryPopUpIsExpanded,
        event_topBarAddCategoryPopUpOnDismiss = event_topBarAddCategoryPopUpOnDismiss,
        event_addCategoryPopUpOnTextChange = event_addCategoryOnTextChange,
        mainContent = {
            TaskRecyclerView(
                state_allTasks = state_allTasks,
                state_importance = "all",
                event_taskRecyclerViewOnTaskSelected = event_taskRecyclerViewOnTaskSelected,
                event_taskRecyclerViewOnMenuItemSelected = event_taskRecyclerViewOnMenuItemSelected,
                state_allCategories = state_allCategories
            )
        },
        event_addCategoryOnColorSelected = event_addCategoryOnColorSelected,
        event_addCategoryOnOkButtonClicked = event_addCategoryOnOkButtonClicked
    )
}