package com.picone.appcompose.ui.main.screen.home.homeTask.states

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.picone.appcompose.ui.main.screen.home.homeTask.events.HomeTaskEvents
import com.picone.appcompose.ui.utils.interfaces.UiState
import com.picone.core.domain.entity.Task

class HomeStateManager(private val viewModel: ViewModel) {

    @Composable
    fun updateState(event: HomeTaskEvents): UiState {

        when (event) {
            HomeTaskEvents.GET_INITIAL_STATE -> {
                HomeTaskUiStates.ALL_TASKS.ObserveState(viewModel)
                return HomeTaskUiStates.ALL_TASKS
            }
        }
    }


    fun getState(state: UiState): List<Task> {

        return when (state) {
            HomeTaskUiStates.ALL_TASKS -> {
                val result = HomeTaskUiStates.ALL_TASKS.state.value
                result as List<*>
                result.filterIsInstance<Task>()
            }
            else -> listOf()
        }
    }


}