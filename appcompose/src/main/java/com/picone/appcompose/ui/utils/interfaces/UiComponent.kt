package com.picone.appcompose.ui.utils.interfaces

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

interface UiComponent {

    val events: List<Event>

    val states: List<UiState>

    @Composable
    fun CreateComponent(component: @Composable () -> Unit) {
        Row() {
            component()
        }
    }
}