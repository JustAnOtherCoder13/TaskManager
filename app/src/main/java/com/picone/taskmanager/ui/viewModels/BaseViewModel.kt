package com.picone.taskmanager.ui.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BaseViewModel : ViewModel() {

    val ioScope = CoroutineScope(Dispatchers.IO)
}