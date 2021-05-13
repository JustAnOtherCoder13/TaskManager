package com.picone.appcompose.ui.component.screen

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.picone.appcompose.ui.component.baseComponent.AppBar
import com.picone.appcompose.ui.component.baseComponent.BottomNavBar
import com.picone.appcompose.ui.navigation.MainDestinations


@Composable
fun Project(navController: NavController){
    var selectedItemState by remember {
        mutableStateOf(MainDestinations.PROJECT)
    }
    Scaffold (
        topBar = { AppBar(navController = navController )},
        content = {Text(text = "work well")},
        bottomBar = { BottomNavBar(navController = navController,selectedItemState){
            selectedItemState=it
        } }
            )

}