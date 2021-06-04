package com.picone.appcompose.ui.main.screen.home.homeTask

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.picone.appcompose.R
import com.picone.appcompose.ui.SetProgressDrawable
import com.picone.appcompose.ui.main.baseComponents.*
import com.picone.appcompose.ui.main.screen.home.HomeScreen
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Task
import com.picone.core.util.Constants
import com.picone.newArchitectureViewModels.HomeViewModel
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavActionManager

@Composable
fun HomeTaskScreen(
    homeViewModel: HomeViewModel,
    androidNavActionManager: AndroidNavActionManager,
    state_allTasks: List<Task>,
    state_currentRoute: String?,
    state_allCategories: List<Category>,
    event_taskRecyclerViewOnTaskSelected: (task: Task) -> Unit,
    event_taskRecyclerViewOnMenuItemSelected: (menuItem: String, task: Task) -> Unit,
    event_onFilterItemSelected: (item: String) -> Unit
) {
    HomeScreen(
        homeViewModel = homeViewModel,
        androidNavActionManager = androidNavActionManager,
        state_currentRoute = state_currentRoute,
        mainContent = {
            TaskRecyclerView(
                state_allTasks = state_allTasks,
                event_onFilterItemSelected = event_onFilterItemSelected,
                event_taskRecyclerViewOnTaskSelected = event_taskRecyclerViewOnTaskSelected,
                event_taskRecyclerViewOnMenuItemSelected = event_taskRecyclerViewOnMenuItemSelected,
                state_allCategories = state_allCategories
            )
        }
    )
}

@Composable
private fun TaskRecyclerView(
    state_allTasks: List<Task>,
    state_allCategories: List<Category>,
    event_onFilterItemSelected : (item : String) -> Unit,
    event_taskRecyclerViewOnTaskSelected: (item: Task) -> Unit,
    event_taskRecyclerViewOnMenuItemSelected: (String, task: Task) -> Unit
) {
    BaseRecyclerView(
        state_itemList = state_allTasks,
        content_header = { TaskImportanceSelector(state_allCategories, event_onFilterItemSelected) },
        content_item = { task ->
            TaskExpandableItem(
                state_task = task,
                state_taskCategoryColor = Color(state_allCategories.filter { it.id == task.categoryId }[Constants.FIRST_ELEMENT].color),
                state_taskCategoryName = state_allCategories.filter { it.id == task.categoryId }[Constants.FIRST_ELEMENT].name,
                event_onTaskSelected = event_taskRecyclerViewOnTaskSelected,
                event_onMenuItemSelected = event_taskRecyclerViewOnMenuItemSelected,
            )
        }
    )
}

@Composable
private fun TaskImportanceSelector(
    state_allCategories : List<Category>,
    event_onFilterItemSelected : (item : String) -> Unit
)
{
    var innerStateSelectedFilterItem by remember {
        mutableStateOf("All")
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        val firstList = listOf(
            stringResource(R.string.filter_all),
            stringResource(R.string.filter_by_importance),
            stringResource(R.string.filter_by_category),
        )
        HomeFilterDropDownMenu(
            state_BaseSpinnerItemList = firstList ,
            state_baseSpinnerHint = innerStateSelectedFilterItem,
            event_onItemSelected = { item ->
                innerStateSelectedFilterItem = item
                event_onFilterItemSelected(item)
            },
            state_allCategories = state_allCategories
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TaskExpandableItem(
    state_task: Task,
    state_taskCategoryName: String,
    state_taskCategoryColor: Color,
    event_onTaskSelected: (item: Task) -> Unit,
    event_onMenuItemSelected: (String, task: Task) -> Unit
) {
    BaseExpandableItem(
        state_itemDescription = state_task.description,
        content_itemHeader = {
            TaskExpandableItemHeader(
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
private fun TaskExpandableItemHeader(
    state_task: Task,
    state_taskCategoryName: String,
    state_taskCategoryColor: Color,
    event_onTaskSelected: (item: Task) -> Unit,
    event_onMenuItemSelected: (String, task: Task) -> Unit
) {
    BaseExpandableItemTitle(
        state_itemName = state_task.name,
        content_optionIcon = {
            TaskExpandableItemHeaderOptionIcon(
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
private fun TaskExpandableItemHeaderOptionIcon(
    state_task: Task,
    state_taskCategoryName: String,
    state_taskCategoryColor: Color,
    event_onMenuItemSelected: (String) -> Unit,
) {
    Column {
        Row {
            SetProgressDrawable(
                start = state_task.start,
                close = state_task.close)
            BaseDropDownMenuIcon(
                state_menuItems = listOf(Constants.DELETE, Constants.EDIT),
                state_icon = Icons.Default.MoreVert,
                event_onMenuItemSelected = event_onMenuItemSelected
            )
        }
        Row {
            Text(text = "$state_taskCategoryName : ")
            Icon(
                imageVector = Icons.Default.Label,
                contentDescription = null,
                tint = state_taskCategoryColor
            )
        }
    }
}