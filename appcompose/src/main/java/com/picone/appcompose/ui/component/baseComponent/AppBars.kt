package com.picone.appcompose.ui.component.baseComponent

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
import com.picone.appcompose.ui.component.manager.action.navAction.NavigationDirections
import com.picone.core.util.Constants.CATEGORY
import com.picone.core.util.Constants.PROJECT
import com.picone.core.util.Constants.TASK

@Composable
fun TaskManagerTopAppBar(onAddItemSelected: (itemTypeToAdd: String) -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            TopAppBarAddItemButton { itemTypeToAdd ->
                onAddItemSelected(itemTypeToAdd)
            }
        }
    )
}

@Composable
private fun TopAppBarAddItemButton(onAddItemSelected: (itemTypeToAdd: String) -> Unit) {
    var isPopUpMenuExpanded by remember {
        mutableStateOf(false)
    }
    val addItems = listOf(CATEGORY, PROJECT, TASK)
    Button(
        onClick = { isPopUpMenuExpanded = !isPopUpMenuExpanded },
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
            AddNewTaskDropDownMenu(
                isPopUpMenuExpanded,
                addItems,
                onClosePopUp = { itemType ->
                    isPopUpMenuExpanded = false
                    if (itemType.trim().isNotEmpty()) {
                        onAddItemSelected(itemType)
                    }
                },
            )
        }
    }
}

@Composable
private fun AddNewTaskDropDownMenu(
    isPopUpMenuExpanded: Boolean,
    addItems: List<String>,
    onClosePopUp: (itemType: String) -> Unit
) {
    DropdownMenu(
        expanded = isPopUpMenuExpanded,
        onDismissRequest = { onClosePopUp("") },
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        addItems.forEachIndexed { _, itemType ->
            DropdownMenuItem(onClick = { onClosePopUp(itemType) }) {
                Text(text = itemType)
            }
        }
    }
}

@Composable
fun BottomNavBar(
    selectedItem: String,
    onBottomNavItemSelected: (item: String) -> Unit
) {
    var selectedItemState by remember {
        mutableStateOf(selectedItem)
    }
    BottomAppBar(modifier = Modifier.fillMaxWidth()) {
        BottomNavigation {
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, "") },
                label = { Text(text = "HOME") },
                selected = selectedItemState == NavigationDirections.Home.destination,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                onClick = {
                    onBottomNavItemSelected(NavigationDirections.Home.destination)
                    selectedItemState = NavigationDirections.Home.destination

                })
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Build, "") },
                label = { Text(text = "PROJECT") },
                selected = selectedItemState == NavigationDirections.Project.destination,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                onClick = {
                    onBottomNavItemSelected(NavigationDirections.Project.destination)
                    selectedItemState = NavigationDirections.Project.destination
                })
        }
    }
}