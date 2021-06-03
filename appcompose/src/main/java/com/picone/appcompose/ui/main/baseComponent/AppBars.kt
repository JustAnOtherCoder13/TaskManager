package com.picone.appcompose.ui.main.baseComponent

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
            BasePopUpMenuIcon(
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

@Composable
fun AddCategoryPopUp(
    state_isExpanded: Boolean,
    event_OnDismiss: () -> Unit,
    event_baseEditTextOnTextChange: (text: String) -> Unit,
    event_addCategoryOnOkButtonClicked : () -> Unit,
    event_addCategoryOnColorSelected : (color : Long) -> Unit
) {

    var innerStateSelectedColor : Long by remember {
        mutableStateOf(0L)
    }
    var innerStateName by remember { mutableStateOf("") }
    event_baseEditTextOnTextChange(innerStateName)
    event_addCategoryOnColorSelected(innerStateSelectedColor)

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .padding(top = 60.dp, start = 20.dp)
    ) {
        DropdownMenu(
            expanded = state_isExpanded,
            onDismissRequest = event_OnDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BaseTitleInformationText(titleText = "Add new Category")
                Spacer(modifier = Modifier.height(10.dp))
                BaseEditText(
                    state_title = "Name",
                    state_textColor = MaterialTheme.colors.onSurface,
                    state_text = innerStateName,
                    event_baseEditTextOnTextChange = { string -> innerStateName = string }
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row() {
                    Text(text = "Color : ")
                    CategoryColorPopUpMenu(
                        event_onMenuItemSelected = { innerStateSelectedColor = it }
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                BaseOkAndCancelButtons(
                    event_onOkButtonClicked = event_addCategoryOnOkButtonClicked,
                    event_onCancelButtonClicked = event_OnDismiss,
                    state_isOkButtonEnable = innerStateName.trim().isNotEmpty() && innerStateSelectedColor != 0L
                )

            }
        }
    }

}