package com.example.jaemebody.ui.intro.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaemebody.repository.FirebaseRepository
import com.example.jaemebody.ui.components.CustomTextField

@Composable
fun RecoverAccountScreen(
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var emailForReset by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("아이디(이메일) 찾기", color = Color.White, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(value = name, onValueChange = { name = it }, placeholder = "이름")
        CustomTextField(value = age, onValueChange = { age = it }, placeholder = "나이")

        Button(onClick = {
            FirebaseRepository.findEmailByNameAndAge(name, age) { email ->
                if (email != null) {
                    Toast.makeText(context, "아이디는 $email 입니다", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "일치하는 정보가 없습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("아이디 찾기")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider(color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        Text("비밀번호 재설정", color = Color.White, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        CustomTextField(value = emailForReset, onValueChange = { emailForReset = it }, placeholder = "이메일")

        Button(onClick = {
            FirebaseRepository.sendPasswordResetEmail(emailForReset) { success ->
                val msg = if (success) "비밀번호 재설정 메일을 보냈습니다" else "이메일을 확인해주세요"
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("비밀번호 재설정 메일 보내기")
        }

        Spacer(modifier = Modifier.height(20.dp))
        TextButton(onClick = onBack) {
            Text("← 로그인으로 돌아가기", color = Color.White)
        }
    }
}