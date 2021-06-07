package com.picone.appcompose.ui.main.baseComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun BaseDescriptionText(
    state_descriptionText: String
) {
    Spacer(modifier = Modifier.height(10.dp))
    Row(modifier = Modifier.padding(5.dp)) {
        Text(
            text = state_descriptionText,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun BaseInformationText(
    state_information_text: String
) {
    Text(
        text = state_information_text,
        modifier = Modifier.padding(horizontal = 5.dp),
        style = MaterialTheme.typography.body1,
        textAlign = TextAlign.Justify
    )
}

@Composable
fun BaseTitleInformationText(
    state_titleText: String
) {
    Text(
        text = state_titleText,
        modifier = Modifier.padding(horizontal = 10.dp),
        style = MaterialTheme.typography.h2,
    )
}

@Composable
fun BaseEditText(
    state_title: String,
    state_textColor: Color,
    state_text: String,
    event_baseEditTextOnTextChange: (text: String) -> Unit
) {
    Text(
        text = state_title,
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 5.dp),
        style = MaterialTheme.typography.subtitle2,
        color = state_textColor
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                if (state_title == "Name") PaddingValues(horizontal = 5.dp)
                else PaddingValues(start = 5.dp, end = 5.dp, bottom = 10.dp)
            )
    ) {
        TextField(
            value = state_text,
            onValueChange = { value -> event_baseEditTextOnTextChange(value) },
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .border(setRedBorderStrokeOnEmpty(state_text))
                .fillMaxWidth(),
        )
    }
}