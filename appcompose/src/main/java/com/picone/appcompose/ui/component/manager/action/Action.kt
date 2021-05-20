package com.picone.appcompose.ui.component.manager.action

import android.util.Log
import androidx.compose.runtime.Composable

interface Action {

    fun doAction(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            Log.e("TAG", "doAction: invalid action ",e )
        }
    }

    @Composable
    fun DoAction(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            Log.e("TAG", "doAction: invalid action ",e )
        }
    }
}
