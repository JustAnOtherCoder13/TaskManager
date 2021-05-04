package com.picone.appcompose.ui.component.baseComponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.picone.appcompose.R


@Composable
fun AppBar(onAddButtonClick : () -> Unit){
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.primarySurface,
        actions = { Fab { }}
    )

}

@Composable
fun Fab(onAddButtonClick : () -> Unit){
    var isPopUpMenuExpanded by remember {
        mutableStateOf(false)
    }
    val addItems = listOf("Category","Project","Task")
    Button(
        onClick = {isPopUpMenuExpanded =!isPopUpMenuExpanded},
        shape = CircleShape,
        elevation = null,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primarySurface)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
        ){
            Icon(
                imageVector = Icons.Rounded.AddCircle,
                contentDescription = null,
            )
            DropdownMenu(
                expanded = isPopUpMenuExpanded ,
                onDismissRequest = { isPopUpMenuExpanded = false },
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {
                addItems.forEachIndexed{
                        _, s -> DropdownMenuItem(onClick = { isPopUpMenuExpanded = false }) {
                    Text(text = s)
                }
                }

            }
        }
    }
}
