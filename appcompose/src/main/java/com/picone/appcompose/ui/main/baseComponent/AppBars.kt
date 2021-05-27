package com.picone.appcompose.ui.main.baseComponent

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
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavObjects

@Composable
fun TaskManagerTopAppBar(
    state_topBarAddMenuItems: List<String>,
    event_topBarOnMenuItemSelected: (itemTypeToAdd: String) -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            PopUpMenuButton(
                state_menuItems = state_topBarAddMenuItems,
                state_icon = Icons.Rounded.AddCircle ,
                event_onMenuItemSelected = event_topBarOnMenuItemSelected,
            )
        }
    )
}




@Composable
fun BottomNavBar(
    event_bottomNavBarOnNavItemSelected: (item: String) -> Unit,
    state_currentRoute : String
) {
    BottomAppBar(modifier = Modifier.fillMaxWidth()) {
        BottomNavigation {
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, "") },
                label = { Text(text = "HOME") },
                selected = state_currentRoute == AndroidNavObjects.Home.destination,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                onClick = { event_bottomNavBarOnNavItemSelected(AndroidNavObjects.Home.destination) })
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Build, "") },
                label = { Text(text = "PROJECT") },
                selected = state_currentRoute == AndroidNavObjects.Project.destination,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                onClick = { event_bottomNavBarOnNavItemSelected(AndroidNavObjects.Project.destination) })
        }
    }
}