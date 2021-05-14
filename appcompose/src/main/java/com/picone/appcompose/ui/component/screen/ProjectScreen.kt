package com.picone.appcompose.ui.component.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.picone.appcompose.ui.component.baseComponent.AppBar
import com.picone.appcompose.ui.component.baseComponent.BottomNavBar
import com.picone.appcompose.ui.component.baseComponent.ExpandableItem
import com.picone.appcompose.ui.navigation.MainDestinations
import com.picone.core.domain.entity.Project


@Composable
fun Project(allProjects : List<Project>,navController: NavController) {
    var selectedItemState by remember {
        mutableStateOf(MainDestinations.PROJECT)
    }
    Scaffold(
        topBar = { AppBar(navController = navController) },
        content = { ProjectRecyclerView(allProjects, navController) },
        bottomBar = {
            BottomNavBar(navController = navController, selectedItemState) {
                selectedItemState = it
            }
        }
    )
}

@Composable
fun ProjectRecyclerView(
    allProjects: List<Project>,
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
            items(items = allProjects) { project ->
                ExpandableItem(project, navController = navController){}
            }
        }
    }
}