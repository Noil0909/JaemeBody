package com.example.jaemebody.ui.dietRecord

import android.content.Intent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaemebody.MainViewModel
import com.example.jaemebody.model.Exercise
import com.example.jaemebody.ui.components.AnimatedText
import com.example.jaemebody.ui.dietRecord.dietRecordInfo.DietRecordInfoActivity
import java.time.LocalDate

@Composable
fun DietRecordListScreen(
    mainViewModel: MainViewModel,
    onAddClicked: () -> Unit
) {
    val allExercises by mainViewModel.exerciseRecords.collectAsState()
    val today = LocalDate.now().toString()
    val todayExercises = allExercises.filter { it.date == today }
    val totalCalories = todayExercises.sumOf { it.calorie }
    val totalDuration = todayExercises.sumOf { it.duration }

    val exerciseList by mainViewModel.exerciseRecords.collectAsState()
    val targetCalories by mainViewModel.targetCalorie.collectAsState()

    LaunchedEffect(Unit){
        mainViewModel.loadTodayExercises()
    }

    val consumedCalories = exerciseList.sumOf { it.calorie }
    var showDialog by remember { mutableStateOf(false) }
    var newTarget by remember { mutableStateOf(targetCalories.toString()) }

    Box(modifier = Modifier
        .background(Color.Black)
        .fillMaxSize()
        .padding(16.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.padding(20.dp))

            AnimatedText(
                text = "오늘의 운동 현황",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.padding(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$consumedCalories / $targetCalories kcal",
                    color = Color.White,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "목표 변경",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .clickable { showDialog = true }
                        .background(Color.DarkGray, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }


            Spacer(modifier = Modifier.padding(20.dp))

            Box(contentAlignment = Alignment.Center){
                AnimatedCircularProgressBar(
                    consumedCalories = consumedCalories,
                    targetCalories = targetCalories,
                    modifier = Modifier.size(150.dp),
                    strokeWidth = 12.dp
                )
            }

            Spacer(modifier = Modifier.padding(20.dp))

            Text(
                text = "🔥 $totalCalories kcal 소모 / $totalDuration 분 운동",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn {
                items(exerciseList){ exercise ->
                    ExerciseRow(exercise = exercise)
                }
            }

        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ){
        Box(
            modifier = Modifier
                .padding(bottom = 20.dp, end = 20.dp)
                .size(50.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .clickable {
                    onAddClicked()
                },
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "+",
                fontSize = 30.sp,
                color = Color.Black
            )
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("목표 칼로리 변경", color = Color.White) },
            text = {
                TextField(
                    value = newTarget,
                    onValueChange = { newTarget = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.DarkGray,
                        unfocusedContainerColor = Color.DarkGray,
                        cursorColor = Color.White
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newTarget.all { it.isDigit() }) {
                        mainViewModel.setTargetCalorie(newTarget.toInt())
                        showDialog = false
                    }
                }) {
                    Text("확인", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("취소", color = Color.White)
                }
            },
            containerColor = Color.Black
        )
    }
}

@Composable
fun ExerciseRow(exercise: Exercise){

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .clickable{
                val intent = Intent(context, DietRecordInfoActivity::class.java)
                intent.putExtra("docId", exercise.docId)
                intent.putExtra("name", exercise.name)
                intent.putExtra("duration", exercise.duration.toString())
                intent.putExtra("calorie", exercise.calorie.toString())
                context.startActivity(intent)
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){

        Column{

            Text(
                text = exercise.name,
                color = Color.Cyan,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${exercise.duration} 분",
                color = Color.Gray
            )

        }
        Text(
            text = "${exercise.calorie} kcal",
            color = Color.White
        )
    }
}

@Composable
fun AnimatedCircularProgressBar(
    consumedCalories: Int,
    targetCalories: Int,
    modifier: Modifier,
    strokeWidth: Dp
){

    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(consumedCalories, targetCalories) {
        animatedProgress.animateTo(
            targetValue = consumedCalories.toFloat() / targetCalories,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    CircularProgressBar(
        progress = animatedProgress.value,
        modifier = modifier,
        strokeWidth = strokeWidth
    )

    Text(
        text = "${(animatedProgress.value * 100).toInt()}%",
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun CircularProgressBar(
    progress: Float,
    modifier: Modifier,
    strokeWidth: Dp,
    color: Color = Color.Green,
    backgroundColor: Color = Color.LightGray
){
    Canvas(modifier = modifier){
        val size = size.minDimension
        val radius = size / 2f
        val center = Offset(size / 2f, size/ 2f)
        val startAngle = -90f
        val strokeWidthPx = strokeWidth.toPx()

        // Draw the background circle
        drawCircle(
            color = backgroundColor,
            radius = radius - strokeWidthPx / 2,
            center = center,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidthPx)
        )

        // Draw the progress circle
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = progress * 360,
            useCenter = false,
            topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
            size = Size(size - strokeWidthPx, size - strokeWidthPx),
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)

        )
    }
}