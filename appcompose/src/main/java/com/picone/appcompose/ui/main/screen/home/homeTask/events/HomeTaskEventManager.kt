package com.picone.appcompose.ui.main.screen.home.homeTask.events

import androidx.compose.runtime.Composable
import com.picone.appcompose.ui.main.screen.home.homeTask.states.HomeStateManager
import com.picone.appcompose.ui.main.screen.home.homeTask.actions.HomeActions
import com.picone.appcompose.ui.utils.interfaces.Event

class HomeTaskEventManager(val homeStateManager: HomeStateManager) {

    @Composable
    fun TriggerEvent (event : Event){
        when(event){
            HomeEvents.onScreenCreated -> HomeEvents.onScreenCreated.TriggerAction(
                action = HomeActions.updateScreenState,
                homeStateManager = homeStateManager
            )
        }
    }
}