package com.picone.appcompose.ui.main.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.picone.appcompose.R
import com.picone.appcompose.ui.main.baseComponents.*
import com.picone.core.util.Constants
import com.picone.newArchitectureViewModels.HomeViewModel
import com.picone.newArchitectureViewModels.androidUiManager.androidActions.HomeActions
import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavActionManager

@Composable
fun HomeScreen(
    state_currentRoute: String?,
    homeViewModel: HomeViewModel,
    androidNavActionManager: AndroidNavActionManager,
    mainContent: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TaskManagerTopAppBar(
                state_topBarAddMenuItems = topBarrAddMenuItems(),
                event_topBarOnMenuItemSelected = { selectedAddItem ->
                    homeViewModel.dispatchEvent(
                        HomeActions.TopBarOnMenuItemSelected(
                            androidNavActionManager = androidNavActionManager,
                            selectedItem = selectedAddItem,
                            selectedTask = null
                        )
                    )
                }
            )
        },
        content = { mainContent() },
        bottomBar = {
            BottomNavBar(
                event_bottomNavBarOnNavItemSelected = { selectedNavItem ->
                    homeViewModel.dispatchEvent(
                        HomeActions.BottomNavBarOnNavItemSelected(
                            androidNavActionManager = androidNavActionManager,
                            selectedNavItem = selectedNavItem
                        )
                    )
                },
                state_currentRoute = state_currentRoute ?: ""
            )
        }
    )
    AddCategoryPopUp(
        state_isExpanded = homeViewModel.mIsAddCategoryPopUpExpandedState.value,
        event_OnDismiss = {
            homeViewModel.dispatchEvent(
                HomeActions.CloseCategoryPopUp
            )
        },
        event_baseEditTextOnTextChange = {
            homeViewModel.dispatchEvent(
                HomeActions.AddCategoryOnTextChange(it)
            )
        },
        event_addCategoryOnColorSelected = {
            homeViewModel.dispatchEvent(
                HomeActions.AddCategoryOnColorSelected(it)
            )
        },
        event_addCategoryOnOkButtonClicked = {
            homeViewModel.dispatchEvent(
                HomeActions.AddCategoryOnOkButtonClicked(
                    androidNavActionManager = androidNavActionManager
                )
            )
        }
    )
}

@Composable
private fun topBarrAddMenuItems() = listOf(Constants.CATEGORY, Constants.PROJECT, Constants.TASK)


@Composable
private fun AddCategoryPopUp(
    state_isExpanded: Boolean,
    event_OnDismiss: () -> Unit,
    event_baseEditTextOnTextChange: (text: String) -> Unit,
    event_addCategoryOnOkButtonClicked: () -> Unit,
    event_addCategoryOnColorSelected: (color: Long) -> Unit
) {

    var innerStateSelectedColor: Long by remember {
        mutableStateOf(0L)
    }
    var innerStateName by remember { mutableStateOf("") }

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
                BaseTitleInformationText(state_titleText = stringResource(R.string.add_category_pop_up_title))
                Spacer(modifier = Modifier.height(10.dp))
                AddCategoryPopUpBody(
                    event_addCategoryOnColorSelected = {
                        innerStateSelectedColor = it
                        event_addCategoryOnColorSelected(it)
                    },
                    event_baseEditTextOnTextChange = {
                        innerStateName = it
                        event_baseEditTextOnTextChange(it)
                    })
                Spacer(modifier = Modifier.height(5.dp))
                BaseOkAndCancelButtons(
                    event_onOkButtonClicked = event_addCategoryOnOkButtonClicked,
                    event_onCancelButtonClicked = event_OnDismiss,
                    state_isOkButtonEnable = innerStateName.trim()
                        .isNotEmpty() && innerStateSelectedColor != 0L
                )
            }
        }
    }

}

@Composable
private fun AddCategoryPopUpBody(
    event_baseEditTextOnTextChange: (text: String) -> Unit,
    event_addCategoryOnColorSelected: (color: Long) -> Unit
) {
    var innerStateSelectedColor: Long by remember {
        mutableStateOf(0L)
    }
    var innerStateName by remember { mutableStateOf("") }
    event_baseEditTextOnTextChange(innerStateName)
    event_addCategoryOnColorSelected(innerStateSelectedColor)

    BaseEditText(
        state_title = stringResource(R.string.add_category_name_edit_text_title),
        state_textColor = MaterialTheme.colors.onSurface,
        state_text = innerStateName,
        event_baseEditTextOnTextChange = { string -> innerStateName = string }
    )
    Spacer(modifier = Modifier.height(5.dp))
    Row() {
        Text(text = stringResource(R.string.add_category_color_selector_title))
        CategoryColorDropDownMenu(
            event_onMenuItemSelected = { innerStateSelectedColor = it }
        )
    }
}