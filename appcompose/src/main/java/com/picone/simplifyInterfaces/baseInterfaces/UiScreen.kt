package com.picone.simplifyInterfaces.baseInterfaces

import androidx.compose.runtime.Composable

interface UiScreen {

    @Composable
    fun events(): List<Event>

    @Composable
    fun states(): List<UiState>

    @Composable
    fun CreateScreen(component : @Composable ()-> Unit)

}