package com.picone.appcompose.ui.main.screen.home.homeProject

import androidx.compose.runtime.Composable
import com.picone.appcompose.ui.main.screen.home.HomeScreen
import com.picone.appcompose.ui.main.screen.home.ProjectRecyclerView
import com.picone.core.domain.entity.Project

@Composable
fun HomeProjectScreen(
    state_allProjects: List<Project>,
    state_topBarAddMenuItems: List<String>,
    event_topBarOnMenuItemSelected: (selectedItem: String) -> Unit,
    event_bottomNavBarOnNavItemSelected: (item: String) -> Unit,
    state_currentRoute: String?,
    state_topBarAddCategoryPopUpIsExpanded: Boolean,
    event_topBarAddCategoryPopUpOnDismiss: () -> Unit,
    event_addCategoryOnTextChange: (text: String) -> Unit,
    event_addCategoryOnOkButtonClicked: () -> Unit,
    event_addCategoryOnColorSelected: (color: Long) -> Unit,
    event_projectRecyclerViewOnMenuItemSelected: (selectedItem: String, project : Project) -> Unit
) {

    HomeScreen(
        mainContent = {
            ProjectRecyclerView(
                state_allProjects = state_allProjects,
                event_projectRecyclerViewOnMenuItemSelected = event_projectRecyclerViewOnMenuItemSelected
            )
        },
        state_topBarAddMenuItems = state_topBarAddMenuItems,
        event_topBarOnMenuItemSelected = event_topBarOnMenuItemSelected,
        event_bottomNavBarOnNavItemSelected = event_bottomNavBarOnNavItemSelected,
        state_currentRoute = state_currentRoute,
        state_topBarAddCategoryPopUpIsExpanded = state_topBarAddCategoryPopUpIsExpanded,
        event_topBarAddCategoryPopUpOnDismiss = event_topBarAddCategoryPopUpOnDismiss,
        event_addCategoryPopUpOnTextChange = event_addCategoryOnTextChange,
        event_addCategoryOnColorSelected = event_addCategoryOnColorSelected,
        event_addCategoryOnOkButtonClicked = event_addCategoryOnOkButtonClicked
    )
}