package com.picone.appcompose.ui.main.baseComponents

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picone.appcompose.ui.values.TopRightCornerCut

@Composable
fun BaseExpandableItemTitle(
    state_itemName: String,
    event_onTaskSelected: () -> Unit,
    content_optionIcon: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .clickable(onClick = event_onTaskSelected)
                .weight(7f)
        ) {
            Text(
                text = state_itemName,
                style = MaterialTheme.typography.subtitle1,
            )
        }
        content_optionIcon()
    }
}

@Composable
fun BaseExpandableItemTitle(
    state_itemName: String,
    content_optionIcon: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .weight(7f)
        ) {
            Text(
                text = state_itemName,
                style = MaterialTheme.typography.subtitle1,
            )
        }
        content_optionIcon()
    }
}

@Composable
fun BaseExpandableItem(
    state_itemDescription: String,
    content_itemHeader: @Composable () -> Unit
) {
    var innerStateIsItemExpanded: Boolean by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .padding(5.dp)
            .clip(TopRightCornerCut)
            .background(MaterialTheme.colors.surface),
        content = {
            content_itemHeader()
            if (innerStateIsItemExpanded) {
                BaseDescriptionText(state_itemDescription)
            }
            ExpandIcon(innerStateIsItemExpanded) {
                innerStateIsItemExpanded = !innerStateIsItemExpanded
            }
        }
    )
}

@Composable
fun <T> BaseRecyclerView(
    state_itemList: List<T>,
    content_header: @Composable (() -> Unit?)?,
    content_item: @Composable (item: T) -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.secondary)
            .fillMaxHeight()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 15.dp)
                .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                .border(2.dp, MaterialTheme.colors.onSurface)
                .background(MaterialTheme.colors.secondaryVariant)
                .fillMaxWidth(),
            content = {
                if (content_header != null) {
                    item { content_header() }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
                items(state_itemList) { item -> content_item(item) }
            }
        )
    }
}

@Composable
fun BaseSpinner(
    state_BaseSpinnerItemList: List<String>,
    state_baseSpinnerHint: String,
    state_nullablePreselectedItem: String?,
    state_nullableErrorItem: String?,
    event_onItemSelected: (item: String) -> Unit
) {
    var innerStateIsExpanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier
        .animateContentSize()
        .clickable { innerStateIsExpanded = !innerStateIsExpanded }
        .padding(5.dp)
        .clip(RoundedCornerShape(5.dp))
        .background(MaterialTheme.colors.surface)
        .border(
            setRedBorderStrokeOnSpinnerError(
                state_nullableErrorItem,
                state_nullablePreselectedItem,
                state_baseSpinnerHint
            )
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = state_nullablePreselectedItem ?: state_baseSpinnerHint,
            modifier = Modifier
                .padding(2.dp),
            style = MaterialTheme.typography.subtitle2
        )
        Icon(
            imageVector = if (innerStateIsExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
            contentDescription = null,
        )
        DropdownMenu(
            expanded = innerStateIsExpanded,
            onDismissRequest = { innerStateIsExpanded = false },
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            content = {
                state_BaseSpinnerItemList.forEachIndexed { _, item ->
                    DropdownMenuItem(onClick = {
                        innerStateIsExpanded = false
                        event_onItemSelected(item)
                    },
                        content = { Text(text = item) }
                    )
                }
            }
        )
    }
}

@Composable
fun <T> BaseDropDownMenuIcon(
    state_menuItems: List<String>,
    state_icon: T,
    event_onMenuItemSelected: (item: String) -> Unit,
) {
    var innerStateIsPopUpMenuExpanded by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        IconToPass(
            state_icon = state_icon,
            event_OnClick = { innerStateIsPopUpMenuExpanded = true }
        )
        BaseDropDownMenu(
            state_isPopUpMenuExpanded = innerStateIsPopUpMenuExpanded,
            state_menuItems = state_menuItems,
            event_onMenuItemSelected = { menuItem ->
                innerStateIsPopUpMenuExpanded = false
                if (menuItem.trim().isNotEmpty()) {
                    event_onMenuItemSelected(menuItem)
                }
            },
        )
    }
}