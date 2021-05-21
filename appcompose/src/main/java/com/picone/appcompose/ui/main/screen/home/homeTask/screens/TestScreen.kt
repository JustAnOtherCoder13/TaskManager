package com.picone.appcompose.ui.main.screen.home.homeTask.screens

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun TestScreen() {
    Scaffold(
        topBar = { },
        content = { UiScreens.HomeTaskScreen.CreateScreen{
            Header()
            Text(text = " events = " + UiComponents.homeHeader().events.size)
        } },
        bottomBar = { }
    )
}

@Composable
fun Header() {
        UiComponents.homeHeader().CreateComponent(
            component = { Text(text = "state" + UiComponents.homeHeader().states[0])}
        )
    }


@Preview
@Composable
fun prev() {
    TestScreen()
}