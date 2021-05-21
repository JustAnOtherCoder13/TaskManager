package com.picone.appcompose.ui.main.screen.home.homeTask.actions

import com.picone.appcompose.ui.utils.interfaces.Action

object HomeActions {

    val updateScreenState = object: Action {}
    val topAppBarAddItemButtonOpenPoUp = object : Action {}
    val topAppBarAddItemButtonClosePoUp = object : Action {}
    val navigateToAddOnPopUpItemSelected = object : Action {}
    val navigateToDetailOnTaskClicked = object : Action {}
    val navigateToHomeTaskOnNavItemClicked = object : Action {}
    val navigateToProjectOnNavItemClicked = object : Action {}
}