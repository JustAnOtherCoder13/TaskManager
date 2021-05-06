package com.picone.appcompose.ui.values

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIcon (color:Color, text : String){
    Row (
        verticalAlignment = Alignment.CenterVertically
            ){
        Text(
            text = text,
            style = MaterialTheme.typography.body2
        )
        Icon(
            Icons.Default.Circle,
            contentDescription = null,
            modifier = Modifier
                .padding(5.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Black, CircleShape),
            tint = color,
        )
    }

}