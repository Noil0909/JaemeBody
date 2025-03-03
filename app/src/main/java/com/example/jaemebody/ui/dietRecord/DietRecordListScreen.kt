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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaemebody.MainViewModel
import com.example.jaemebody.model.Exercise
import com.example.jaemebody.ui.components.AnimatedText
import com.example.jaemebody.ui.dietRecord.dietRecordInfo.DietRecordInfoActivity

@Composable
fun DietRecordListScreen(
    mainViewModel: MainViewModel,
    onAddClicked: () -> Unit
) {

    val exerciseList by mainViewModel.exerciseRecords.collectAsState()

    LaunchedEffect(Unit){
        mainViewModel.loadExercises()
    }

    val targetCalories = 1500
    val consumedCalories = exerciseList.sumOf { it.calorie }

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
                text = "Diet Records",
                color = Color.White,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.padding(20.dp))

            Text(
                text = "$consumedCalories / $targetCalories kcal",
                color = Color.White,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.padding(20.dp))

            Box(contentAlignment = Alignment.Center){
                AnimatedCircularProgressBar(
                    consumedCalories = consumedCalories,
                    targetCalories = targetCalories,
                    modifier = Modifier.size(200.dp),
                    strokeWidth = 20.dp
                )
            }

            Spacer(modifier = Modifier.padding(20.dp))

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
}

@Composable
fun ExerciseRow(exercise: Exercise){

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(Color(0xFF500371), shape = RoundedCornerShape(10.dp))
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
                color = Color(0xFF00BFAE),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${exercise.duration} 분",
                color = Color(0xFF007DBF)
            )

        }
        Text(
            text = "${exercise.calorie} kcal",
            color = Color(0xFF007A8C)
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

    LaunchedEffect(consumedCalories) {
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
    color: Color = Color.Blue,
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