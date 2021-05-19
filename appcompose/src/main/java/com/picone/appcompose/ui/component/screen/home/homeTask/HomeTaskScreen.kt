package com.picone.appcompose.ui.component.screen.home.homeTask

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.picone.appcompose.ui.component.screen.home.HomeScreen
import com.picone.appcompose.ui.component.screen.home.TaskRecyclerView
import com.picone.core.domain.entity.Task

@Composable
fun HomeTaskScreen(
    allTasks: List<Task>,
    taskRecyclerViewOnTaskSelected: (task: Task) -> Unit,
    topAppBarAddItemButtonPopUpItems: List<String>,
    topAppBarAddItemButtonIsPopUpMenuExpanded: Boolean,
    topAppBarAddItemButtonOnAddItemSelected: (itemTypeToAdd: String) -> Unit,
    topAppBarAddItemButtonOnAddButtonClick: () -> Unit,
    topAppBarAddItemButtonOnClosePopUp: () -> Unit,
    bottomNavBarSelectedNavItem: String,
    bottomNavBarOnNavItemSelected: (item: String) -> Unit,
    navController: NavController
){

    HomeScreen(
        mainContent = { TaskRecyclerView(
            allTasks,
            "all",
            taskRecyclerViewOnTaskSelected = { task -> taskRecyclerViewOnTaskSelected(task) },
        ) },
        topAppBarAddItemButtonPopUpItems  ,
        topAppBarAddItemButtonIsPopUpMenuExpanded  ,
        topAppBarAddItemButtonOnAddItemSelected ,
        topAppBarAddItemButtonOnAddButtonClick ,
        topAppBarAddItemButtonOnClosePopUp ,
        bottomNavBarSelectedNavItem,
        bottomNavBarOnNavItemSelected,
        navController
    )
}