package com.picone.appcompose.ui.values

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val TaskManagerShapes = Shapes(
    small = RoundedCornerShape(5.dp),
    medium = RoundedCornerShape(5.dp),
    large = RoundedCornerShape(8.dp)
)

val TopRightCornerCut = CutCornerShape(topStart = 10.dp)
val TopLeftCornerCut = CutCornerShape(topEnd = 10.dp)
val TopRoundedCorner = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)