package com.example.jaemebody.ui.profile

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaemebody.MainViewModel
import com.example.jaemebody.R
import com.example.jaemebody.ui.components.CustomGradientButton
import com.example.jaemebody.ui.intro.IntroActivity

@Composable
fun ProfileInfoScreen(
    mainViewModel: MainViewModel,
    name: String,
    age: String,
    height: String,
    onEditClicked: () -> Unit,
) {
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }


    Box(
       modifier = Modifier
           .fillMaxSize()
           .background(Color.Black)
           .padding(16.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "프로필",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            Spacer(modifier = Modifier.padding(20.dp))

            Image(
                painter = painterResource(id = R.drawable.user_img),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.padding(20.dp))

            ProfileInfo(label = "이름", value = name)
            ProfileInfo(label = "나이", value = "$age 살")
            ProfileInfo(label = "키", value = "$height cm")
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){
            CustomGradientButton(
                text = "수정하기",
                onClick = { onEditClicked() },
                gradientColors = listOf(Color(0xFF6A1B9A), Color(0xFFAB47BC)),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 100.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomGradientButton(
                text = "로그아웃",
                onClick = { showLogoutDialog = true },
                gradientColors = listOf(Color.Gray, Color.DarkGray),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
            )
        }
    }
    // 로그아웃 다이얼로그
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color(0xFF1C1C1E), // 블랙톤 배경
            modifier = Modifier
                .fillMaxWidth(0.85f) // 다이얼로그 너비 비율
                .wrapContentHeight(),
            title = {
                Text(
                    text = "로그아웃",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            },
            text = {
                Text(
                    text = "정말 로그아웃하시겠습니까?",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            mainViewModel.logout()
                            val intent = Intent(context, IntroActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6A1B9A),
                            contentColor = Color.White
                        )
                    ) {
                        Text("예", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { showLogoutDialog = false },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text("아니오", fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {} // Row 내부에 confirm/취소 모두 포함했으므로 비워둠
        )
    }
}

@Composable
fun ProfileInfo(label : String, value : String){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = Color.DarkGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ){
        Text(
            text = label,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = value,
            fontSize = 22.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
