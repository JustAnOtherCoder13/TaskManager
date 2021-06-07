package com.picone.appcompose.ui.main.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@ExperimentalAnimationApi
@Composable
fun EnterAnimation(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(
            initialOffsetY = { -200 }
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        content = content,
        initiallyVisible = false
    )
}

@ExperimentalAnimationApi
@Composable
fun HorizontalAnimationLeftToRight(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(
            initialOffsetX = { -200 },
            animationSpec = tween(durationMillis = 200)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(durationMillis = 200),
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { 200 },
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
        ) + shrinkHorizontally() + fadeOut(),
        content = { content() },
        initiallyVisible = false
    )

}

@ExperimentalAnimationApi
@Composable
fun HorizontalAnimationRightToLeft(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(
            initialOffsetX = { 200 },
            animationSpec = tween(durationMillis = 200)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(durationMillis = 200),
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { -200 },
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
        ) + shrinkHorizontally(
            shrinkTowards = Alignment.Start
        ) + fadeOut(),
        content = { content() },
        initiallyVisible = false
    )

}