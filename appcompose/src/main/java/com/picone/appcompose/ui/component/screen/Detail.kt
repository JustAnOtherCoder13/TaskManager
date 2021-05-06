package com.picone.appcompose.ui.component.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.picone.appcompose.ui.component.baseComponent.utilsComponent.ExpandableTaskItem
import com.picone.appcompose.ui.component.baseComponent.utilsComponent.InformationText
import com.picone.appcompose.ui.component.baseComponent.utilsComponent.TitleInformationText
import com.picone.appcompose.ui.values.TopLeftCornerCut
import com.picone.appcompose.ui.values.TopRightCornerCut
import com.picone.appcompose.ui.values.TopRoundedCorner
import com.picone.core.domain.entity.CompleteTask
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Detail(task: CompleteTask) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(TopRoundedCorner)
            .background(MaterialTheme.colors.secondary)
    ) {

        item {
            Header(task)
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        items(items = task.underStainsForTask) { underStain ->
            ExpandableTaskItem(underStain)
        }
    }
}

@Composable
private fun Header(task: CompleteTask) {
    Row(
        modifier = Modifier
            .clip(TopRoundedCorner)
            .background(MaterialTheme.colors.primaryVariant)
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TaskInformation(task)
        UnderStainInformation(task)
    }
}

@Composable
private fun UnderStainInformation(task: CompleteTask) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .clip(TopLeftCornerCut)
            .background(MaterialTheme.colors.surface)
    ) {
        TitleInformationText(text = "Under Stain ")
        Spacer(modifier = Modifier.height(10.dp))
        InformationText(text = "Total = " + task.underStainsForTask.size)
        InformationText(
            text = "Done = " +
                    if (getCompletedUnderStainsForTask(task).isEmpty()) "0"
                    else getCompletedUnderStainsForTask(task)
        )
    }
}

@Composable
private fun TaskInformation(task: CompleteTask) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .clip(TopRightCornerCut)
            .background(MaterialTheme.colors.surface)

    ) {
        TitleInformationText(text = task.task.name ?: "no name for this task")
        Spacer(modifier = Modifier.height(10.dp))
        InformationText(
            text = "Create on : " + SimpleDateFormat(
                "dd/MM/yyy",
                Locale.FRANCE
            ).format(task.task.creation)
        )
        InformationText(
            text =
            "Deadline on : " +
                    if (task.task.deadLine == null) "none"
                    else SimpleDateFormat("dd/MM/yyy", Locale.FRANCE)
                        .format(task.task.deadLine!!)
        )
    }
}

@Composable
private fun getCompletedUnderStainsForTask(task: CompleteTask) =
    task.underStainsForTask.filter { underStain ->
        underStain.close != null
    }

