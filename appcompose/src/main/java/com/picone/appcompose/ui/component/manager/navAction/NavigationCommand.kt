package com.picone.appcompose.ui.component.manager.navAction

import androidx.navigation.compose.NamedNavArgument
import com.picone.core.util.Constants

interface NavigationCommand {
    val arguments: List<NamedNavArgument>
    val destination: String
    val KEY: String
        get() = ""

    fun getRoute(): String =
        if (KEY.trim().isNotEmpty()) {
            "${destination}/{${Constants.KEY_TASK}}"
        } else {
            destination
        }
}