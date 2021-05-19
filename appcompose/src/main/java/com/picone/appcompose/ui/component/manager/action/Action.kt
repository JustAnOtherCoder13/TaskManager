package com.picone.appcompose.ui.component.manager.action

import android.util.Log

interface Action {
    fun doAction(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            Log.e("TAG", "doAction: invalid action ",e )
        }
    }
}
