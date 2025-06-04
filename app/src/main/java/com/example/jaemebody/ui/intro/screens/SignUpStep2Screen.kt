package com.example.jaemebody.ui.intro.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaemebody.ui.components.CustomGradientButton
import com.example.jaemebody.ui.components.CustomTextField

@Composable
fun SignUpStep2Screen(
    email: String,
    password: String,
    onRegister: (String, String, String, String, String, String) -> Unit, // email, password, name, age, height, weight
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "추가 정보 입력",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        CustomTextField(value = name, onValueChange = { name = it }, placeholder = "이름")
        CustomTextField(value = age, onValueChange = { age = it }, placeholder = "나이")
        CustomTextField(value = height, onValueChange = { height = it }, placeholder = "키")
        CustomTextField(value = weight, onValueChange = { weight = it}, placeholder = "몸무게")

        Spacer(modifier = Modifier.height(16.dp))

        CustomGradientButton(
            text = "회원가입 완료",
            onClick = {
                if (name.isNotBlank() && age.all { it.isDigit() } && height.all { it.isDigit() } && weight.all {it.isDigit()}) {
                    onRegister(email, password, name, age, height, weight)
                } else {
                    Toast.makeText(context, "정확히 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            },
            gradientColors = listOf(Color(0xFF6A1B9A), Color(0xFFAB47BC))
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onBack) {
            Text(text = "← 이전으로", color = Color.White)
        }
    }
}