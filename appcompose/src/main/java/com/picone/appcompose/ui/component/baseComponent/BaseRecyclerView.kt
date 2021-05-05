package com.picone.appcompose.ui.component.baseComponent

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picone.core.domain.entity.CompleteTask

@Composable
fun TaskRecyclerView(allTasks: List<CompleteTask>, importance: String) {

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
                TaskItem(task)
            }
        }
    }
}

@Composable
private fun TaskItem(task: CompleteTask) {
    var expanded: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .padding(5.dp)
            .clip(CutCornerShape(topStart = 10.dp))
            .background(MaterialTheme.colors.surface)
    ) {
        Row(modifier = Modifier.padding(5.dp)) {
            Text(
                text = task.task.name ?: "no name for this task",
                style = MaterialTheme.typography.subtitle1
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = task.task.description ?: "no description",
                    style = MaterialTheme.typography.body1
                )
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = !expanded })
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

    }
}