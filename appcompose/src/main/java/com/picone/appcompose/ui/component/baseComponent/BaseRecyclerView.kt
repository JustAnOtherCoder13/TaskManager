package com.picone.appcompose.ui.component.baseComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.picone.appcompose.ui.component.baseComponent.utilsComponent.ExpandableTaskItem
import com.picone.core.domain.entity.CompleteTask

@Composable
fun TaskRecyclerView(
    allTasks: List<CompleteTask>,
    importance: String,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.secondary)
            .fillMaxHeight()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 15.dp)
                .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                .border(2.dp, MaterialTheme.colors.onSurface)
                .background(MaterialTheme.colors.secondaryVariant)

        ) {
            item {
                Text(
                    text = importance,
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colors.surface)
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(items = allTasks) { task ->
                ExpandableTaskItem(task.task,navController = navController)
            }
        }
    }
}



