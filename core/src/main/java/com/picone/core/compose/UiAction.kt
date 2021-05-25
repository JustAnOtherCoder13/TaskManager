package com.picone.core.compose

import com.picone.core.domain.navAction.NavAction
import com.picone.core.domain.navAction.NavActionManager

interface UiAction
interface HomeAction : UiAction
interface HomeNavAction : HomeAction {
    val navActionManager: NavActionManager
}

interface DetailAction : UiAction
interface DetailNavAction : DetailAction {
    val navActionManager: NavActionManager
}

interface AddAction : UiAction
interface AddNavAction : AddAction {
    val navActionManager: NavActionManager
}