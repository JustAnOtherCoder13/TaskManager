package com.picone.appcompose.ui.component.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel

interface UiState {


    var state: MutableState<Any>

    @Composable
    fun observeState(viewModel: ViewModel) :Any {
        return ""
    }

    fun updateState(state : Any){
        this.state.value = state
    }
}