//package com.example.jaemebody.ui.calendar
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.jaemebody.MainViewModel
//import com.example.jaemebody.model.Exercise
//import java.time.LocalDate
//import java.time.YearMonth
//
//@Composable
//fun CalendarScreen(
//    mainViewModel: MainViewModel,
//    allExercises: List<Exercise>,
//    targetCalorie: Int = 1500
//) {
//    val daysInMonth = YearMonth.now().lengthOfMonth()
//    val today = LocalDate.now()
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .background(Color.Black)
//        .padding(16.dp)) {
//
//        Text("운동 기록 달력", color = Color.White, fontSize = 20.sp)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        LazyVerticalGrid(columns = GridCells.Fixed(7)) {
//            items(daysInMonth) { day ->
//                val dateStr = today.withDayOfMonth(day + 1).toString()
//                val color = getColorForDate(dateStr, allExercises, targetCalorie)
//
//                Box(
//                    modifier = Modifier
//                        .size(40.dp)
//                        .padding(4.dp)
//                        .background(color, shape = CircleShape),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(text = "${day + 1}", color = Color.White, fontSize = 14.sp)
//                }
//            }
//        }
//    }
//}
//
//fun getColorForDate(date: String, records: List<Exercise>, targetCalorie: Int): Color {
//    val exercises = records.filter { it.date == date }
//    return when {
//        exercises.isEmpty() -> Color.Gray
//        exercises.sumOf { it.calorie } < targetCalorie -> Color(0xFF9CCC65) // 연두
//        else -> Color(0xFF4CAF50) // 초록
//    }
//}