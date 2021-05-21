package com.picone.simplifyInterfaces.interfaceToObjectList.homeObjects

import com.picone.simplifyInterfaces.baseInterfaces.Event
import com.picone.simplifyInterfaces.baseInterfaces.UiComponent
import com.picone.simplifyInterfaces.baseInterfaces.UiState

object UiComponentObjects {

    val topAppBar = object :UiComponent{
        override val events: List<Event> = HomeEventObjects.topAppBarEvents
        override val states: List<UiState> = HomeStateObjects.TopAppBarStates
    }

}