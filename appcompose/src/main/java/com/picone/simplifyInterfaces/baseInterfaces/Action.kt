package com.picone.simplifyInterfaces.baseInterfaces

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

interface Action {

    fun doAction(action: () -> Unit) { action() }
    fun doAction(action: () -> Unit,viewModel: ViewModel) { action() }
    fun doAction(action: () -> Unit,navController: NavController) { action() }

    @Composable
    fun DoAction(action: () -> Unit) { action() }
    @Composable
    fun DoAction(action: () -> Unit,viewModel: ViewModel) { action() }
    @Composable
    fun DoAction(action: () -> Unit,navController: NavController) { action() }
}
