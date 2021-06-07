package com.picone.appcompose.ui.main.screen.home.homeProject

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.picone.appcompose.ui.main.baseComponents.BaseDropDownMenuIcon
import com.picone.appcompose.ui.main.baseComponents.BaseExpandableItem
import com.picone.appcompose.ui.main.baseComponents.BaseExpandableItemTitle
import com.picone.appcompose.ui.main.baseComponents.BaseRecyclerView
import com.picone.appcompose.ui.main.screen.home.HomeScreen
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.util.Constants
import com.picone.newArchitectureViewModels.HomeViewModel
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavActionManager

@Composable
fun HomeProjectScreen(
    homeViewModel: HomeViewModel,
    androidNavActionManager: AndroidNavActionManager,
    state_allProjects: List<Project>,
    state_currentRoute: String?,
    state_allCategories: List<Category>,
    event_projectRecyclerViewOnMenuItemSelected: (selectedItem: String, project: Project) -> Unit,
) {
    HomeScreen(
        state_currentRoute = state_currentRoute,
        homeViewModel = homeViewModel,
        androidNavActionManager = androidNavActionManager,
        mainContent = {
            ProjectRecyclerView(
                state_allProjects = state_allProjects,
                event_projectRecyclerViewOnMenuItemSelected = event_projectRecyclerViewOnMenuItemSelected,
                state_allCategories = state_allCategories
            )
        }
    )
}

@Composable
private fun ProjectRecyclerView(
    state_allProjects: List<Project>,
    state_allCategories: List<Category>,
    event_projectRecyclerViewOnMenuItemSelected: (selectedItem: String, project: Project) -> Unit
) {
    BaseRecyclerView(
        state_itemList = state_allProjects,
        content_header = null,
        content_item = { project ->
            BaseExpandableItem(state_itemDescription = project.description) {
                BaseExpandableItemTitle(
                    state_itemName = project.name,
                    content_optionIcon = {
                        ProjectRecyclerViewItemOptionIcon(
                            state_allCategories = state_allCategories,
                            state_project = project,
                            event_projectRecyclerViewOnMenuItemSelected = event_projectRecyclerViewOnMenuItemSelected
                        )
                    }
                )
            }
        }
    )
}

@Composable
private fun ProjectRecyclerViewItemOptionIcon(
    state_allCategories: List<Category>,
    state_project: Project,
    event_projectRecyclerViewOnMenuItemSelected: (selectedItem: String, project: Project) -> Unit
) {
    Row {
        Text(text = "${state_allCategories.filter { it.id == state_project.categoryId }[Constants.FIRST_ELEMENT].name} : ")
        Icon(
            imageVector = Icons.Default.Label,
            contentDescription = null,
            tint = Color(state_allCategories.filter { it.id == state_project.categoryId }[Constants.FIRST_ELEMENT].color),
        )
    }
    Row {
        BaseDropDownMenuIcon(
            //todo replace with string array
            state_menuItems = listOf(
                Constants.EDIT,
                Constants.DELETE,
                Constants.PASS_TO_TASK
            ),
            state_icon = Icons.Default.MoreVert,
            event_onMenuItemSelected = { selectedItem ->
                event_projectRecyclerViewOnMenuItemSelected(
                    selectedItem,
                    state_project
                )
            }
        )
    }
}