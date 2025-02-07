package com.example.jaemebody.ui.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaemebody.ui.components.CustomGradientButton

@Composable
fun ProfileEditScreen(
    initialName: String,
    initialAge: String,
    initialHeight: String,
    onSaveClicked: (String, String, String) -> Unit,
    onCancelClicked: () -> Unit
) {

    val context = LocalContext.current

    var name by remember {mutableStateOf(initialName)}
    var age by remember { mutableStateOf(initialAge) }
    var height by remember { mutableStateOf(initialHeight) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "프로필 수정",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            ProfileEditField(label = "이름", value = name, onValueChange = {name = it})
            ProfileEditField(label = "나이", value = age, onValueChange = {age = it})
            ProfileEditField(label = "키", value = height, onValueChange = {height = it})

            Spacer(modifier = Modifier.height(20.dp))

            CustomGradientButton(text = "취소",
                onClick = {
                    onCancelClicked()
                },
                gradientColors = listOf(Color.Gray, Color.DarkGray)
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomGradientButton(text = "저장",
                onClick = {
                    if(name.isNotBlank() && age.all{it.isDigit()} && height.all {it.isDigit()}){
                        onSaveClicked(name, age, height)
                    }
                    else{
                        Toast.makeText(
                            // 예외처리에 디테일 추가해보기
                            context,
                            "이름은 문자, 나이와 키는 숫자를 입력해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                gradientColors = listOf(Color(0xFF6A1B9A), Color(0xFFAB47BC))
            )
        }
    }
}

@Composable
fun ProfileEditField(label: String, value: String, onValueChange: (String) -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ){
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 4.dp)
            )

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

    }
}
