package com.example.jaemebody.ui.profile

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.placeholder
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.jaemebody.MainViewModel
import com.example.jaemebody.R
import com.example.jaemebody.ui.components.CustomGradientButton
import com.example.jaemebody.ui.intro.IntroActivity

@Composable
@ExperimentalWearMaterialApi
@ExperimentalLayoutApi
fun ProfileInfoScreen(
    mainViewModel: MainViewModel,
    name: String,
    age: String,
    height: String,
    onEditClicked: () -> Unit
) {
    val context = LocalContext.current

    val showLogoutDialog = remember { mutableStateOf(false) }

    val profileImageUri by mainViewModel.profileImageUri.collectAsState()
    val profileImageUrl by mainViewModel.profileImageUrl.collectAsState()

    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            mainViewModel.setProfileImageUri(it)

            // 🔧 이미지 선택 즉시 업로드 → 저장
            mainViewModel.saveProfileImage(context, it) { success ->
                if (!success) {
                    Toast.makeText(context, "프로필 이미지 저장 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "프로필",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 프로필 이미지 변경
            ProfileImageSection(
                profileImageUri = profileImageUri,
                profileImageUrl = profileImageUrl,
                onPickImage = { imageLauncher.launch("image/*") },
                onResetImage = { mainViewModel.setProfileImageUri(null) }
            )
            Spacer(modifier = Modifier.height(20.dp))

            ProfileInfo(label = "이름", value = name)
            ProfileInfo(label = "나이", value = "$age 살")
            ProfileInfo(label = "키", value = "$height cm")
        }

        // 하단 버튼 영역
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column {
                CustomGradientButton(
                    text = "수정하기",
                    onClick = onEditClicked,
                    gradientColors = listOf(Color(0xFF6A1B9A), Color(0xFFAB47BC)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                )

                CustomGradientButton(
                    text = "로그아웃",
                    onClick = { showLogoutDialog.value = true },
                    gradientColors = listOf(Color.Gray, Color.DarkGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        }
    }


    // 로그아웃 다이얼로그
    if (showLogoutDialog.value) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog.value = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color(0xFF1C1C1E),
            modifier = Modifier
                .fillMaxWidth(0.85f)
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
                            showLogoutDialog.value = false
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
                        onClick = { showLogoutDialog.value = false },
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
            dismissButton = {}
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

@Composable
fun ProfileImageSection(
    profileImageUri: Uri?,
    profileImageUrl: String?,
    onPickImage: () -> Unit,
    onResetImage: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        // 프로필 이미지
        Image(
            painter = when {
                profileImageUri != null -> rememberAsyncImagePainter(profileImageUri)
                profileImageUrl != null -> rememberAsyncImagePainter(profileImageUrl)
                else -> painterResource(id = R.drawable.user_img)
            },
            contentDescription = "Profile Image",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // edit 아이콘
        Box(
            modifier = Modifier
                .size(30    .dp) //
                .offset(x = (-6).dp, y = (-6).dp)
                .clickable { expanded = true }
                .background(Color.White, CircleShape)
                .border(1.dp, Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_edit),
                contentDescription = "Edit Profile"
            )
        }

        // 팝업 메뉴
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = (-80).dp, y = 0.dp),
            modifier = Modifier
                .background(Color.White)
                .width(160.dp)
        ) {
            DropdownMenuItem(
                text = { Text("앨범에서 사진 선택", fontSize = 14.sp) },
                onClick = {
                    expanded = false
                    onPickImage()
                }
            )
            DropdownMenuItem(
                text = { Text("기본 이미지로 변경", fontSize = 14.sp) },
                onClick = {
                    expanded = false
                    onResetImage()
                }
            )
        }
    }
}