package com.picone.appcompose.ui.component.screen.home

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.picone.appcompose.ui.component.screen.Event
import com.picone.newArchitectureViewModels.HomeScreenViewModel

enum class HomeTaskEvents {

    GET_INITIAL_STATE{
        override fun getEvent() {
            object : Event{
                @Composable
                override fun triggerEvent(viewModel: ViewModel) {
                    //HomeTaskUiStates.ALL_TASKS.observeState(viewModel = viewModel)
                }

            }

            }
    };
    abstract fun getEvent()
}