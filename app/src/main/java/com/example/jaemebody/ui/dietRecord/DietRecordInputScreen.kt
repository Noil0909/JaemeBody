package com.example.jaemebody.ui.dietRecord

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaemebody.ui.components.AnimatedText
import com.example.jaemebody.ui.components.CustomGradientButton
import com.example.jaemebody.ui.components.CustomTextField


@Composable
fun DietRecordInputScreen(
    onSaveClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Spacer(modifier = Modifier.padding(20.dp))

            AnimatedText(
                text = "Diet Record Input",
                color = Color.White,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.padding(20.dp))

            CustomTextField(
                value = "",
                onValueChange = {},
                placeholder = "운동 종류"
            )

            Spacer(modifier = Modifier.padding(20.dp))

            CustomTextField(
                value = "",
                onValueChange = {},
                placeholder = "칼로리"
            )

            Spacer(modifier = Modifier.padding(20.dp))

            CustomTextField(
                value = "",
                onValueChange = {},
                placeholder = "시간"
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 50.dp)
        ){
            CustomGradientButton(
                text = "저장",
                onClick = {
                    onSaveClicked()
                },
                gradientColors = listOf(Color(0xFF6A1B9A), Color(0xFFAB47BC))
            )

            Spacer(modifier = Modifier.padding(5.dp))

            CustomGradientButton(
                text = "취소",
                onClick = {
                    onCancelClicked()
                },
                gradientColors = listOf(Color.Gray, Color.DarkGray)
            )
        }

    }
}