package com.example.jaemebody.ui.intro.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaemebody.R
import com.example.jaemebody.ui.components.CustomButton
import com.example.jaemebody.ui.components.CustomGradientButton
import com.example.jaemebody.ui.components.CustomTextField

@Composable
fun SignUpStep1Screen(
    onNext: (String, String) -> Unit,
    onBackToLogin: () -> Unit,
    errorMessage: String? = null
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_a),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(top = 100.dp, bottom = 32.dp)
                .size(200.dp)
                .clip(RoundedCornerShape(100.dp))
        )

        CustomTextField(value = email, onValueChange = { email = it }, placeholder = "이메일")
        CustomTextField(value = password, onValueChange = { password = it }, placeholder = "비밀번호", isPassword = true)

        errorMessage?.let {
            Text(text = it, color = Color.Red, fontSize = 16.sp, modifier = Modifier.padding(10.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomButton(
            text = "다음",
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    onNext(email, password)
                } else {
                    Toast.makeText(context, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            },
            backgroundColor = Color.Blue
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onBackToLogin) {
            Text(text = "← 로그인으로 돌아가기", color = Color.White, fontSize = 14.sp)
        }
    }
}
