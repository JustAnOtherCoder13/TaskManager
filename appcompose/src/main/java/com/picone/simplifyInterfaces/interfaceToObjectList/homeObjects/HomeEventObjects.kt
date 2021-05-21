package com.picone.simplifyInterfaces.interfaceToObjectList.homeObjects

import androidx.compose.runtime.Composable
import com.picone.simplifyInterfaces.baseInterfaces.Action
import com.picone.simplifyInterfaces.baseInterfaces.Event

object HomeEventObjects {

    private val topAppBarOnAddItemSelected = object : Event{
        @Composable
        override fun TriggerAction(action: Action) {
            TODO("Not yet implemented")
        }
    }
    private val topAppBarOnAddButtonClicked = object :Event{
        @Composable
        override fun TriggerAction(action: Action) {
            TODO("Not yet implemented")
        }
    }

    private val topAppBaronClosePopUp = object : Event{
        @Composable
        override fun TriggerAction(action: Action) {
            TODO("Not yet implemented")
        }
    }

    val topAppBarEvents = listOf(topAppBarOnAddButtonClicked, topAppBaronClosePopUp, topAppBarOnAddItemSelected)

}