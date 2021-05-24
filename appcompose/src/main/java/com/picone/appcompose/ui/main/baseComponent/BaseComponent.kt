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
            Description(itemDescription)
        }
        ExpandIcon(expandedState) { expandedState = !expandedState }
    }
}


@Composable
fun BaseExpandableItemTitle(
    itemName: String,
    optionIcon: @Composable () -> Unit,
    onTaskSelected: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .clickable(onClick = { onTaskSelected(true) })
                .weight(7f)
        ) {
            Text(
                text = itemName,
                style = MaterialTheme.typography.subtitle1,
            )
            optionIcon()
        }
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
private fun Description(itemDescription: String) {
    Spacer(modifier = Modifier.height(10.dp))
    Row(modifier = Modifier.padding(5.dp)) {
        Text(
            text = itemDescription,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun InformationText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 5.dp),
        style = MaterialTheme.typography.body1,
        textAlign = TextAlign.Justify
    )
}

@Composable
fun TitleInformationText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 10.dp),
        style = MaterialTheme.typography.h2,
    )
}

@Composable
fun BaseSpinner(
    itemList: List<String>,
    title: String,
    categoryIfProjectToPass: String? = null,
    onItemSelected: (item: String) -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }
    var selectedItemState by remember { mutableStateOf(categoryIfProjectToPass ?: title) }
    onItemSelected(categoryIfProjectToPass ?: "")

    Row(modifier = Modifier
        .animateContentSize()
        .clickable { expandedState = !expandedState }
        .padding(5.dp)
        .border(
            if (selectedItemState == "Category") {
                BorderStroke(2.dp, MaterialTheme.colors.error)
            } else BorderStroke(0.dp, Color.Transparent)
        )
        .clip(RoundedCornerShape(5.dp))
        .background(MaterialTheme.colors.surface),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = selectedItemState,
            modifier = Modifier
                .padding(2.dp),
            style = MaterialTheme.typography.subtitle2
        )
        Icon(
            imageVector = if (expandedState) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
            contentDescription = null,
        )
        DropdownMenu(
            expanded = expandedState,
            onDismissRequest = { expandedState = false },
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
        ) {
            itemList.forEachIndexed { _, item ->
                DropdownMenuItem(onClick = {
                    expandedState = false
                    onItemSelected(item)
                    selectedItemState = item
                }) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun BaseEditText(
    title: String,
    textColor: Color,
    state_text: String,
    event_baseEditTextOnTextChange: (text: String) -> Unit
) {
    Text(
        text = title,
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 5.dp),
        style = MaterialTheme.typography.subtitle2,
        color = textColor
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                if (title == "Name") PaddingValues(horizontal = 5.dp)
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
fun <T> PopUpMenuButton(
    state_menuItems: List<String>,
    state_icon : T,
    event_onMenuItemSelected: (item: String) -> Unit,
) {
    var isPopUpMenuExpanded by remember { mutableStateOf(false) }

    val iconToPass : @Composable ()->Unit = {
        when (state_icon) {
            is ImageVector -> Icon(
                imageVector = state_icon,
                contentDescription = null,
            )
            is ImageBitmap ->Icon(
                bitmap = state_icon,
                contentDescription = null,
            )
            is Painter -> Icon(
                painter = state_icon,
                contentDescription = null,
            )
            else -> throw Exception("pass only Painter, ImageVector or ImageBitmap for icon value")
        }
    }
    Button(
        onClick = { isPopUpMenuExpanded=true },
        shape = CircleShape,
        elevation = null,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
        ) {
            if (state_icon is ImageVector)
                iconToPass()
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