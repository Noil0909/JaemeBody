package com.example.jaemebody

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import com.example.jaemebody.ui.common.JaemeBodyScreen
import com.example.jaemebody.ui.main.MainNavigation
import com.example.jaemebody.ui.main.screens.CalendarScreen
import com.example.jaemebody.ui.main.screens.DietScreen
import com.example.jaemebody.ui.main.screens.HomeScreen
import com.example.jaemebody.ui.main.screens.ProfileScreen
import com.example.jaemebody.ui.theme.JaemeBodyTheme

@ExperimentalWearMaterialApi
@ExperimentalLayoutApi
class MainActivity : ComponentActivity() {

    private val mainViewModel = MainViewModel()
    companion object {
        private const val callPermission = android.Manifest.permission.CALL_PHONE
        private const val mediaPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
        private const val imagePermission = android.Manifest.permission.READ_MEDIA_IMAGES
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 앱 상단 상태바 숨기기
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        actionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  // API 33 이상
            if (ContextCompat.checkSelfPermission(this, imagePermission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(imagePermission),
                    100
                )
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, mediaPermission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(mediaPermission),
                    100
                )
            }
        }
        mainViewModel.loadExercises()

        setContent {
            JaemeBodyTheme {

                var currentScreen by remember {mutableStateOf(JaemeBodyScreen.Home)}

                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                ){
                    Box(modifier = Modifier
                        .padding(bottom = 60.dp)
                        .fillMaxSize()){
                        when(currentScreen){
                            JaemeBodyScreen.Home -> HomeScreen(mainViewModel = mainViewModel)
                            JaemeBodyScreen.Diet -> DietScreen(mainViewModel = mainViewModel)
                            JaemeBodyScreen.Calendar -> CalendarScreen(mainViewModel = mainViewModel)
                            JaemeBodyScreen.Profile -> ProfileScreen(mainViewModel = mainViewModel)
                        }
                    }
                }

                Box(modifier = Modifier
                    .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ){
                    MainNavigation(currentScreen = currentScreen, onTabSelected = {
                        currentScreen = it
                    })
                }
            }
        }

    }
    // 무조건  mainViewModel.loadExercises()가 onRestart되므로
    // Profile탭 등에서 액티비티 이동이 있을 수 있으므로
    // 현재 탭이 어딘지에 따라, 즉 Diet 탭에서만 onRestart 되도록 수정하면 더 좋을듯
    override fun onRestart(){
        super.onRestart()
        mainViewModel.loadTodayExercises()
    }
}
