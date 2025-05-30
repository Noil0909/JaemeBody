package com.example.jaemebody.ui.main.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.jaemebody.MainViewModel
import com.example.jaemebody.model.Exercise
import com.example.jaemebody.ui.components.AnimatedText
import com.example.jaemebody.ui.components.ShiningText
import java.time.LocalDate

@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    val allExercises by mainViewModel.exerciseRecords.collectAsState()
    val today = LocalDate.now().toString()
    val todayExercises = allExercises.filter { it.date == today }

    LaunchedEffect(Unit) {
        mainViewModel.loadTodayExercises()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (todayExercises.isNotEmpty()) {
            AnimatedExerciseGraph(todayExercises)
        } else {
            Text(
                text = "오늘의 운동 기록이 없습니다.",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun AnimatedExerciseGraph(exercises: List<Exercise>) {

    val maxDuration = exercises.maxOf { it.duration }
    val maxCalorie = exercises.maxOf { it.calorie }

    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.padding(15.dp))
//
//        ShiningText()
        Text(
            text = "오늘의 운동 기록",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White

        )

        Spacer(modifier = Modifier.padding(15.dp))

        Row(
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {

            exercises.forEach { exercise ->

                val durationRatio = exercise.duration.toFloat() / maxDuration
                val calorieRatio = exercise.calorie.toFloat() / maxCalorie

                val animatedDurationRatio = remember { Animatable(0f) }
                val animatedCalorieRatio = remember { Animatable(0f) }

                LaunchedEffect(Unit) {
                    animatedDurationRatio.animateTo(
                        durationRatio, animationSpec = tween(durationMillis = 1000)
                    )
                    animatedCalorieRatio.animateTo(
                        calorieRatio,
                        animationSpec = tween(durationMillis = 1000)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(animatedDurationRatio.value * 200.dp)
                            .background(
                                Color.Blue,
                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )

                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(animatedCalorieRatio.value * 200.dp)
                            .background(
                                Color.Red,
                            )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = exercise.name,
                        color = Color.White,
                        fontSize = 20.sp
                    )

                }



            }

        }

        Column {

            AnimatedText(
                text = "duration",
                color = Color.Blue,
                fontSize = 24.sp
            )

            AnimatedText(
                text = "calorie",
                color = Color.Red,
                fontSize = 24.sp
            )
        }

    }
}