package com.picone.appcompose.ui.component.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.picone.appcompose.ui.component.baseComponent.AppBar
import com.picone.appcompose.ui.component.baseComponent.BottomNavBar
import com.picone.appcompose.ui.component.baseComponent.ExpandableItem
import com.picone.appcompose.ui.navigation.MainDestinations.HOME
import com.picone.appcompose.ui.navigation.navigateToAddScreenOnAddItemClicked


@Composable
fun HomeScreen(items: List<CompleteTask>, navController: NavController) {
    var selectedItemState by remember {
        mutableStateOf(HOME)
    }
    Scaffold(
        topBar = { AppBar(navController)},
        content = { TaskRecyclerView(items, "all", navController) },
        bottomBar = { BottomNavBar(navController,selectedItemState){
            selectedItemState=it
        } }
    )
}


@Composable
fun Fab(navController: NavController){
    var isPopUpMenuExpanded by remember {
        mutableStateOf(false)
    }
    val addItems = listOf("Category","Project","Task")
    Button(
        onClick = {isPopUpMenuExpanded =!isPopUpMenuExpanded},
        shape = CircleShape,
        elevation = null,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
        ){
            Icon(
                imageVector = Icons.Rounded.AddCircle,
                contentDescription = null,
            )
            AddNewTaskDropDownMenu(
                isPopUpMenuExpanded,
                addItems,
                closePopUp = {itemType ->
                    isPopUpMenuExpanded=false
                    if(itemType.trim().isNotEmpty())
                    navigateToAddScreenOnAddItemClicked(navController = navController,itemType = itemType, projectId = null)
                             },
            )
        }
    }
}

@Composable
fun AddNewTaskDropDownMenu(
    isPopUpMenuExpanded: Boolean,
    addItems: List<String>,
    closePopUp: (itemType : String) -> Unit
) {
    DropdownMenu(
        expanded = isPopUpMenuExpanded,
        onDismissRequest = { closePopUp("") } ,
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        addItems.forEachIndexed { _, itemType ->
            DropdownMenuItem(onClick = { closePopUp(itemType) }) {
                Text(text = itemType)
            }
        }
    }
}

@Composable
fun TaskRecyclerView(
    allTasks: List<CompleteTask>,
    importance: String,
    navController: NavController
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
            item { TaskImportanceSelector(importance) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(items = allTasks) { task ->
                ExpandableItem(task.task, navController = navController){}
            }
        }
    }
}

@Composable
private fun TaskImportanceSelector(importance: String) {
    //TODO add selector to filter by importance
    Text(
        text = importance,
        style = MaterialTheme.typography.h2,
        color = MaterialTheme.colors.onSurface,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colors.surface)
    )
}
