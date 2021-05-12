package com.picone.appcompose.ui.component.baseComponent

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.picone.appcompose.ui.SetProgressDrawable
import com.picone.appcompose.ui.navigation.navigateToDetailOnTaskClicked
import com.picone.appcompose.ui.values.TopRightCornerCut
import com.picone.core.domain.entity.BaseTask
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import com.picone.core.util.Constants.UnknownTask


@Composable
fun <T> ExpandableTaskItem(item: T, navController: NavController) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    val itemToShow = when (item) {
        is Task -> item
        is UnderStain -> item
        else -> UnknownTask
    }
    Column(
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .padding(5.dp)
            .clip(TopRightCornerCut)
            .background(MaterialTheme.colors.surface)
    ) {
        TaskTitle(item, itemToShow, navController)
        if (expanded) {
            Description(itemToShow)
        }
        ExpandIcon(expanded) { expanded = !expanded }
    }
}

@Composable
fun <T> TaskTitle(
    item: T,
    itemToShow: BaseTask,
    navController: NavController
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clickable(onClick = navigateToDetailOnTaskClicked(navController, item))
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = itemToShow.name,
            style = MaterialTheme.typography.subtitle1,
        )
        SetProgressDrawable(start = itemToShow.start, close = itemToShow.close)
    }
}

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
private fun Description(itemToShow: BaseTask) {
    Spacer(modifier = Modifier.height(10.dp))
    Row(modifier = Modifier.padding(5.dp)) {
        Text(
            text = itemToShow.description,
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
    onItemSelected: (item: String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedItem by remember {
        mutableStateOf(title)
    }
    Row(modifier =
    Modifier
        .animateContentSize()
        .clickable { expanded = !expanded }
        .padding(5.dp)
        .border(
            if (selectedItem == "Category") {
                BorderStroke(2.dp, MaterialTheme.colors.error)
            } else BorderStroke(0.dp, Color.Transparent)
        )
        .clip(RoundedCornerShape(5.dp))
        .background(MaterialTheme.colors.surface),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = selectedItem,
            modifier = Modifier
                .padding(2.dp),
            style = MaterialTheme.typography.subtitle2
        )
        Icon(
            imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
            contentDescription = null,
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
        ) {
            itemList.forEachIndexed { _, item ->

                DropdownMenuItem(onClick = {
                    expanded = false
                    onItemSelected(item)
                    selectedItem = item

                }) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun BaseEditText(title: String, textColor: Color, getText: (text: String) -> Unit) {
    var textState by remember {
        mutableStateOf(TextFieldValue())
    }
    getText(textState.text)
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
            value = textState,
            onValueChange = { value ->
                textState = value
            },
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .border(
                    if (textState.text
                            .trim()
                            .isEmpty()
                    ) BorderStroke(
                        2.dp,
                        MaterialTheme.colors.error
                    )
                    else BorderStroke(0.dp, Color.Transparent)
                )
                .fillMaxWidth(),

            )
    }
}

