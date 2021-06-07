package com.picone.appcompose.ui.main.baseComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.picone.appcompose.ui.values.ProgressIcon
import com.picone.core.domain.entity.Category
import java.util.*

@Composable
fun setRedBorderStrokeOnSpinnerError(
    state_nullableErrorItem: String?,
    state_nullablePreselectedItem: String?,
    state_baseSpinnerHint: String
) = if (state_nullableErrorItem != null
    && state_nullablePreselectedItem ?: state_baseSpinnerHint == state_nullableErrorItem
) {
    BorderStroke(2.dp, MaterialTheme.colors.error)
} else BorderStroke(0.dp, Color.Transparent)

@Composable
fun categoriesNameList(state_allCategories: List<Category>): MutableList<String> {
    val filterByCategoryList: MutableList<String> = mutableListOf()
    state_allCategories.forEachIndexed { _, category -> filterByCategoryList.add(category.name) }
    return filterByCategoryList
}

@Composable
fun setRedBorderStrokeOnEmpty(state_text: String) =
    if (state_text.trim().isEmpty())
        BorderStroke(2.dp, MaterialTheme.colors.error)
    else BorderStroke(0.dp, Color.Transparent)

data class ColorItem(val name: String, val color: Long)

@Composable
fun categoryColorsList(): List<ColorItem> {
    return listOf(
        ColorItem(name = "Red", color = 0xfffe0000),
        ColorItem(name = "Yellow", color = 0xfffdfe02),
        ColorItem(name = "Green", color = 0xff0bff01),
        ColorItem(name = "Blue", color = 0xff011efe),
        ColorItem(name = "Purple", color = 0xfffe00f6)
    )
}

@Composable
fun <T> IconToPass(state_icon: T, event_OnClick: () -> Unit) {
    when (state_icon) {
        is ImageVector -> Icon(
            imageVector = state_icon,
            contentDescription = null,
            modifier = Modifier
                .clickable { event_OnClick() }
                .fillMaxHeight()
                .padding(horizontal = 5.dp)
        )
        is ImageBitmap -> Icon(
            bitmap = state_icon,
            contentDescription = null,
            modifier = Modifier
                .clickable { event_OnClick() }
                .fillMaxHeight()
                .padding(horizontal = 5.dp)
        )
        is Painter -> Icon(
            painter = state_icon,
            contentDescription = null,
            modifier = Modifier
                .clickable { event_OnClick() }
                .fillMaxHeight()
                .padding(horizontal = 5.dp)
        )
        else -> throw Exception("pass only Painter, ImageVector or ImageBitmap for icon value")
    }
}

@Composable
fun SetProgressDrawable(start: Date?, close: Date?) {
    return when(close){
        null ->
            if (start!=null) ProgressIcon(color = Color.Yellow,"In progress  ")
            else ProgressIcon(color = Color.Red,"To do  ")
        else -> ProgressIcon(color = Color.Green,"Done  ")
    }
}