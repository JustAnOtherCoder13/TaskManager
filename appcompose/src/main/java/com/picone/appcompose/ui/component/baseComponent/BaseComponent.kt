package com.picone.appcompose.ui.component.baseComponent

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.picone.appcompose.R
import com.picone.appcompose.ui.SetProgressDrawable
import com.picone.appcompose.ui.component.screen.AddNewTaskDropDownMenu
import com.picone.appcompose.ui.component.screen.Fab
import com.picone.appcompose.ui.navigation.*
import com.picone.appcompose.ui.values.TopRightCornerCut
import com.picone.core.domain.entity.BaseTask
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import com.picone.core.util.Constants.UnknownTask

@Composable
fun AppBar(navController: NavController) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.primary,
        actions = { Fab(navController) }
    )
}

@Composable
fun BottomNavBar(
    navController: NavController,
    selectedItem: String,
    onItemSelected: (item: String) -> Unit
) {
    var selectedItemState by remember {
        mutableStateOf(selectedItem)
    }
    onItemSelected(selectedItemState)
    BottomAppBar(modifier = Modifier.fillMaxWidth()) {
        BottomNavigation {
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, "") },
                label = { Text(text = "HOME") },
                selected = selectedItemState == MainDestinations.HOME,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                onClick = {
                    if (selectedItemState != MainDestinations.HOME) {
                        selectedItemState = MainDestinations.HOME
                        navigateToHome(navController)
                    }
                })
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Build, "") },
                label = { Text(text = "PROJECT") },
                selected = selectedItemState == MainDestinations.PROJECT,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                onClick = {
                    if (selectedItemState != MainDestinations.PROJECT) {
                        selectedItemState = MainDestinations.PROJECT
                        navigateToProject(navController)
                    }
                })
        }
    }
}


@Composable
fun <T> ExpandableItem(item: T, navController: NavController) {
    var expandedState: Boolean by remember { mutableStateOf(false) }
    val itemToShow = when (item) {
        is Task -> item
        is UnderStain -> item
        is Project -> item
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
        if (expandedState) {
            Description(itemToShow)
        }
        ExpandIcon(expandedState) { expandedState = !expandedState }
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
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .clickable(onClick = navigateToDetailOnTaskClicked(navController, item))
                .weight(7f)
        ) {
            Text(
                text = itemToShow.name,
                style = MaterialTheme.typography.subtitle1,
            )
            if (item !is Project) {
                SetProgressDrawable(start = itemToShow.start, close = itemToShow.close)
            }
        }

        var isPopUpMenuExpanded by remember {
            mutableStateOf(false)
        }
        val option : List<String> = when (item ){
            is Project ->  listOf("Change to task")
            else -> listOf("start", "close")
        }
        if (item !is Task) {
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
                                option[0] -> navigateToAddScreenOnAddItemClicked(
                                    navController = navController,
                                    itemType = "Task",
                                    projectId = item.id.toString()
                                )
                            }
                            else -> when (selectedItem) {
                                option[0] -> Log.i("TAG", "TaskTitle: start under stain")
                                option[1] -> Log.i("TAG", "TaskTitle: close under stain")
                            }
                        }
                    }
                )
            }
        }

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
    categoryIfProjectToPass : String? = null,
    onItemSelected: (item: String) -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }
    var selectedItemState by remember { mutableStateOf(categoryIfProjectToPass?:title) }
    onItemSelected(categoryIfProjectToPass?:"")

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
fun BaseEditText(title: String, textColor: Color,text: String?, getText: (text: String) -> Unit) {
    var textState by remember {
        mutableStateOf(TextFieldValue(text?:""))
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
            onValueChange = { value -> textState = value },
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

