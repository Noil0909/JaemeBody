package com.example.jaemebody.ui.dietRecord.dietRecordInfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaemebody.MainViewModel
import com.example.jaemebody.ui.components.AnimatedText
import com.example.jaemebody.ui.components.CustomGradientButton
import com.example.jaemebody.ui.theme.JaemeBodyTheme

class DietRecordInfoActivity : ComponentActivity(){

    private val dietRecordInfoViewModel by viewModels<DietRecordInfoViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val docId = intent.getStringExtra("docId")
        val name = intent.getStringExtra("name")
        val duration = intent.getStringExtra("duration")
        val calorie = intent.getStringExtra("calorie")

        setContent {

            JaemeBodyTheme {
                DietRecordInfoScreen(
                    dietRecordInfoViewModel = dietRecordInfoViewModel,
                    mainViewModel = mainViewModel,
                    docId = docId ?: "",
                    name = name ?: "",
                    duration = duration ?: "",
                    calorie = calorie ?: ""
                )

            }
        }
    }

}

@Composable
fun DietRecordInfoScreen(
    docId: String,
    name: String,
    duration: String,
    calorie: String,
    dietRecordInfoViewModel: DietRecordInfoViewModel,
    mainViewModel: MainViewModel
){

    val context = LocalContext.current
    val activity = context as DietRecordInfoActivity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
    ){
        AnimatedText(
            text = "운동 정보",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.padding(20.dp))

        Text(
            text = "운동명 : $name",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp)
        )

        Text(
            text = "운동 시간 : $duration",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp)
        )

        Text(
            text = "소모 칼로리 : $calorie",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp)
        )

        Spacer(modifier = Modifier.padding(20.dp))

        CustomGradientButton(
            text = "삭제하기",
            onClick = {
                dietRecordInfoViewModel.removeExerciseRecord(docId) {
                    mainViewModel.loadTodayExercises()
                    activity.finish()
                }
            },
            gradientColors = listOf(Color(0xFF6A1B9A), Color(0xFFAB47BC))
        )

        Spacer(modifier = Modifier.padding(20.dp))

        CustomGradientButton(
            text = "뒤로가기",
            onClick = { activity.finish() },
            gradientColors = listOf(Color.Gray, Color.DarkGray)
        )

    }

}