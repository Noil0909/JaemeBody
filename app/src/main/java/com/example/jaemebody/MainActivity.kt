package com.example.jaemebody

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.jaemebody.ui.common.JaemeBodyScreen
import com.example.jaemebody.ui.main.MainNavigation
import com.example.jaemebody.ui.main.screens.DietScreen
import com.example.jaemebody.ui.main.screens.HomeScreen
import com.example.jaemebody.ui.main.screens.ProfileScreen
import com.example.jaemebody.ui.theme.JaemeBodyTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        mainViewModel.loadExercises()
    }
}
