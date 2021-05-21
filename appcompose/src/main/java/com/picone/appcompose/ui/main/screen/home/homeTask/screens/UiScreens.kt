package com.picone.appcompose.ui.main.screen.home.homeTask.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import com.picone.appcompose.ui.utils.interfaces.Event
import com.picone.appcompose.ui.utils.interfaces.UiComponent
import com.picone.appcompose.ui.utils.interfaces.UiScreen
import com.picone.appcompose.ui.utils.interfaces.UiState

object UiScreens {

    val HomeTaskScreen = object : UiScreen {

        @Composable
        override fun events(): List<Event> {
            val list = mutableListOf<Event>()
            list.addAll(UiComponents.homeHeader().events)
            return list
        }

        @Composable
        override fun states(): List<UiState> {
            val list = mutableListOf<UiState>()
            list.addAll(UiComponents.homeHeader().states)
            return list
        }

        @Composable
        override fun CreateScreen(component: @Composable () -> Unit) {
            Column() {
                component()
            }

        }
    }


}