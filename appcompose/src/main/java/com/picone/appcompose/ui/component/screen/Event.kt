package com.picone.appcompose.ui.component.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel

interface Event {

     @Composable
     fun triggerEvent(){}

     @Composable
     fun triggerEvent(viewModel: ViewModel){}
}