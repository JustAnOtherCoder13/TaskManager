package com.picone.appcompose.ui.main.baseComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.picone.appcompose.R
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavDirections

@Composable
fun TaskManagerTopAppBar(
    state_topBarAddMenuItems: List<String>,
    event_topBarOnMenuItemSelected: (itemTypeToAdd: String) -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            BaseDropDownMenuIcon(
                state_menuItems = state_topBarAddMenuItems,
                state_icon = Icons.Rounded.AddCircle,
                event_onMenuItemSelected = event_topBarOnMenuItemSelected,
            )
        }
    )
}

@Composable
fun BottomNavBar(
    event_bottomNavBarOnNavItemSelected: (item: String) -> Unit,
    state_currentRoute: String
) {
    BottomAppBar(modifier = Modifier.fillMaxWidth()) {
        BottomNavigation {
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = stringResource(R.string.home_icon_content_description)
                    )
                },
                label = { Text(text = stringResource(R.string.home_icon_text)) },
                selected = state_currentRoute == AndroidNavDirections.Home.destination,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                onClick = { event_bottomNavBarOnNavItemSelected(AndroidNavDirections.Home.destination) })
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = stringResource(R.string.project_icon_content_description)
                    )
                },
                label = { Text(text = stringResource(R.string.project_icon_text)) },
                selected = state_currentRoute == AndroidNavDirections.Project.destination,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                onClick = { event_bottomNavBarOnNavItemSelected(AndroidNavDirections.Project.destination) })
        }
    }
}