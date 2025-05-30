package com.example.jaemebody.ui.main.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaemebody.MainViewModel
import com.example.jaemebody.model.Exercise
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(mainViewModel: MainViewModel) {

    val allExercises by mainViewModel.exerciseRecords.collectAsState()

    val today = remember{LocalDate.now()}
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }


    val daysInMonth = remember(currentMonth) { currentMonth.lengthOfMonth() }
    val firstDayOfMonth = remember(currentMonth) { currentMonth.atDay(1).dayOfWeek.value % 7 }
    val targetCalorie = 1500

    val dates = remember(currentMonth) {
        buildList {
            repeat(firstDayOfMonth) { add(null) }
            repeat(daysInMonth) { day -> add(currentMonth.atDay(day + 1)) }
        }
    }

    LaunchedEffect(Unit) {
        mainViewModel.loadExercises()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "운동 기록 캘린더",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 월 표시 및 이동 버튼
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {
                currentMonth = currentMonth.minusMonths(1)
                selectedDate = null
            }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "이전 달", tint = Color.White)
            }

            Text(
                text = "${currentMonth.year}년 ${currentMonth.monthValue}월",
                color = Color.LightGray,
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IconButton(onClick = {
                currentMonth = currentMonth.plusMonths(1)
                selectedDate = null
            }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "다음 달", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 요일 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // 달력
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            items(dates) { date ->
                val bgColor = when {
                    date == null -> Color.Transparent
                    else -> getColorForDate(date.toString(), allExercises, targetCalorie)
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .border(
                            width = if (date == today) 2.dp else 1.dp,
                            color = if (date == today) Color.White else Color.DarkGray,
                            shape = RectangleShape
                        )
                        .background(bgColor)
                        .clickable(enabled = date != null) {
                            selectedDate = date
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date?.dayOfMonth?.toString() ?: "",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 날짜 선택 시 상세 운동 기록 표시
        selectedDate?.let { date ->
            val dayExercises = allExercises.filter { it.date == date.toString() }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = "${date.monthValue}월 ${date.dayOfMonth}일 기록",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (dayExercises.isEmpty()) {
                    Text(text = "운동 기록 없음", color = Color.Gray)
                } else {
                    dayExercises.forEach {
                        Text(
                            text = "${it.name} - ${it.duration}분 / ${it.calorie}kcal",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

fun getColorForDate(date: String, records: List<Exercise>, targetCalorie: Int): Color {
    val exercises = records.filter { it.date == date }
    return when {
        exercises.isEmpty() -> Color.Gray
        exercises.sumOf { it.calorie } < targetCalorie -> Color(0xFF9CCC65) // 연두
        else -> Color(0xFF4CAF50) // 초록
    }
}