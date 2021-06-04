package com.picone.appcompose.ui.main.baseComponents

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picone.core.domain.entity.Category
import com.picone.core.util.Constants


@Composable
fun CategoryColorDropDownMenu(
    event_onMenuItemSelected: (color: Long) -> Unit,
) {
    var innerStateIsPopUpMenuExpanded by remember { mutableStateOf(false) }
    var innerStateColorSelectorTint by remember {
        mutableStateOf(Color.Transparent)
    }

    val colorList: List<ColorItem> = categoryColorsList()
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Icon(
            imageVector = Icons.Default.Circle,
            contentDescription = "",
            modifier = Modifier
                .clickable { innerStateIsPopUpMenuExpanded = true }
                .fillMaxHeight()
                .padding(horizontal = 5.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Black, CircleShape),
            tint = innerStateColorSelectorTint
        )
        DropdownMenu(
            expanded = innerStateIsPopUpMenuExpanded,
            onDismissRequest = { innerStateIsPopUpMenuExpanded = false },
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
        ) {
            colorList.forEachIndexed { _, item ->
                DropdownMenuItem(onClick = {
                    innerStateIsPopUpMenuExpanded = false
                    innerStateColorSelectorTint = Color(item.color)
                    event_onMenuItemSelected(item.color)
                }) {
                    BaseInformationText(state_information_text = "${item.name} : ")
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = "",
                        tint = Color(item.color)
                    )
                }
            }
        }
    }
}

@Composable
fun BaseDropDownMenu(
    state_isPopUpMenuExpanded: Boolean,
    state_menuItems: List<String>,
    event_onMenuItemSelected: (itemType: String) -> Unit
) {
    DropdownMenu(
        expanded = state_isPopUpMenuExpanded,
        onDismissRequest = { event_onMenuItemSelected("") },
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        state_menuItems.forEachIndexed { _, item ->
            if (item.trim().isNotEmpty())
                DropdownMenuItem(onClick = { event_onMenuItemSelected(item) }) {
                    Text(text = item)
                }
        }
    }
}

//----------------------------------------------HOME FILTER
@Composable
fun HomeFilterDropDownMenu(
    state_BaseSpinnerItemList: List<String>,
    state_baseSpinnerHint: String,
    event_onItemSelected: (item: String) -> Unit,
    state_allCategories: List<Category>
) {
    var innerStateIsBaseDropDownMenuExpanded by remember {
        mutableStateOf(false) }

    var innerStateIsImportanceMenuExpanded by remember {
        mutableStateOf(false)
    }
    var innerStateIsCategoryMenuExpanded by remember {
        mutableStateOf(false)
    }
    //todo replace with string array importance_list
    val filterByImportanceList = mutableListOf(
        Constants.IMPORTANT,
        Constants.NORMAL,
        Constants.UNIMPORTANT,
    )
    val filterByCategoryList: MutableList<String> = categoriesNameList(state_allCategories)

    Row(modifier = Modifier
        .animateContentSize()
        .clickable {
            innerStateIsBaseDropDownMenuExpanded = !innerStateIsBaseDropDownMenuExpanded
        }
        .padding(5.dp)
        .clip(RoundedCornerShape(5.dp))
        .background(MaterialTheme.colors.surface)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HomeFilterSpinner(
            state_baseSpinnerHint = state_baseSpinnerHint,
            state_isHomeFilterPopUpExpanded = innerStateIsBaseDropDownMenuExpanded
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            HomeFilterBaseDropDownMenu(
                state_BaseSpinnerItemList = state_BaseSpinnerItemList,
                state_isBaseDropDownMenuExpanded = innerStateIsBaseDropDownMenuExpanded,
                event_onItemSelected = event_onItemSelected,
                event_expandBaseDropDownMenu = { isBaseDropDownMenuExpanded ->
                    innerStateIsBaseDropDownMenuExpanded = isBaseDropDownMenuExpanded
                },
                event_onFilterByImportanceItemSelected = { isImportanceDropDownMenuExpanded ->
                    innerStateIsImportanceMenuExpanded = isImportanceDropDownMenuExpanded
                },
                event_onFilterByCategoryItemSelected = { isCategoryDropDownMenuExpanded ->
                    innerStateIsCategoryMenuExpanded = isCategoryDropDownMenuExpanded
                }
            )
            //Importance drop down menu---------------------------------------
            DropdownMenu(
                expanded = innerStateIsImportanceMenuExpanded,
                onDismissRequest = {
                    innerStateIsBaseDropDownMenuExpanded = false
                    innerStateIsImportanceMenuExpanded = false
                },
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                content = {
                    filterByImportanceList.forEachIndexed { _, item ->
                        DropdownMenuItem(onClick = {
                            innerStateIsImportanceMenuExpanded = false
                            innerStateIsBaseDropDownMenuExpanded = false
                            event_onItemSelected(item)
                        }) { Text(text = item) }
                    }
                }
            )
            //Category drop down menu------------------------------------------
            DropdownMenu(
                expanded = innerStateIsCategoryMenuExpanded,
                onDismissRequest = {
                    innerStateIsBaseDropDownMenuExpanded = false
                    innerStateIsCategoryMenuExpanded = false
                },
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                content = {
                    filterByCategoryList.forEachIndexed { _, item ->
                        DropdownMenuItem(onClick = {
                            innerStateIsCategoryMenuExpanded = false
                            innerStateIsBaseDropDownMenuExpanded = false
                            event_onItemSelected(item)
                        }) { Text(text = item) }
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeFilterBaseDropDownMenu(
    state_BaseSpinnerItemList: List<String>,
    state_isBaseDropDownMenuExpanded : Boolean,
    event_onItemSelected: (item: String) -> Unit,
    event_expandBaseDropDownMenu: (isBaseDropDownMenuExpanded: Boolean) -> Unit,
    event_onFilterByImportanceItemSelected: (isImportanceMenuExpanded: Boolean) -> Unit,
    event_onFilterByCategoryItemSelected: (isCategoryDropDownMenuExpanded: Boolean) -> Unit
) {
    var innerStateIsFilterBaseDropDownMenuExpanded by remember {
        mutableStateOf(false)
    }
    innerStateIsFilterBaseDropDownMenuExpanded = state_isBaseDropDownMenuExpanded

    DropdownMenu(
        expanded = innerStateIsFilterBaseDropDownMenuExpanded,
        onDismissRequest = {
            event_expandBaseDropDownMenu(false)
        },
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        content = {
            state_BaseSpinnerItemList.forEachIndexed { _, item ->
                //todo replace with string resources
                DropdownMenuItem(
                    onClick = {
                        when (item) {
                            "All" -> {
                                event_onItemSelected(item)
                                event_expandBaseDropDownMenu(false)
                            }
                            "Filter by importance" -> {
                                event_onFilterByImportanceItemSelected(true)
                            }
                            "Filter by category" -> {
                                event_onFilterByCategoryItemSelected(true)
                            }
                        }
                    },
                    content = { Text(text = item) }
                )
            }
        }
    )
}

@Composable
private fun HomeFilterSpinner(
    state_baseSpinnerHint: String,
    state_isHomeFilterPopUpExpanded: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = state_baseSpinnerHint,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colors.surface)
        )
        Icon(
            imageVector = if (state_isHomeFilterPopUpExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
            contentDescription = null,
        )
    }
}