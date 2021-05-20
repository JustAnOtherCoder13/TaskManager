package com.picone.appcompose.ui.component.screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.picone.appcompose.ui.component.screen.home.HomeTaskEvents
import com.picone.appcompose.ui.component.screen.home.HomeTaskUiStates
import com.picone.core.domain.entity.Task

class HomeStateManager(private val viewModel: ViewModel) {

    @Composable
    fun updateState(event: HomeTaskEvents) : UiState {

        when(event){
            HomeTaskEvents.GET_INITIAL_STATE -> {
                HomeTaskUiStates.ALL_TASKS.observeState(viewModel)
                return HomeTaskUiStates.ALL_TASKS
            }
        }
    }

    fun  getState(state:UiState): List<Task> {
        when(state){
            HomeTaskUiStates.ALL_TASKS -> {
                val result = HomeTaskUiStates.ALL_TASKS.state.value
                result as List<*>
                return result.filterIsInstance<Task>()
            }
            else -> return listOf()
        }
    }


}