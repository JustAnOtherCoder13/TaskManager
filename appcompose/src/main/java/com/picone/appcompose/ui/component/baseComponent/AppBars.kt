package com.picone.appcompose.ui.component.baseComponent

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.picone.appcompose.R
import com.picone.appcompose.ui.component.manager.navAction.NavigationDirections

@Composable
fun TaskManagerTopAppBar(
    TopAppBarAddItemButtonPopUpItems: List<String>,
    TopAppBarAddItemButtonIsPopUpMenuExpanded: Boolean,
    TopAppBarAddItemButtonOnAddItemSelected: (itemTypeToAdd: String) -> Unit,
    TopAppBarAddItemButtonOnAddButtonClick: () -> Unit,
    TopAppBarAddItemButtonOnClosePopUp: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            TopAppBarAddItemButton(
                TopAppBarAddItemButtonPopUpItems,
                TopAppBarAddItemButtonIsPopUpMenuExpanded,
                TopAppBarAddItemButtonOnAddItemSelected,
                TopAppBarAddItemButtonOnAddButtonClick,
                TopAppBarAddItemButtonOnClosePopUp
            )
        }
    )
}

@Composable
private fun TopAppBarAddItemButton(
    topAppBarAddItemButtonPopUpItems: List<String>,
    topAppBarAddItemButtonIsPopUpMenuExpanded: Boolean,
    topAppBarAddItemButtonOnAddItemSelected: (itemTypeToAdd: String) -> Unit,
    topAppBarAddItemButtonOnAddButtonClick: () -> Unit,
    topAppBarAddItemButtonOnClosePopUp: () -> Unit
) {
    Button(
        onClick = { topAppBarAddItemButtonOnAddButtonClick() },
        shape = CircleShape,
        elevation = null,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.AddCircle,
                contentDescription = null,
            )
            BaseDropDownMenu(
                topAppBarAddItemButtonIsPopUpMenuExpanded,
                topAppBarAddItemButtonPopUpItems,
                onClosePopUp = { popUpItem ->
                    topAppBarAddItemButtonOnClosePopUp()
                    if (popUpItem.trim().isNotEmpty()) {
                        topAppBarAddItemButtonOnAddItemSelected(popUpItem)
                    }
                },
            )
        }
    }
}


@Composable
fun BottomNavBar(
    bottomNavBarSelectedNavItem: String,
    bottomNavBarOnNavItemSelected: (item: String) -> Unit,
    currentRoute : String
) {
    BottomAppBar(modifier = Modifier.fillMaxWidth()) {
        BottomNavigation {
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, "") },
                label = { Text(text = "HOME") },
                selected = currentRoute == NavigationDirections.Home.destination,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                onClick = {
                    Log.i("TAG", "BottomNavBar: $currentRoute")
                    bottomNavBarOnNavItemSelected(NavigationDirections.Home.destination)
                })
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Build, "") },
                label = { Text(text = "PROJECT") },
                selected = currentRoute == NavigationDirections.Project.destination,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                onClick = {
                    Log.i("TAG", "BottomNavBar project: $bottomNavBarSelectedNavItem")
                    bottomNavBarOnNavItemSelected(NavigationDirections.Project.destination)
                })
        }
    }
}