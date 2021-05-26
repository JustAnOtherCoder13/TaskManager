package com.picone.appcompose.ui.main.screen.home.homeProject

import androidx.compose.runtime.Composable
import com.picone.appcompose.ui.main.screen.home.HomeScreen
import com.picone.appcompose.ui.main.screen.home.ProjectRecyclerView
import com.picone.core.domain.entity.Project

@Composable
fun HomeProjectScreen(
    state_allProjects: List<Project>,
    state_topBarAddMenuItems: List<String>,
    event_topBarOnMenuItemSelected: (selectedItem : String) -> Unit,
    event_bottomNavBarOnNavItemSelected: (item: String) -> Unit,
    state_currentRoute : String?
){

    HomeScreen(
        mainContent = { ProjectRecyclerView(allProjects = state_allProjects) },
        state_topBarAddMenuItems = state_topBarAddMenuItems,
        event_topBarOnMenuItemSelected = event_topBarOnMenuItemSelected,
        event_bottomNavBarOnNavItemSelected = event_bottomNavBarOnNavItemSelected,
        state_currentRoute = state_currentRoute
    )
}