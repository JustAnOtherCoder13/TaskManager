package com.picone.simplifyInterfaces.baseInterfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel

interface UiState {
    var state: MutableState<Any>
    @Composable
    fun ObserveState(viewModel: ViewModel)
    fun updateState(state : Any)
}