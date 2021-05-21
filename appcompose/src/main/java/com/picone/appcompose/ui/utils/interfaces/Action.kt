package com.picone.appcompose.ui.utils.interfaces

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

interface Action {

    fun doAction(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            Log.e("TAG", "doAction: invalid action ",e )
        }
    }

    fun doAction(action: () -> Unit,viewModel: ViewModel) {
        try {
            action()
        } catch (e: Exception) {
            Log.e("TAG", "doAction: invalid action ",e )
        }
    }
    fun doAction(action: () -> Unit,navController: NavController) {
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
    @Composable
    fun DoAction(action: () -> Unit,viewModel: ViewModel) {
        try {
            action()
        } catch (e: Exception) {
            Log.e("TAG", "doAction: invalid action ",e )
        }
    }

    @Composable
    fun DoAction(action: () -> Unit,navController: NavController) {
        try {
            action()
        } catch (e: Exception) {
            Log.e("TAG", "doAction: invalid action ",e )
        }
    }
}
