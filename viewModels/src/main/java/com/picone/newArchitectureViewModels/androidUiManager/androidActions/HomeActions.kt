package com.picone.newArchitectureViewModels.androidUiManager.androidActions

import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.newArchitectureViewModels.androidUiManager.HomeAction
import com.picone.newArchitectureViewModels.androidUiManager.HomeNavAction
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavActionManager

object HomeActions : HomeAction {

    object OnHomeCreated : HomeAction
    object OnProjectCreated : HomeAction
    object CloseCategoryPopUp : HomeAction



    data class TaskRecyclerViewOnMenuItemSelected(
        val selectedItem: String,
        val task: Task,
        override val androidNavActionManager: AndroidNavActionManager
        ) : HomeNavAction

    data class AddCategoryOnOkButtonClicked(
        override val androidNavActionManager: AndroidNavActionManager,
    ) : HomeNavAction

    data class AddCategoryOnColorSelected(val color: Long) : HomeAction

    data class AddCategoryOnTextChange(val name: String) : HomeAction


    data class BottomNavBarOnNavItemSelected(
        override val androidNavActionManager: AndroidNavActionManager,
        val selectedNavItem: String
    ) : HomeNavAction

    data class TopBarOnMenuItemSelected(
        override val androidNavActionManager: AndroidNavActionManager,
        val selectedItem: String,
        val selectedTask: Task?
    ) : HomeNavAction

    data class TaskRecyclerViewOnTaskSelected(
        override val androidNavActionManager: AndroidNavActionManager,
        val selectedTask: Task
    ) : HomeNavAction

    data class ProjectRecyclerViewOnMenuItemSelected(
        override val androidNavActionManager: AndroidNavActionManager,
        val selectedItem: String,
        val project: Project
    ) : HomeNavAction

    data class OnFilterItemSelected(val selectedItem: String) : HomeAction
}