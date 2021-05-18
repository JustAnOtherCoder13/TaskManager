package com.picone.appcompose.ui.component.manager.action.nav

import androidx.navigation.compose.NamedNavArgument
import com.picone.core.util.Constants

interface NavigationCommand {
    val arguments : List<NamedNavArgument>
    val destination : String
    fun getRoute(Key:String) : String = "${destination}/{${Constants.KEY_TASK}}"
}