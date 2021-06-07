package com.picone.appcompose.ui.main.baseComponents

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.picone.appcompose.R

@Composable
fun BaseOkAndCancelButtons(
    state_isOkButtonEnable: Boolean,
    event_onOkButtonClicked: () -> Unit,
    event_onCancelButtonClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(//positive button
            onClick = { event_onOkButtonClicked() },
            enabled = state_isOkButtonEnable,
            content = { Text(text = stringResource(R.string.ok)) }
        )
        Button( //cancel button
            onClick = { event_onCancelButtonClicked() },
            content = {Text(text = stringResource(R.string.cancel))}
        )
    }
}

@Composable
fun ExpandIcon(
    state_isExpanded: Boolean,
    event_onExpandIconCLick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = event_onExpandIconCLick),
        content = {
            Icon(
                imageVector = if (state_isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    )
}

@Composable
fun BaseDatePickerClickableIcon(
    state_datePickerIconDateText: String,
    event_onDatePickerIconClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .animateContentSize()
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.surface)
            .clickable { event_onDatePickerIconClicked() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.dead_line),
            modifier = Modifier.padding(2.dp),
            style = MaterialTheme.typography.subtitle2
        )
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            modifier = Modifier
                .padding(5.dp)
        )
        if (state_datePickerIconDateText.trim().isNotEmpty()) {
            Text(
                text = state_datePickerIconDateText,
                modifier = Modifier.padding(5.dp),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}