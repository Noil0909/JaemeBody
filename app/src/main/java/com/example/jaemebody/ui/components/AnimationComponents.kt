package com.example.jaemebody.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit

@Composable
fun AnimatedText(text: String, color: Color, fontSize: TextUnit){

    val animatedAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit){
        animatedAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        modifier = Modifier.alpha(animatedAlpha.value)
    )
    }