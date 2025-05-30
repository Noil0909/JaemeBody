package com.example.jaemebody.ui.main.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.jaemebody.MainViewModel
import com.example.jaemebody.model.Exercise
import com.example.jaemebody.ui.components.AnimatedText
import com.example.jaemebody.ui.components.ShiningText
import java.time.LocalDate

@ExperimentalLayoutApi
@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    val exercises by mainViewModel.exerciseRecords.collectAsState()
    val today = LocalDate.now().toString()
    val todayExercises = exercises.filter { it.date == today }

    val totalCalories = todayExercises.sumOf { it.calorie }
    val totalDuration = todayExercises.sumOf { it.duration }
    val targetCalories by mainViewModel.targetCalorie.collectAsState()

    val progress = totalCalories.toFloat() / targetCalories

    LaunchedEffect(Unit) {
        mainViewModel.loadTodayExercises()
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("🏋️‍♂️ 오늘 총 ${totalDuration}분 운동", fontSize = 20.sp, color = Color.White)
                Text("🔥 $totalCalories kcal 소모", fontSize = 20.sp, color = Color.White)
                Text("💪 목표의 ${(progress * 100).toInt()}% 달성", fontSize = 18.sp, color = Color.Green)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (todayExercises.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "오늘의 운동 기록이 없습니다.\n\n운동을 추가해보세요!",
                    color = Color.Gray,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        else {
            Text("오늘 한 운동", color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            BadgeListSection(todayExercises)
            AnimatedExerciseGraph(todayExercises)
        }
    }
}

@ExperimentalLayoutApi
@Composable
fun BadgeListSection(exercises: List<Exercise>) {
    // 중복된 운동 제거 (이름 기준)
    val uniqueNames = exercises.map { it.name }.distinct()

    Box(
        modifier = Modifier
            .height(80.dp) // ✅ 최대 두 줄 정도 높이 제한
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()) // ✅ 내부 스크롤 가능하도록
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            uniqueNames.forEach { name ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color(0xFF3700B3), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "• $name",
                        color = Color.White,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedExerciseGraph(exercises: List<Exercise>) {
    val maxDuration = exercises.maxOfOrNull { it.duration } ?: 1
    val maxCalorie = exercises.maxOfOrNull { it.calorie } ?: 1

    val barMaxHeight = 140.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .height(315.dp) // 그래프 높이 고정
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            exercises.forEach { exercise ->
                val durationRatio = exercise.duration.toFloat() / maxDuration
                val calorieRatio = exercise.calorie.toFloat() / maxCalorie

                val animatedDuration = remember { Animatable(0f) }
                val animatedCalorie = remember { Animatable(0f) }

                LaunchedEffect(exercise.name) {
                    animatedDuration.animateTo(
                        targetValue = durationRatio,
                        animationSpec = tween(durationMillis = 800)
                    )
                    animatedCalorie.animateTo(
                        targetValue = calorieRatio,
                        animationSpec = tween(durationMillis = 800)
                    )
                }

                Column(
                    modifier = Modifier
                        .width(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    // 막대 그래프만 아래에서 위로 커지도록
                    Box(
                        modifier = Modifier
                            .height(animatedDuration.value * barMaxHeight)
                            .width(16.dp)
                            .background(Color.Blue, RoundedCornerShape(4.dp))
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .height(animatedCalorie.value * barMaxHeight)
                            .width(16.dp)
                            .background(Color.Red, RoundedCornerShape(4.dp))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 운동 이름 고정 표시
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            exercises.forEach { exercise ->
                Text(
                    text = exercise.name,
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .width(48.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedText(
                text = "시간",
                color = Color.Blue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(10.dp))
            AnimatedText(
                text = "칼로리",
                color = Color.Red,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}