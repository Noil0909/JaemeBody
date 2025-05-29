package com.example.jaemebody.model

import java.time.LocalDate

data class Exercise(
    val docId: String = "",
    val name: String = "",
    val duration: Int = 0,
    val calorie: Int = 0,
    val date: String = LocalDate.now().toString()
)