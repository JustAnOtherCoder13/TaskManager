package com.picone.newArchitectureViewModels

import com.picone.core.compose.HomeAction
import com.picone.core.compose.HomeNavAction
import com.picone.core.domain.entity.Task
import com.picone.core.domain.navAction.NavActionManager

object HomeActions : HomeAction {
    object OnHomeCreated : HomeAction
    object OnProjectCreated : HomeAction
    data class BottomNavBarOnNavItemSelected(
        override val navActionManager: NavActionManager,
        val selectedNavItem: String
    ) : HomeNavAction

    data class TopBarOnMenuItemSelected(
        override val navActionManager: NavActionManager,
        val selectedItem: String
    ) : HomeNavAction

    data class TaskRecyclerViewOnTaskSelected(
        override val navActionManager: NavActionManager,
        val selectedTask : Task
    ) :HomeNavAction
}