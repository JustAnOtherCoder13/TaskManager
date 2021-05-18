package com.picone.appcompose.ui.component.manager.action

interface Action {
    fun doAction(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {

        }
    }
}
