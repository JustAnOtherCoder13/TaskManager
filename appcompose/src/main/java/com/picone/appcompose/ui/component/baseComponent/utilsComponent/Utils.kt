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
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.picone.appcompose.ui.MainDestinations.DETAIL
import com.picone.appcompose.ui.SetProgressDrawable
import com.picone.appcompose.ui.main.NavigateToDetailOnTaskClicked
import com.picone.appcompose.ui.values.TopRightCornerCut
import com.picone.core.domain.entity.BaseTask
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import com.picone.core.util.Constants.UnknownTask


@Composable
fun <T>ExpandableTaskItem(item: T,navController: NavController) {
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
        NavigateToDetailOnTaskClicked(item, itemToShow, navController)
        if (expanded) { Description(itemToShow) }
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
private fun Description(itemToShow: BaseTask) {
    Spacer(modifier = Modifier.height(10.dp))
    Row(modifier = Modifier.padding(5.dp)) {
        Text(
            text = itemToShow.description,
            style = MaterialTheme.typography.body1
        )
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