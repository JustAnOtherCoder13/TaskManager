package com.picone.appcompose.ui.component.screen.home

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.picone.appcompose.ui.component.screen.UiState
import com.picone.core.domain.entity.Task
import com.picone.newArchitectureViewModels.HomeScreenViewModel

enum class HomeTaskUiStates : UiState {

    ALL_TASKS {
        override var state: MutableState<Any> = mutableStateOf(listOf<Task>())

        @Composable
        override fun  observeState(viewModel: ViewModel){
            viewModel as HomeScreenViewModel
            Log.i("TAG", "observeState: ${state.value}")
            //state.value = viewModel.mAllTasksMutableLD.observeAsState(listOf()).value
            updateState(viewModel.mAllTasksMutableLD.observeAsState(listOf()).value)
        }

        override fun updateState(state: Any) {
            this.state.value = state
        }
    };
}