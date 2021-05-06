package com.picone.appcompose.ui.component.baseComponent.utilsComponent

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picone.appcompose.ui.SetProgressDrawable
import com.picone.appcompose.ui.values.TopRightCornerCut
import com.picone.core.domain.entity.*
import java.util.*


@Composable
fun <T>ExpandableTaskItem(item: T) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    val itemToShow:BaseTask = when(item){
        is Task -> item
        is UnderStain -> item
        else -> UnknownTask
    }
    Column(
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .padding(5.dp)
            .clip(TopRightCornerCut)
            .background(MaterialTheme.colors.surface)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()

        ) {
            Text(
                text = itemToShow.name,
                style = MaterialTheme.typography.subtitle1
            )
            SetProgressDrawable(start = itemToShow.start, close =itemToShow.close )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = itemToShow.description,
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

@Composable
fun InformationText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 5.dp),
        style = MaterialTheme.typography.body1,
        textAlign = TextAlign.Justify
    )
}

@Composable
fun TitleInformationText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 10.dp),
        style = MaterialTheme.typography.h2,
    )
}

val UnknownTask = Task(0, 0, "task not found", "", 0, Calendar.getInstance().time, null, null, null)