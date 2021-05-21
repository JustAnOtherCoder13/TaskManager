package com.picone.appcompose.ui.main.screen.home.homeTask.events

import androidx.compose.runtime.Composable
import com.picone.appcompose.ui.main.screen.home.homeTask.states.HomeStateManager
import com.picone.appcompose.ui.utils.interfaces.Action
import com.picone.appcompose.ui.utils.interfaces.Event

object HomeEvents {

    val onScreenCreated = object : Event {
        @Composable
        override fun TriggerAction(action : Action,homeStateManager: HomeStateManager){
            homeStateManager.updateState(event = HomeTaskEvents.GET_INITIAL_STATE).state
        }
    }

}