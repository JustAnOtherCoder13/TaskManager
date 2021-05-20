package com.picone.appcompose.ui.component.screen.home.homeProject

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.picone.appcompose.ui.component.screen.home.HomeScreen
import com.picone.appcompose.ui.component.screen.home.ProjectRecyclerView
import com.picone.appcompose.ui.component.screen.home.TaskRecyclerView
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task

@Composable
fun HomeProjectScreen(
    allProjects: List<Project>,
    topAppBarAddItemButtonPopUpItems: List<String>,
    topAppBarAddItemButtonIsPopUpMenuExpanded: Boolean,
    topAppBarAddItemButtonOnAddItemSelected: (itemTypeToAdd: String) -> Unit,
    topAppBarAddItemButtonOnAddButtonClick: () -> Unit,
    topAppBarAddItemButtonOnClosePopUp: () -> Unit,
    bottomNavBarSelectedNavItem: String,
    bottomNavBarOnNavItemSelected: (item: String) -> Unit,
    currentRoute : String?
){

    HomeScreen(
        mainContent = { ProjectRecyclerView(allProjects = allProjects) },
        topAppBarAddItemButtonPopUpItems  ,
        topAppBarAddItemButtonIsPopUpMenuExpanded  ,
        topAppBarAddItemButtonOnAddItemSelected ,
        topAppBarAddItemButtonOnAddButtonClick ,
        topAppBarAddItemButtonOnClosePopUp ,
        bottomNavBarSelectedNavItem,
        bottomNavBarOnNavItemSelected,
        currentRoute
    )
}