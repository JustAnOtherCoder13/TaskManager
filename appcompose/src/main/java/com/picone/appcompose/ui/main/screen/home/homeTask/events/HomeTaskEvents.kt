package com.picone.appcompose.ui.main.screen.home.homeTask.events

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.picone.appcompose.ui.utils.interfaces.Event

enum class HomeTaskEvents {

    GET_INITIAL_STATE{
        override fun getEvent() {
            object : Event {}
            }
    };
    abstract fun getEvent()
}