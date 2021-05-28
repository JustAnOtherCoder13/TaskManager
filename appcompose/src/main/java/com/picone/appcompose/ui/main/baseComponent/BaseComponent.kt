package com.picone.appcompose.ui.main.baseComponent

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picone.appcompose.ui.values.TopRightCornerCut


@Composable
fun BaseExpandableItem(
    itemDescription: String,
    itemHeader: @Composable () -> Unit
) {
    var expandedState: Boolean by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .padding(5.dp)
            .clip(TopRightCornerCut)
            .background(MaterialTheme.colors.surface)
    ) {
        itemHeader()
        if (expandedState) {
            BaseDescriptionText(itemDescription)
        }
        ExpandIcon(expandedState) { expandedState = !expandedState }
    }
}


@Composable
fun BaseExpandableItemTitle(
    itemName: String,
    optionIcon: @Composable () -> Unit,
    onTaskSelected: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .clickable(onClick = onTaskSelected )
                .weight(7f)
        ) {
            Text(
                text = itemName,
                style = MaterialTheme.typography.subtitle1,
            )
        }
        optionIcon()
    }
}

@Composable
fun <T> BaseRecyclerView(
    items: List<T>,
    tableHeaderView: @Composable (() -> Unit?)?,
    itemView: @Composable (item: T) -> Unit
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

        ) {
            if (tableHeaderView != null) {
                item { tableHeaderView() }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
            items(items) { item -> itemView(item) }
        }
    }
}


/*
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .clickable(onClick = { if (item is Task) onTaskSelected(item) })
                .weight(7f)
        ) {
            Text(
                text = task.name,
                style = MaterialTheme.typography.subtitle1,
            )
            if (item !is Project) {
                SetProgressDrawable(start = task.start, close = task.close)
            }
        }

        var isPopUpMenuExpanded by remember {
            mutableStateOf(false)
        }
        val option: List<String> = when (item) {
            is Project -> listOf("Change to task")
            else -> listOf("start", "close")
        }*/
/*if (item !is Task) {
    Row(modifier = Modifier
        .wrapContentWidth(align = Alignment.End)
        .clickable { isPopUpMenuExpanded = !isPopUpMenuExpanded }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            modifier = Modifier.padding(start = 10.dp)
        )
        AddNewTaskDropDownMenu(
            isPopUpMenuExpanded = isPopUpMenuExpanded,
            addItems = option,
            closePopUp = { selectedItem ->
                isPopUpMenuExpanded = false
                when (item) {
                    is Project -> when (selectedItem) {
                        option[0] -> onTransformProjectInTaskClicked(item)
                    }
                    else -> when (selectedItem) {
                        option[0] -> {
                            item as UnderStain
                            item.start = Calendar.getInstance().time
                            onAddUnderStainButtonClicked(item)
                        }
                        option[1] -> Log.i("TAG", "TaskTitle: close under stain")
                    }
                }
            }
        )
    }
}

}*/


@Composable
private fun ExpandIcon(expanded: Boolean, onCLick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCLick)
    ) {
        Icon(
            imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun BaseDescriptionText(descriptionText: String) {
    Spacer(modifier = Modifier.height(10.dp))
    Row(modifier = Modifier.padding(5.dp)) {
        Text(
            text = descriptionText,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun BaseInformationText(informationText: String) {
    Text(
        text = informationText,
        modifier = Modifier.padding(horizontal = 5.dp),
        style = MaterialTheme.typography.body1,
        textAlign = TextAlign.Justify
    )
}

@Composable
fun BaseTitleInformationText(titleText: String) {
    Text(
        text = titleText,
        modifier = Modifier.padding(horizontal = 10.dp),
        style = MaterialTheme.typography.h2,
    )
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

            if (state_nullableErrorItem != null
                && state_nullablePreselectedItem ?: state_baseSpinnerHint == state_nullableErrorItem
            ) {
                BorderStroke(2.dp, MaterialTheme.colors.error)
            } else BorderStroke(0.dp, Color.Transparent)
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
                .wrapContentHeight()
        ) {
            state_BaseSpinnerItemList.forEachIndexed { _, item ->
                DropdownMenuItem(onClick = {
                    innerStateIsExpanded = false
                    event_onItemSelected(item)
                }) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun BaseDatePickerClickableIcon(
    state_datePickerIconDateText: String,
    event_onDatePickerIconClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .animateContentSize()
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.surface)
            .clickable { event_onDatePickerIconClicked() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Dead line",
            modifier = Modifier.padding(2.dp),
            style = MaterialTheme.typography.subtitle2
        )
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            modifier = Modifier
                .padding(5.dp)
        )
        if (state_datePickerIconDateText.trim().isNotEmpty()) {
            Text(
                text = state_datePickerIconDateText,
                modifier = Modifier.padding(5.dp),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
fun BaseEditText(
    state_title: String,
    state_textColor: Color,
    state_text: String,
    event_baseEditTextOnTextChange: (text: String) -> Unit
) {
    Text(
        text = state_title,
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 5.dp),
        style = MaterialTheme.typography.subtitle2,
        color = state_textColor
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                if (state_title == "Name") PaddingValues(horizontal = 5.dp)
                else PaddingValues(start = 5.dp, end = 5.dp, bottom = 10.dp)
            )
    ) {
        TextField(
            value = state_text,
            onValueChange = { value -> event_baseEditTextOnTextChange(value) },
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .border(setBorderStrokeOnEmpty(state_text))
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun setBorderStrokeOnEmpty(state_text: String) = if (state_text
        .trim()
        .isEmpty()
) BorderStroke(
    2.dp,
    MaterialTheme.colors.error
)
else BorderStroke(0.dp, Color.Transparent)


@Composable
fun <T> PopUpMenuIcon(
    state_menuItems: List<String>,
    state_icon: T,
    event_onMenuItemSelected: (item: String) -> Unit,
) {
    var isPopUpMenuExpanded by remember { mutableStateOf(false) }

    @Composable
    fun <T> iconToPass(state_icon: T, event_OnClick : ()-> Unit)  {
         when (state_icon) {
            is ImageVector -> Icon(
                imageVector = state_icon,
                contentDescription = null,
                modifier = Modifier
                    .clickable { event_OnClick() }
                    .fillMaxHeight()
                    .padding(horizontal = 5.dp)
            )
            is ImageBitmap -> Icon(
                bitmap = state_icon,
                contentDescription = null,
                modifier = Modifier.clickable { event_OnClick() }
            )
            is Painter -> Icon(
                painter = state_icon,
                contentDescription = null,
                modifier = Modifier.clickable { event_OnClick() }
            )
            else -> throw Exception("pass only Painter, ImageVector or ImageBitmap for icon value")
        }
    }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            iconToPass(state_icon = state_icon ,event_OnClick = { isPopUpMenuExpanded = true })
            BaseDropDownMenu(
                isPopUpMenuExpanded,
                state_menuItems = state_menuItems,
                event_onMenuItemSelected = { menuItem ->
                    isPopUpMenuExpanded = false
                    if (menuItem.trim().isNotEmpty()) {
                        event_onMenuItemSelected(menuItem)
                    }
                },
            )
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
            DropdownMenuItem(onClick = { event_onMenuItemSelected(item) }) {
                Text(text = item)
            }
        }
    }
}
