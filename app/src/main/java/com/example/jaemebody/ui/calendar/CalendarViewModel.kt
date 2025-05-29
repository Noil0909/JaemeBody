//package com.example.jaemebody.ui.calendar
//
//import androidx.compose.ui.graphics.Color
//import androidx.lifecycle.ViewModel
//import com.example.jaemebody.model.Exercise
//
//class CalendarViewModel: ViewModel()  {
//    fun getColorForDate(date: String, records: List<Exercise>, targetCalorie: Int): Color {
//        val exercises = records.filter { it.date == date }
//        return when {
//            exercises.isEmpty() -> Color.Gray
//            exercises.sumOf { it.calorie } < targetCalorie -> Color(0xFF9CCC65) // 연두
//            else -> Color(0xFF4CAF50) // 초록
//        }
//    }
//}