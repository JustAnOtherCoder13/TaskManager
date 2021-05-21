package com.picone.simplifyInterfaces.interfaceToObjectList.homeObjects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.picone.core.util.Constants
import com.picone.newArchitectureViewModels.HomeScreenViewModel
import com.picone.simplifyInterfaces.baseInterfaces.UiState

object HomeStateObjects {

    private val TopAppBarPopUpMenu = object : UiState{
        override var state: MutableState<Any> = mutableStateOf(listOf(
            Constants.CATEGORY,
            Constants.PROJECT,
            Constants.TASK
        ))
        @Composable
        override fun ObserveState(viewModel: ViewModel){}
        override fun updateState(state: Any) {}
    }

    private val TopAppBarIsPopUpMenuExpanded = object : UiState{
        override var state: MutableState<Any> = mutableStateOf(false)

        @Composable
        override fun ObserveState(viewModel: ViewModel) {
            viewModel as HomeScreenViewModel
            viewModel.mPopUpStateMutableLD.observeAsState()
        }
        override fun updateState(state: Any) {this.state.value = state}
    }

    val TopAppBarStates = listOf(TopAppBarIsPopUpMenuExpanded, TopAppBarPopUpMenu)
}