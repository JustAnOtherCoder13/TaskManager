package com.picone.appcompose.ui.main.screen.home.homeTask.screens

import androidx.compose.runtime.Composable
import com.picone.appcompose.ui.main.screen.home.homeTask.events.HomeEvents
import com.picone.appcompose.ui.main.screen.home.homeTask.states.HomeTaskUiStates
import com.picone.appcompose.ui.utils.interfaces.Event
import com.picone.appcompose.ui.utils.interfaces.UiComponent
import com.picone.appcompose.ui.utils.interfaces.UiState

object UiComponents {
    @Composable
    fun homeHeader() = object : UiComponent {
        override val events: List<Event> = listOf(HomeEvents.onScreenCreated)
        override val states: List<UiState> = listOf(HomeTaskUiStates.ALL_TASKS)
    }

}


