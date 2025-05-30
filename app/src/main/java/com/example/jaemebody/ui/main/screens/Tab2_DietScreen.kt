package com.example.jaemebody.ui.main.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.jaemebody.MainViewModel
import com.example.jaemebody.ui.common.DietRecordScreens
import com.example.jaemebody.ui.dietRecord.DietRecordInputScreen
import com.example.jaemebody.ui.dietRecord.DietRecordListScreen
import java.time.LocalDate

@Composable
fun DietScreen(mainViewModel: MainViewModel) {
    val today = LocalDate.now().toString()
    val allExercises by mainViewModel.exerciseRecords.collectAsState()
//    val todayDietRecords = allDietRecords.filter { it.date == today }

    var currentScreen by rememberSaveable { mutableStateOf(DietRecordScreens.DietRecordInfo) }

    LaunchedEffect(Unit) {
        mainViewModel.loadTodayExercises()
    }

    when (currentScreen) {
        DietRecordScreens.DietRecordInfo -> {
            DietRecordListScreen(
                mainViewModel = mainViewModel,
//                todayDietRecords = todayDietRecords, //  전달할 수 있도록 수정
                onAddClicked = {
                    currentScreen = DietRecordScreens.DietRecordAdd
                }
            )
        }

        DietRecordScreens.DietRecordAdd -> {
            DietRecordInputScreen(
                mainViewModel = mainViewModel,
                onSaveClicked = {
                    currentScreen = DietRecordScreens.DietRecordInfo
                },
                onCancelClicked = {
                    currentScreen = DietRecordScreens.DietRecordInfo
                }
            )
        }
    }
}