package com.picone.appcompose.ui.main.screen.home.homeTask.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.picone.appcompose.ui.utils.interfaces.UiState
import com.picone.core.domain.entity.Task
import com.picone.newArchitectureViewModels.HomeScreenViewModel

enum class HomeTaskUiStates : UiState {

    ALL_TASKS {
        override var state: MutableState<Any> = mutableStateOf(listOf<Task>())

        @Composable
        override fun  ObserveState(viewModel: ViewModel){
            viewModel as HomeScreenViewModel
            updateState(viewModel.mAllTasksMutableLD.observeAsState(listOf()).value)
        }
        override fun updateState(state: Any) {this.state.value = state}
    };
}