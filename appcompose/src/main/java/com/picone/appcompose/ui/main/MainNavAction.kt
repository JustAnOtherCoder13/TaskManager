package com.picone.appcompose.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.picone.appcompose.ui.MainDestinations
import com.picone.appcompose.ui.SetProgressDrawable
import com.picone.core.domain.entity.BaseTask
import com.picone.core.domain.entity.Task


// HOME SCREEN
@Composable
fun <T> NavigateToDetailOnTaskClicked(
    item: T,
    itemToShow: BaseTask,
    navController: NavController
) {
    fun navigateToTask(task: Task) {
        navController.navigate("${MainDestinations.DETAIL}/${task.id}")
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clickable(onClick = { if (item is Task) navigateToTask(item as Task) })
            .padding(5.dp)
            .fillMaxWidth()

    ) {
        Text(
            text = itemToShow.name,
            style = MaterialTheme.typography.subtitle1,
        )
        SetProgressDrawable(start = itemToShow.start, close = itemToShow.close)
    }
}