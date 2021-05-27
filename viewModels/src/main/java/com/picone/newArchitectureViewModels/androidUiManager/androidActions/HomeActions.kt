package com.picone.newArchitectureViewModels.androidUiManager.androidActions

import com.picone.newArchitectureViewModels.androidUiManager.HomeAction
import com.picone.newArchitectureViewModels.androidUiManager.HomeNavAction
import com.picone.core.domain.entity.Task
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavActionManager

object HomeActions : HomeAction {

    object OnHomeCreated : HomeAction
    object OnProjectCreated : HomeAction

    data class BottomNavBarOnNavItemSelected(
        override val androidNavActionManager: AndroidNavActionManager,
        val selectedNavItem: String
    ) : HomeNavAction

    data class TopBarOnMenuItemSelected(
        override val androidNavActionManager: AndroidNavActionManager,
        val selectedItem: String
    ) : HomeNavAction

    data class TaskRecyclerViewOnTaskSelected(
        override val androidNavActionManager: AndroidNavActionManager,
        val selectedTask : Task
    ) : HomeNavAction
}