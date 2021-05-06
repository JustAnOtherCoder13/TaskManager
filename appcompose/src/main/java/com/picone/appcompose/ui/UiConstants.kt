package com.picone.appcompose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.picone.appcompose.ui.values.ProgressIcon
import java.util.*


@Composable
fun SetProgressDrawable(start:Date?,close: Date?) {
    return when(close){
        null ->
            if (start!=null) ProgressIcon(color = Color.Yellow,"In progress  ")
            else ProgressIcon(color = Color.Red,"To do  ")
        else -> ProgressIcon(color = Color.Green,"Done  ")
    }
}