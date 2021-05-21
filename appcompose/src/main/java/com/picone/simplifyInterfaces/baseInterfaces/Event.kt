package com.picone.simplifyInterfaces.baseInterfaces

import androidx.compose.runtime.Composable
import com.picone.appcompose.ui.main.screen.home.homeTask.states.HomeStateManager

interface Event {
    @Composable
    fun TriggerAction(action: Action)
}