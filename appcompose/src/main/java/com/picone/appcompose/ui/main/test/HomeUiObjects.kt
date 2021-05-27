package com.picone.appcompose.ui.main.test

import com.picone.viewModels.androidUiManager.UiAction
import com.picone.viewModels.androidUiManager.UiState
import com.picone.core.domain.entity.Task
import com.picone.viewModels.HomeViewModel

typealias Reducer <S> = (S, UiAction) -> S

object HomeUiObjects {


    sealed class HomeActions : UiAction {
        data class OnHomeCreated(val homeViewModel: HomeViewModel) : HomeActions()
    }
    sealed class HomeStates : UiState {
        data class AllTaskState(var allTasks : List<Task>) : HomeStates()

    }

    val HomeStateReducer : Reducer<HomeStates> = {
        old, action ->
        when(action){
            is HomeActions.OnHomeCreated -> {
                old as HomeStates.AllTaskState
                old
            }
            else -> old
        }

    }

}