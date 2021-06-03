package com.picone.newArchitectureViewModels.androidUiManager

import com.picone.newArchitectureViewModels.androidUiManager.androidNavActions.AndroidNavActionManager

interface UiAction
interface HomeAction : UiAction
interface HomeNavAction : HomeAction {
    val androidNavActionManager: AndroidNavActionManager
}

interface DetailAction : UiAction
interface DetailNavAction : DetailAction {
    val androidNavActionManager: AndroidNavActionManager
}

interface AddAction : UiAction
interface AddNavAction : AddAction {
    val androidNavActionManager: AndroidNavActionManager
}